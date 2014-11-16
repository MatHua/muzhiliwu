package com.muzhiliwu.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.model.TestDemo;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.LogFileFilter;

@IocBean
@At("test")
public class TestModule {
	
	@Inject
	private Dao dao;
	private static Log log = LogFactory.getLog(TestDemo.class);

	@At
	@Ok("json")
	public Object check() {
		TestDemo test = dao.fetch(TestDemo.class, "1");
		dao.fetchLinks(test, "myDemos");
		return test;
	}

	@At
	@Ok("json")
	public Object getNewly(String wisherId) {
		// name LIKE 'J%' AND age>20
		// ELECT * FROM t_person WHERE || name LIKE 'J%' AND age>20;
		// TestDemo test = dao.fetch(TestDemo.class,
		// Cnd.wrap("id in (select max(id) from t_test_demo)"));
		Wish wish = dao
				.fetch(Wish.class,
						Cnd.wrap("wisherId='"
								+ wisherId
								+ "' and date in (select max(date) from t_wish where wisherId='"
								+ wisherId + "')"));
		return wish;
	}

	@At("/?/name")
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myName(String id, HttpServletRequest request) {
		ActionMessage am = new ActionMessage();
		am.setMessage(id);
		am.setType(request.getRemoteAddr());
		return am;
	}

	@At
	@Ok("json")
	public Object myTest(String name) {
		ActionMessage am = new ActionMessage();
		List<TestDemo> tests = dao.query(TestDemo.class,
				Cnd.where("name", "=", name));
		dao.fetchLinks(tests, "myTests");
		am.setObject(tests);
		am.setMessage("雅妹蝶~");
		return am;
	}

	@At
	@Ok("json")
	public Object me(String name) {
		ActionMessage am = new ActionMessage();
		List<TestDemo> tests = dao.query(TestDemo.class,
				Cnd.where("name", "=", name));
		dao.fetchLinks(tests, "myTests");
		for (TestDemo test : tests) {
			// dao.fetchLinks(test.getMyTests(), "msgs");
		}
		am.setObject(tests);
		am.setMessage("雅妹蝶~");
		return am;
	}

	@At
	@Ok("json")
	public Object delete(String id) {
		TestDemo xx = dao.fetch(TestDemo.class, id);
		dao.fetchLinks(xx, "myTests");
		dao.deleteWith(xx, "myTests");

		ActionMessage am = new ActionMessage();
		List<TestDemo> tests = dao.query(TestDemo.class,
				Cnd.where("name", "=", "name"));
		dao.fetchLinks(tests, "myTests");

		am.setObject(tests);
		am.setMessage("雅妹蝶~");

		return am;
	}

	@At
	@Ok("json")
	public Object insert() {
		TestDemo test = new TestDemo();
		test.setId("xx_xx");
		test.setDate("xx_xx");
		test.setEmail("xx_xx");
		test.setName("xx_xx");
		dao.insert(test);

		TestDemo demo = new TestDemo();
		demo.setId("xxxxx");
		demo.setDate("xxxxx");
		demo.setEmail("xxxxx");
		demo.setName("xxxxx");
		dao.insert(demo);
		return "插入成功~";
	}

	@At
	@Ok("json")
	public Object getById1() {
		TestDemo test = dao.fetch(TestDemo.class, "xx_xx");
		return test;
	}

	@At
	@Ok("json")
	public Object getById2() {
		log.warn("查找一条信息");
		log.info("有新操作~");
		log.error("错误了~");
		// log.debug("测试~");
		// TestDemo test = dao.fetch(TestDemo.class, "xxxxx");
		return "xx";
	}
}
