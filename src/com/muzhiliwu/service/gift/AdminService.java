package com.muzhiliwu.service.gift;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.gift.Admin;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.utils.MD5;

@IocBean
public class AdminService {
	@Inject
	private Dao dao;

	public Admin checkAdmin(String code, String pass, boolean convert) {
		MD5 md5 = new MD5();
		Admin admin = dao.fetch(
				Admin.class,
				Cnd.where("code", "=", code).and("pass", "=",
						convert ? md5.getMD5ofStr(pass) : pass));
		return admin;
	}

	public QueryResult getShopList(Pager page) {
		// 审核通过的,还没下架的,没被删除的才能被显示
		List<Shop> shops = dao.query(Shop.class, Cnd.orderBy().desc("date"),
				page);
		if (page == null) {
			page = new Pager();
			page.setPageSize(-1);
		}
		page.setRecordCount(dao.count(Gift.class, null));
		return new QueryResult(shops, page);
	}

	public String banShop(Shop shop) {
		shop = dao.fetch(Shop.class, shop.getId());
		shop.setOkBusiness(Shop.BanBusiness);
		return shop.getCode();
	}

	public QueryResult getGiftListOfShop(Shop shop, Pager page, String orderBy) {

		String AscClickNum = "AscClickNum";// 点击量升序
		String DescClickNum = "DescClickNum";// 点击量降序
		String AscSalePrice = "AscSalePrice";// 销售价升序
		String DescSalePrice = "DescSalePrice";// 销售价降序
		String OnSale = "OnSale";// 在售
		String NotSale = "NotSale";// 已下架

		Criteria cri = Cnd.cri();
		cri.where().and("shopId", "=", shop.getId());
		cri.where().and("isDelete", "=", false);// 没被删除的
		if (AscClickNum.equals(orderBy)) {// 点击量升序
			cri.getOrderBy().asc("clickNum");
		} else if (DescClickNum.equals(orderBy)) {// 点降
			cri.getOrderBy().desc("clickNum");
		} else if (AscSalePrice.equals(orderBy)) {// 销售价升序
			cri.getOrderBy().asc("price");
		} else if (DescSalePrice.equals(orderBy)) {// 价降
			cri.getOrderBy().desc("price");
		} else if (OnSale.equals(orderBy)) {// 在售
			cri.where().and("saleState", "=", Gift.OnSale);
		} else if (NotSale.equals(orderBy)) {// 已下架
			cri.where().and("saleState", "=", Gift.NotSale);
		}
		List<Gift> gifts = dao.query(Gift.class, cri, page);
		if (page == null) {
			page = new Pager();
			page.setPageSize(-1);
		}
		page.setRecordCount(dao.count(Gift.class, cri));
		return new QueryResult(gifts, page);
	}

	public void auditGift(Gift gift) {
		Gift g = dao.fetch(Gift.class, gift.getId());
		g.setAuditState(gift.getAuditState());
		g.setAuditMess(gift.getAuditMess());
		dao.update(g);
	}
}
