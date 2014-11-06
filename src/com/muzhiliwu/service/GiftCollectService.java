package com.muzhiliwu.service;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.GiftCollect;
import com.muzhiliwu.model.GiftCollectPraise;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.service.gift.GiftService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class GiftCollectService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;
	@Inject
	private GiftService giftService;

	/**
	 * 收藏一个礼品商品
	 * 
	 * @param collector
	 *            收藏者
	 * @param gift
	 *            被收藏的礼品商品
	 * @return
	 */
	public String collectGift(User collector, Gift gift) {
		if (okCollect(collector, gift)) {// 可以被收藏
			GiftCollect collect = new GiftCollect();
			collect.setId(NumGenerator.getUuid());// 主键id
			collect.setDate(DateUtils.now());// 被收藏时间
			collect.setCollectorId(collector.getId());// 收藏者id
			collect.setGiftId(gift.getId());// 礼品id
			collect.setPraiseNum(0);// 点赞数为0
			collect.setCommentNum(0);// 评论数为0
			dao.insert(collect);

			giftService.changeGiftCollectNum(gift, 1);// 被收藏的礼品商品被收藏数+1
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 删除收藏某商品~~还没写~~
	 * 
	 * @param collector
	 * @param gift
	 * @return
	 */
	// public String deleteCollectGift(User collector, Gift gift) {
	// if (!okCollect(collector, gift)) {// 不可被收藏的才是已经被收藏的,才是可以被取消的
	//
	// return ActionMessage.success;
	// }
	// return ActionMessage.fail;
	// }

	/**
	 * 点赞礼品收藏
	 * 
	 * @param praiser
	 *            点赞者
	 * @param collect
	 *            被点赞的礼品收藏
	 * @return
	 */
	public String praiseGiftCollect(User praiser, GiftCollect collect) {
		if (okPraise(praiser, collect)) {
			GiftCollectPraise praise = new GiftCollectPraise();
			praise.setId(NumGenerator.getUuid());// 主键id
			praise.setDate(DateUtils.now());// 点赞时间
			praise.setCollectId(collect.getId());// 被点赞的礼品收藏的id
			praise.setPraiserId(praiser.getId());// 点赞者的id

			changeGiftCollectPraiseNum(collect, 1);// 对应商品收藏的点赞数+1
			// 发送一条点赞类未读信息~还没写
			return ActionMessage.success;
		} else {
			deletePraise(praiser, collect);// 删除对应的点赞记录
			changeGiftCollectPraiseNum(collect, -1);// 对应的点赞数-1
			// 删除对应的点赞类未读信息xxxxxxxx还没写
			return ActionMessage.cancel;
		}
	}

	// 改变礼品收藏的点赞数
	private void changeGiftCollectPraiseNum(GiftCollect collect, int increment) {
		collect = dao.fetch(GiftCollect.class, collect.getId());
		collect.setPraiseNum(collect.getPraiseNum() + increment);
		dao.update(collect);
	}

	// 判断是否已点赞
	private boolean okPraise(User praiser, GiftCollect collect) {
		GiftCollectPraise praise = dao.fetch(
				GiftCollectPraise.class,
				Cnd.where("praiserId", "=", praiser.getId()).and("collectId",
						"=", collect.getId()));
		return praise == null ? true : false;
	}

	// 删除对应的点赞
	private void deletePraise(User praiser, GiftCollect collect) {
		GiftCollectPraise praise = dao.fetch(
				GiftCollectPraise.class,
				Cnd.where("praiserId", "=", praiser.getId()).and("collectId",
						"=", collect.getId()));
		if (praise != null) {
			dao.delete(praise);
		}
	}

	// 判断该商品是否已被该用户收藏~
	private boolean okCollect(User collector, Gift gift) {
		GiftCollect collect = dao.fetch(
				GiftCollect.class,
				Cnd.where("collectorId", "=", collector.getId()).and("giftId",
						"=", gift.getId()));
		return collect == null ? true : false;
	}
}
