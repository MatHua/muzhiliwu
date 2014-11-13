package com.muzhiliwu.service.gift;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.Message;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.OrderForm;

@IocBean
public class OrderService {
	@Inject
	private Dao dao;

	public QueryResult getMyNotPayOrders(User user, Pager page) {
		List<OrderForm> orders = dao.query(
				OrderForm.class,
				Cnd.where("buyerId", "=", user.getId())
						.and("state", "=", OrderForm.WaitForPay).desc("date"),
				page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(
				OrderForm.class,
				Cnd.where("buyerId", "=", user.getId()).and("state", "=",
						OrderForm.WaitForPay)));
		dao.fetchLinks(orders, "gift");
		dao.fetchLinks(orders, "style");
		for (OrderForm order : orders) {
			dao.fetchLinks(order.getStyle(), "stylePics");
		}
		dao.fetchLinks(orders, "size");
		return new QueryResult(orders, page);
	}

}
