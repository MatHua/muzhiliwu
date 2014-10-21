package com.muzhiliwu.web;

import javax.servlet.http.HttpSession;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.muzhiliwu.service.UserService;

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
	
	// @At
	// @Ok("json")
	// public Object login(String code, String pass,HttpSession session) {
	// // if(Strings.isEmpty(code) || Strings.isEmail(input))
	// }
}
