package com.muzhiliwu.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.muzhiliwu.model.TestDemo;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
@At("test")
public class TestModule {
	@Inject
	private Dao dao;

	private static Log log = LogFactory.getLog(TestDemo.class);// Logs.get()

	@At("/?/name")
	@Ok("json")
	public Object myName(String id) {
		ActionMessage am = new ActionMessage();
		am.setMessage(id);
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
			dao.fetchLinks(test.getMyTests(), "msgs");
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
		for (TestDemo test : tests) {
			dao.fetchLinks(test.getMyTests(), "msgs");
		}
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
		TestDemo test = dao.fetch(TestDemo.class, "xxxxx");
		return test;
	}
}
