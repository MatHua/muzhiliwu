package com.muzhiliwu.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.muzhiliwu.model.User;
import com.muzhiliwu.service.UserService;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
@At("user")
public class UserModule {
	@Inject
	private UserService userService;// = new UserService();

	// @At
	// public JSONObject login(String name, String pass) {
	// User user = userService.checkUser(name, pass, true);
	// JSONObject json = new JSONObject();
	// if (user != null) {
	// json.put("id", user.getId());
	// json.put("name", user.getName());
	// json.put("pass", user.getPass());
	// json.put("mess", "这是敏姐");
	// }
	// return json;
	// }

	@At
	@Ok("json")
	public Object login(String code, String pass, HttpSession session,
			HttpServletResponse response) {
		if (Strings.isBlank(code) || Strings.isBlank(pass)) {
			ActionMessage am = new ActionMessage();
			am.setMessage("用户不存在或者密码错误");
			am.setType(ActionMessage.ACCOUNT_FAIL);
			return am;
		}
		code = code.trim();
		pass = pass.trim();
		User user = userService.checkUser(code, pass, true);
		if (user == null) {
			ActionMessage am = new ActionMessage();
			am.setMessage("用户名不存在或者密码错误");
			am.setType(ActionMessage.ACCOUNT_FAIL);
			return am;
		}
		session.setAttribute("t_user", user);// 登录用户信息
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(user);

		Cookie cookie = new Cookie("t_code", user.getCode());
		Cookie cookie1 = new Cookie("t_pass", user.getPass());
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setPath("/");
		cookie1.setMaxAge(60 * 60 * 24 * 365);
		cookie1.setPath("/");
		// cookie.setDomain(".iisp.com");
		response.addCookie(cookie1);
		response.addCookie(cookie);
		return am;
	}

	@At
	@Ok("json")
	public Object regist(@Param("::user.") User user) {
		ActionMessage am = new ActionMessage();
		if (userService.registOrEditUser(user, user.getPass())) {
			am.setMessage("注册成功");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("注册失败,账号已被注册");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 修改个人资料
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object editSelf(User user, HttpSession session) {
		ActionMessage am = new ActionMessage();
		if (userService.registOrEditUser(user, user.getPass())) {
			am.setType(ActionMessage.success);
			am.setMessage("个人信息修改成功~");
		} else {
			am.setType(ActionMessage.fail);
			am.setMessage("个人信息修改失败~");
		}
		return am;
	}

	@At
	@Ok("json")
	public Object logout(HttpSession session) {
		session.invalidate();
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		return am;
	}
}
