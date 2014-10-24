package com.muzhiliwu.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class WishService {
	@Inject
	private Dao dao;
}
