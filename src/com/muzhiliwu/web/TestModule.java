package com.muzhiliwu.web;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.muzhiliwu.model.MyTest;
import com.muzhiliwu.model.TestDemo;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
@At("test")
public class TestModule {
	@Inject
	private Dao dao;

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
}
