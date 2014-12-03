package com.muzhiliwu.listener;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;

import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.service.gift.ShopService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.AdminUtils;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;

@IocBean
public class CheckSuperAdminLoginFilter implements ActionFilter {
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

		// check session
		HttpSession session = Mvcs.getHttpSession(false);
		if (session == null) {
			am.setType(ActionMessage.Not_Login);
			am.setMessage("超级管理员没有登录");
			v.setData(am);
			return v;
		}

		Object obj = session.getAttribute(name);
		if (obj == null
				|| !AdminUtils.superAdminId.equals(((Shop) obj).getId())) {// 如果用户名没保存在session中

			log.info("[ip:" + IpUtils.getIpAddr(context.getRequest())
					+ "]  [用户:" + "游客" + "]  [时间:" + DateUtils.now()
					+ "]  [操作:" + "正在尝试恶意访问~_~]");
			am.setType(ActionMessage.Not_Login);
			am.setMessage("超级管理员没有登录");
			v.setData(am);
			return v;
		}
		return null;// 如果session有保存,就不用跳到登陆页
	}
}