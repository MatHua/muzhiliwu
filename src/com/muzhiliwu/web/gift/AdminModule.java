package com.muzhiliwu.web.gift;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.nutz.mvc.view.ViewWrapper;

import com.muzhiliwu.listener.CheckSuperAdminLoginFilter;
import com.muzhiliwu.model.gift.Admin;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.service.gift.AdminService;
import com.muzhiliwu.service.gift.ShopService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;
import com.muzhiliwu.utils.LogFileFilter;
import com.muzhiliwu.utils.LogView;

@IocBean
@At("/admin")
public class AdminModule {
	@Inject
	private AdminService adminService;
	@Inject
	private ShopService shopService;
	private static Log log = LogFactory.getLog(AdminModule.class);

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckSuperAdminLoginFilter.class, args = { "ioc:checkSuperAdminLoginFilter" }))
	public Object auditGift(@Param("::gift.") Gift gift, HttpSession session,
			HttpServletRequest request) {
		Admin admin = (Admin) session.getAttribute("a_admin");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (admin != null ? admin.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "审核一件商品]");
		ActionMessage am = new ActionMessage();
		if (Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("~参数gift.id不能为空~");
			return am;
		}
		if (Strings.isBlank(gift.getAuditState())) {
			am.setType(ActionMessage.fail);
			am.setMessage("~审核结果不能为空~");
			return am;
		}
		if (!Gift.AuditFail.equals(gift.getAuditState())
				&& !Gift.AuditSuccess.equals(gift.getAuditState())) {
			am.setType(ActionMessage.fail);
			am.setMessage("~审核结果不能辨别:审核结果必须是'AuditFail'或'AuditSuccess'~");
			return am;
		}
		adminService.auditGift(gift);
		am.setType(ActionMessage.success);
		am.setMessage("~审核成功~");
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckSuperAdminLoginFilter.class, args = { "ioc:checkSuperAdminLoginFilter" }))
	public Object getGiftListOfShop(@Param("::shop.") Shop shop,
			String orderBy, @Param("::page.") Pager page, HttpSession session,
			HttpServletRequest request) {
		Admin admin = (Admin) session.getAttribute("a_admin");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (admin != null ? admin.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取某商家礼物商品列表]");

		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());
		QueryResult result = adminService
				.getGiftListOfShop(shop, page, orderBy);

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
	@Filters(@By(type = CheckSuperAdminLoginFilter.class, args = { "ioc:checkSuperAdminLoginFilter" }))
	public Object banShop(@Param("::shop.") Shop shop, HttpSession session,
			HttpServletRequest request) {
		Admin admin = (Admin) session.getAttribute("a_admin");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (admin != null ? admin.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "禁止某商家营业]");
		ActionMessage am = new ActionMessage();
		if (Strings.isBlank(shop.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("~参数shop.id不能为空~");
			return am;
		}

		String result = adminService.banShop(shop);

		am.setMessage("~商家[" + result + "]被成功禁止营业~");
		am.setType(ActionMessage.success);
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object logout(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		if (session.getAttribute("s_shop") != null) {
			session.removeAttribute("s_shop");
		}

		ActionMessage am = new ActionMessage();
		am.setMessage("成功退出登录~");
		am.setType(ActionMessage.success);
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckSuperAdminLoginFilter.class, args = { "ioc:checkSuperAdminLoginFilter" }))
	public Object shopList(@Param("::page.") Pager page, HttpSession session,
			HttpServletRequest request) {
		Admin admin = (Admin) session.getAttribute("a_admin");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (admin != null ? admin.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取商家列表]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = adminService.getShopList(page);

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
	public Object login(@Param("::admin.") Admin admin, HttpSession session,
			HttpServletRequest request) {
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ admin.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "尝试登陆~]");
		if (admin == null || Strings.isBlank(admin.getCode())
				|| Strings.isBlank(admin.getPass())) {
			ActionMessage am = new ActionMessage();
			am.setMessage("用户名或密码不能为空");
			am.setType(ActionMessage.fail);
			return am;
		}
		admin.setCode(admin.getCode().trim());
		admin.setPass(admin.getPass().trim());

		Admin a = adminService.checkAdmin(admin.getCode(), admin.getPass(),
				true);
		if (a == null) {
			log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
					+ admin.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
					+ "用户名不存在或者密码错误]");

			ActionMessage am = new ActionMessage();
			am.setMessage("用户名不存在或者密码错误");
			am.setType(ActionMessage.Account_Fail);
			return am;
		}
		session.setAttribute("a_admin", a);// 登录用户信息
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setMessage("登录成功~");
		// am.setObject(u);
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object logList(ServletContext context) {
		String path = context.getRealPath("/") + "/WEB-INF/logs/";
		String regx = ".log";
		File f2 = new File(path);
		File[] s = f2.listFiles(new LogFileFilter(regx));
		ActionMessage am = new ActionMessage();
		List<String> list = new ArrayList<String>();
		for (File file : s) {
			list.add(file.getName());
		}
		am.setObject(list);
		am.setMessage("获取日志文件目录成功~");
		am.setType(ActionMessage.success);
		return am;
		// return new ViewWrapper(new LogView("logs.muzhiliwu_error"), null);
	}

	@At
	@POST
	public ViewWrapper lookLog(String file) {
		return new ViewWrapper(new LogView(file), null);
	}
}
