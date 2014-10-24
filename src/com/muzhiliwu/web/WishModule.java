package com.muzhiliwu.web;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.service.WishService;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
@At("wish")
public class WishModule {
	@Inject
	private WishService wishService;
	@Inject
	private Dao dao;// 用于测试而已

	// 许愿
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object publish(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.publishOrUpdateWish(publisher, wish,
				session);

		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("积分不够,不能发表愿望~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}

	// 修改愿望
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object update(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.publishOrUpdateWish(publisher, wish,
				session);
		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望修改成功~");
			am.setType(ActionMessage.success);
		}
		return am;
	}

}
