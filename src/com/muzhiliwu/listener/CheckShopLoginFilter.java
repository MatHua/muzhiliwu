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
import org.nutz.mvc.view.UTF8JsonView;

import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.service.gift.ShopService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;
import com.muzhiliwu.utils.MD5;

//检查商家有没有登录
@IocBean
public class CheckShopLoginFilter implements ActionFilter {
	private static Log log = LogFactory.getLog(CheckLoginFilter.class);

	private static final String name = "s_shop";
	private static final String path = "/index.jsp";
	@Inject
	private ShopService shopService;

	public View match(ActionContext context) {
		// check cookie
		UTF8JsonView v = new UTF8JsonView(JsonFormat.compact());
		ActionMessage am = new ActionMessage();

		// Cookie cookies[] = Mvcs.getReq().getCookies();
		String code = "";
		String pass = "";
		// if (cookies != null)
		// for (Cookie cookie : cookies) {
		// if (cookie.getName().equals("s_code")) {
		// code = cookie.getValue();
		// }
		// if (cookie.getName().equals("s_pass")) {
		// pass = cookie.getValue();
		// }
		// String tmpCode = Mvcs.getReq().getHeader("s_code");
		// String tmpPass = Mvcs.getReq().getHeader("s_pass");
		// if (tmpCode != null) {
		// code = tmpCode;
		// }
		// if (tmpPass != null) {
		// pass = MD5.toMD5(tmpPass);
		// }
		// }

		// check session
		HttpSession session = Mvcs.getHttpSession(false);
		if (session == null) {
			am.setType(ActionMessage.Not_Login);
			am.setMessage("商家没有登录");
			v.setData(am);
			return v;
		}

		Object obj = session.getAttribute(name);
		if (obj == null) {// 如果用户名没保存在session中
			// if (!Strings.isBlank(code) && !Strings.isBlank(pass)) {
			// Shop shop = shopService.checkShop(code, pass, false);
			// if (shop != null) {// 但cookie有保存
			// session.setAttribute(name, shop);
			// return null;// 就不用跳到登录页
			// }
			// }
			log.info("[ip:" + IpUtils.getIpAddr(context.getRequest())
					+ "]  [用户:" + "游客" + "]  [时间:" + DateUtils.now()
					+ "]  [操作:" + "正在尝试恶意访问~_~]");
			am.setType(ActionMessage.Not_Login);
			am.setMessage("商家没有登录");
			v.setData(am);
			return v;
			// session和cookie都没有保存,当然得跳到登陆页
		}
		return null;// 如果session有保存,就不用跳到登陆页
	}
}
