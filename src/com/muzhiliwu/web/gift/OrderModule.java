package com.muzhiliwu.web.gift;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.OrderForm;
import com.muzhiliwu.service.gift.OrderService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;

@IocBean
@At("order")
public class OrderModule {
	@Inject
	private OrderService orderService;
	private static Log log = LogFactory.getLog(OrderModule.class);

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myNotPayOrders(@Param("::page.") Pager page,
			HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取未付款订单]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = orderService.getMyNotPayOrders(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());
		return ams;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object deleteNotPayOrder(@Param("::order.") OrderForm order,
			HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "正在尝试删除订单~]");
		ActionMessage am = new ActionMessage();
		if (order == null || Strings.isBlank(order.getOrderId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请求参数order.orderId不能为空~");
			return am;
		}
		String resule = orderService.deleteNotPayOrder(user, order);
		if (ActionMessage.fail.equals(resule)) {
			am.setType(ActionMessage.fail);
			am.setMessage("删除失败,您或许不是订单创建者,亦或许该订单不是未付款订单~");
			return am;
		}
		am.setType(ActionMessage.success);
		am.setMessage("未付款订单删除成功^_^");
		return am;
	}
}
