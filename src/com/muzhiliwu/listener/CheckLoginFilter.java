package com.muzhiliwu.listener;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.UTF8JsonView;

import com.muzhiliwu.model.User;
import com.muzhiliwu.service.UserService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;
import com.muzhiliwu.utils.MD5;
import com.muzhiliwu.web.UserModule;

//检查用户有没有登录
@IocBean
public class CheckLoginFilter implements ActionFilter {
	private static Log log = LogFactory.getLog(CheckLoginFilter.class);

	private static final String name = "t_user";
	private static final String path = "/index.jsp";
	@Inject
	private UserService userService;

	public View match(ActionContext context) {
		// check cookie
		UTF8JsonView v = new UTF8JsonView(JsonFormat.compact());
		ActionMessage am = new ActionMessage();

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

		// check session
		HttpSession session = Mvcs.getHttpSession(false);
		if (session == null)
			return new ServerRedirectView(path);// 连session都找不到,应该是没登陆吧~那就去登陆
		Object obj = session.getAttribute(name);
		if (obj == null) {// 如果用户名没保存在session中
			if (!Strings.isBlank(username) && !Strings.isBlank(password)) {
				User user = userService.checkUser(username, password, false);
				if (user != null) {// 但cookie有保存
					session.setAttribute("t_user", user);
					return null;// 就不用跳到登录页
				}
			}
			log.info("[ip:" + IpUtils.getIpAddr(context.getRequest())
					+ "]  [用户:" + "游客" + "]  [时间:" + DateUtils.now()
					+ "]  [操作:" + "正在尝试恶意访问~_~]");
			return new ServerRedirectView(path);// session和cookie都没有保存,当然得跳到登陆页
		}
		return null;// 如果session有保存,就不用跳到登陆页
	}
}
