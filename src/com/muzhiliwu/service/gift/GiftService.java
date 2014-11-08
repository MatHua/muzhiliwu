package com.muzhiliwu.service.gift;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.service.UserService;

@IocBean
public class GiftService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;
	
	

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
}
