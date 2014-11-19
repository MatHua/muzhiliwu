package com.muzhiliwu.web;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
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
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.model.User;
import com.muzhiliwu.service.UserService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;

@IocBean
@At("user")
public class UserModule {
	@Inject
	private UserService userService;
	private static Log log = LogFactory.getLog(UserModule.class);

	// 测试
	@At
	@Ok("json")
	public Object login(@Param("::user.") User user, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "尝试登陆~]");
		if (user == null || Strings.isBlank(user.getCode())
				|| Strings.isBlank(user.getPass())) {
			ActionMessage am = new ActionMessage();
			am.setMessage("用户名或密码不能为空");
			am.setType(ActionMessage.fail);
			return am;
		}
		user.setCode(user.getCode().trim());
		user.setPass(user.getPass().trim());

		User u = userService.checkUser(user.getCode(), user.getPass(), true);
		if (u == null) {
			log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
					+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
					+ "用户名不存在或者密码错误]");

			ActionMessage am = new ActionMessage();
			am.setMessage("用户名不存在或者密码错误");
			am.setType(ActionMessage.Account_Fail);
			return am;
		}
		session.setAttribute("t_user", u);// 登录用户信息
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setMessage("登录成功~");
		// am.setObject(u);

		// Cookie cookie = new Cookie("t_code", user.getCode());
		// Cookie cookie1 = new Cookie("t_pass", user.getPass());
		// cookie.setMaxAge(60 * 60 * 24 * 365);
		// cookie.setPath("/");
		// cookie1.setMaxAge(60 * 60 * 24 * 365);
		// cookie1.setPath("/");
		// cookie.setDomain(".iisp.com");
		// response.addCookie(cookie1);
		// response.addCookie(cookie);
		return am;
	}

	// 几时检查用户输入的用户名是否已被注册
	@At
	@Ok("json")
	public Object checkRepeat(@Param("::user.") User user) {
		ActionMessage am = new ActionMessage();
		if (userService.checkRepeat(user.getCode())) {
			am.setMessage("该账号可用^_^");
			am.setType(ActionMessage.success);
		} else {
			am.setType(ActionMessage.fail);
			am.setMessage("用户已存在,请选择其他账号~");
		}
		return am;
	}

	// 注册
	@At
	@Ok("json")
	public Object regist(@Param("::user.") User user, String repass,
			HttpServletRequest request) {
		ActionMessage am = new ActionMessage();
		if (user == null || Strings.isBlank(user.getCode())
				|| Strings.isBlank(user.getPass()) || Strings.isBlank(repass)) {
			am.setMessage("注册失败,账号或密码不能为空~");
			am.setType(ActionMessage.fail);
			return am;
		}
		if (!repass.equals(user.getPass())) {
			am.setMessage("注册失败,两次密码输入不一致~");
			am.setType(ActionMessage.fail);
			return am;
		}
		String tmp = userService.registUser(user);
		if (ActionMessage.success.equals(tmp)) {

			am.setMessage("注册成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("注册失败,账号已被注册~");
			am.setType(ActionMessage.fail);
		}
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "尝试注册~]");
		return am;
	}

	// 修改个人资料
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object editSelf(@Param("::user.") User user, HttpSession session) {
		ActionMessage am = new ActionMessage();
		String tmp = userService.editUser(user, user.getPass());
		if (ActionMessage.success.equals(tmp)) {
			am.setType(ActionMessage.success);
			am.setMessage("个人信息修改成功~");
		} else {
			am.setType(ActionMessage.fail);
			am.setMessage("个人信息修改失败~");
		}
		return am;
	}

	// 修改密码
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object changePass(String oldPass, String newPass, HttpSession session) {
		User user = (User) session.getAttribute("t_user");

		ActionMessage am = new ActionMessage();
		if (userService.changePass(user, oldPass, newPass)) {
			am.setMessage("密码修改成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("旧密码填写错误,修改密码未成功~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 退出登录
	@At
	@Ok("json")
	public Object logout(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		if (session.getAttribute("t_user") != null) {
			session.removeAttribute("t_user");
		}
		// 销毁Cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("t_code")
						|| cookie.getName().equals("t_pass")) {
					cookie.setMaxAge(0);
				}
				response.addCookie(cookie);
			}
		}
		ActionMessage am = new ActionMessage();
		am.setMessage("成功退出登录~");
		am.setType(ActionMessage.success);
		return am;
	}

	// 获取个人详细信息
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myDetail(HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取个人基本信息]");
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(userService.getMyDetail(user.getId()));
		return am;
	}

	// 获取个人信息
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object me(HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取个人信息]");

		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(userService.getUserById(user.getId()));
		return am;
	}

	// 获取未读信息数
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object countUnreadReplyNum(HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取未读信息数]");
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(userService.countUnreadReplyNum(user.getId()));
		return am;
	}

	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object unreadReply(@Param("::page.") Pager page,
			HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取未读信息]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = userService.getMyUnreadReply(user, page);

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
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object reply(@Param("::page.") Pager page, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取未读信息]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = userService.getMyReply(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());
		return ams;
	}

	// 上传用户头像
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object uploadUserPhoto(@Param("userpic") TempFile tfs,
			ServletContext context, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		boolean result = userService.uploadPhoto(user.getCode(), tfs,
				context.getRealPath("/"));

		ActionMessage am = new ActionMessage();
		if (result) {
			am.setMessage("头像上传成功^_^");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("头像上传失败,原因可能由于用户不存在~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 获取用户头像
	// @At
	// @Ok("void")
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public void getUserPic(HttpServletResponse response, String code,
	// ServletContext context) throws IOException {
	// ServletOutputStream out = response.getOutputStream();// 获取输出流
	// response.setContentType("image/gif");
	// response.setHeader("Pragma", "No-cache");
	// response.setHeader("Cache-Control", "no-cache");
	// response.setDateHeader("Expires", 0);
	//
	// // 通过过滤查找图片
	// String regx = code + ".*";
	// File f2 = new File(context.getRealPath("/") + "/WEB-INF/userphoto/");
	//
	// File[] s = f2.listFiles(new FileFilter(regx));
	// if (s == null || s.length <= 0) {
	// return;
	// }
	//
	// InputStream fis = new FileInputStream(s[0]);
	// int i = fis.available();// 得到文件大小
	// byte buf[] = new byte[i];
	// int len = 0;
	// while ((len = fis.read(buf)) != -1) {
	// out.write(buf, 0, len);
	// }
	// out.close();
	// fis.close();
	// }
}
