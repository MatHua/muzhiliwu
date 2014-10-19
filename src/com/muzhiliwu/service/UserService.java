package com.muzhiliwu.service;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.muzhiliwu.model.User;

@IocBean
public class UserService {
	@Inject
	private Dao dao;

	public User checkUser(String name, String pass) {
		if (Strings.isEmpty(name) || Strings.isEmpty(pass)) {
			return null;
		}
		User user = dao.fetch(User.class,
				Cnd.where("name", "=", name).and("pass", "=", pass));
		return user;
	}
}
