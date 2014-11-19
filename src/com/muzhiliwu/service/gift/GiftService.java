package com.muzhiliwu.service.gift;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.GiftCollect;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.GiftShare;
import com.muzhiliwu.model.gift.OrderForm;
import com.muzhiliwu.model.gift.OrderFormForWish;
import com.muzhiliwu.service.GiftCollectService;
import com.muzhiliwu.service.UserService;

@IocBean
public class GiftService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;
	@Inject
	private GiftCollectService giftCollectService;

	/**
	 * 改变礼品的被收藏数
	 * 
	 * @param gift
	 *            被收藏的礼品
	 * @param increment
	 *            被收藏数增量
	 * @return
	 */
	public boolean changeGiftCollectNum(Gift gift, int increment) {
		gift = dao.fetch(Gift.class, gift.getId());
		gift.setCollectNum(gift.getCollectNum() + increment);
		dao.update(gift);
		return true;
	}

	/**
	 * 获取礼物商品列表
	 * 
	 * @param user
	 *            已登录的用户
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getGiftList(User user, Pager page) {
		// 审核通过的,还没下架的,没被删除的才能被显示
		List<Gift> gifts = dao.query(Gift.class,
				Cnd.where("auditState", "=", Gift.AuditSuccess)// 审核通过
						.and("isDelete", "=", false)// 没被删除
						.and("saleState", "=", Gift.OnSale)// 在售中(还没下架)
						.desc("date"), page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(Gift.class,
				Cnd.where("auditState", "=", Gift.AuditSuccess)// 审核通过
						.and("isDelete", "=", false)// 没被删除
						.and("saleState", "=", Gift.OnSale)// 在售中(还没下架)
				));
		dao.fetchLinks(gifts, "tags");// 获取标签
		dao.fetchLinks(gifts, "pics");// 获取礼品图片

		for (Gift gift : gifts) {
			gift.setPrice(Float.parseFloat(Gift.df.format(gift.getPrice())));// 130.000000->130.0
			gift.setCollectNum(getGiftCollectNum(gift));// 获取收藏数
			gift.setShareNum(getGiftShareNum(gift));// 获取分享数
			if (user != null) {
				if (!okCollect(user, gift))// 可否收藏
					gift.setCollected(true);
				if (hasShared(user, gift))// 是否分享过
					gift.setShared(true);
				if (hasBuyed(user, gift))// 是否购买过,或者已存在在购物车中
					gift.setBuyed(true);
				if (hasWished(user, gift))// 是否用该商品许愿果
					gift.setWished(true);
			}
		}
		return new QueryResult(gifts, page);
	}

	// 获取收藏数
	public int getGiftCollectNum(Gift gift) {
		return dao.count(GiftCollect.class,
				Cnd.where("giftId", "=", gift.getId()));
	}

	// 获取分享数
	public int getGiftShareNum(Gift gift) {
		return dao.count(GiftShare.class,
				Cnd.where("giftId", "=", gift.getId()));
	}

	// 判断该商品是否已被该用户收藏~
	private boolean okCollect(User collector, Gift gift) {
		GiftCollect collect = dao.fetch(
				GiftCollect.class,
				Cnd.where("collectorId", "=", collector.getId()).and("giftId",
						"=", gift.getId()));
		return collect == null ? true : false;
	}

	// 判断是否分享
	private boolean hasShared(User sharer, Gift gift) {
		int tmp = dao.count(
				GiftShare.class,
				Cnd.where("sharerId", "=", sharer.getId()).and("giftId", "=",
						gift.getId()));
		return (tmp == 0) ? false : true;
	}

	// 判断是否购买过,或者已存在在购物车
	private boolean hasBuyed(User buyer, Gift gift) {
		int tmp = dao.count(
				OrderForm.class,
				Cnd.where("buyerId", "=", buyer.getId()).and("giftId", "=",
						gift.getId()));
		return (tmp == 0) ? false : true;
	}

	private boolean hasWished(User wisher, Gift gift) {
		int tmp = dao.count(
				OrderFormForWish.class,
				Cnd.where("creatorId", "=", wisher.getId()).and("giftId", "=",
						gift.getId()));
		return (tmp == 0) ? false : true;
	}
}
