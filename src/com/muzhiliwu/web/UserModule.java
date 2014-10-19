package com.muzhiliwu.web;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;

import com.alibaba.fastjson.JSONObject;
import com.muzhiliwu.model.User;
import com.muzhiliwu.service.UserService;
//xxx
@IocBean
@At("user")
public class UserModule {
	@Inject
	private UserService userService;

	@At
	public JSONObject login(String name, String pass) {
		User user = userService.checkUser(name, pass);
		JSONObject json = new JSONObject();
		if (user != null) {
			json.put("id", user.getId());
			json.put("name", user.getName());
			json.put("pass", user.getPass());
			json.put("mess", "�Ұ�����");
		}
		return json;
	}
}
