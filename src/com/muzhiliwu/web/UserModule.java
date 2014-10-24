package com.muzhiliwu.web;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.muzhiliwu.model.User;
import com.muzhiliwu.service.UserService;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
@At("user")
public class UserModule {
	@Inject
	private UserService userService;

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
			am.setType(ActionMessage.Account_Fail);
			return am;
		}
		code = code.trim();
		pass = pass.trim();
		User user = userService.checkUser(code, pass, true);
		if (user == null) {
			ActionMessage am = new ActionMessage();
			am.setMessage("用户名不存在或者密码错误");
			am.setType(ActionMessage.Account_Fail);
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

	// 几时检查用户输入的用户名是否已被注册
	@At
	@Ok("json")
	public Object checkRepeat(String code) {
		ActionMessage am = new ActionMessage();
		if (userService.checkRepeat(code)) {
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
	public Object regist(@Param("::user.") User user) {
		ActionMessage am = new ActionMessage();
		// if (user == null) {
		// am.setMessage("null");
		// return am;
		// }
		if (Strings.isBlank(user.getCode()) || Strings.isBlank(user.getPass())) {
			am.setMessage("注册失败,账号或密码不能为空~");
			am.setType(ActionMessage.fail);
			return am;
		}
		if (userService.registOrEditUser(user, user.getPass())) {
			am.setMessage("注册成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("注册失败,账号已被注册~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 修改个人资料
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object editSelf(@Param("::user.") User user, HttpSession session) {
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

	// 退出登录
	@At
	@Ok("json")
	public Object logout(HttpSession session) {
		session.removeAttribute("t_user");
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		return am;
	}

	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object me(HttpSession session) {
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(userService.getUserById(((User) session
				.getAttribute("t_user")).getId()));
		return am;
	}

	// 上传用户头像
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object uploadUserPhoto(@Param("userpic") TempFile tfs, String code,
			ServletContext context) {
		userService.uploadPhoto(code, tfs, context.getRealPath("/"));
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		return am;
	}
}
