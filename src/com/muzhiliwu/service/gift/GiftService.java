package com.muzhiliwu.service.gift;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
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
		List<Gift> gifts = dao.query(Gift.class, Cnd.orderBy().desc("date"),
				page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(Gift.class));
		dao.fetchLinks(gifts, "tags");// 获取标签

		for (Gift gift : gifts) {
			gift.setPrice(Float.parseFloat(Gift.df.format(gift.getPrice())));
			gift.setCollected(false);
			if (user != null) {
				if (!giftCollectService.okCollect(user, gift))
					gift.setCollected(true);
			}
		}
		return new QueryResult(gifts, page);
	}
}
