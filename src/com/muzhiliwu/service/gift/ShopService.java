package com.muzhiliwu.service.gift;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.utils.MD5;

@IocBean
public class ShopService {
	@Inject
	private Dao dao;

	public Shop checkShop(String code, String pass, boolean convert) {
		MD5 md5 = new MD5();
		Shop shop = dao.fetch(
				Shop.class,
				Cnd.where("code", "=", code).and("pass", "=",
						convert ? md5.getMD5ofStr(pass) : pass));
		return shop;
	}
}
