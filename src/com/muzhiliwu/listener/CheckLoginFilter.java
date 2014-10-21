package com.muzhiliwu.listener;

import javax.management.relation.Role;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;

import com.muzhiliwu.model.User;
import com.muzhiliwu.service.UserService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.MD5;

//检查用户有没有登录
@IocBean
public class CheckLoginFilter implements ActionFilter {

	private String name = "user";// session中存储用户信息的parameter
	@Inject
	private UserService userService;

	public View match(ActionContext context) {
		UTF8JsonView v = new UTF8JsonView(JsonFormat.compact());
		ActionMessage am = new ActionMessage();

		HttpSession session = Mvcs.getHttpSession(true);
		Cookie cookies[] = Mvcs.getReq().getCookies();
		String username = "";
		String password = "";
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("t_code")) {
					username = cookie.getValue();
				}
				if (cookie.getName().equals("t_pass")) {
					password = cookie.getValue();
				}
				String name = Mvcs.getReq().getHeader("t_code");
				String passwd = Mvcs.getReq().getHeader("t_pass");
				if (name != null) {
					username = name;
				}
				if (passwd != null) {
					password = MD5.toMD5(passwd);
				}
			}

		Object obj = session.getAttribute(name);
		// 用于跳转到输入密码前的链接
		if (null == obj) {

			if (!Strings.isBlank(username) && !Strings.isBlank(password)) {
				User user = userService.checkUser(username, password, false);
				return null;
			}
			am.setMessage("用户没有登陆");
			am.setType(ActionMessage.NOT_LOGIN);
			v.setData(am);
			return v;
		}
		return null;
	}
}
