package com.muzhiliwu.service;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.muzhiliwu.model.TestDemo;
import com.muzhiliwu.model.UserTag;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
public class TestDemoService {//
	@Inject
	private Dao dao;

	public TestDemo getDemo(String id) {
		TestDemo demo = dao.fetch(TestDemo.class, id);
		// Number num = new ;
		return demo;
	}
	
	public void saveDemo(String name, String email) {
		TestDemo demo = new TestDemo();
		demo.setId(new Date().toString());
		demo.setName(name);
		demo.setEmail(email);
		dao.insert(demo);
	}
}
