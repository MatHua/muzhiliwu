package com.muzhiliwu.web;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.muzhiliwu.service.TestDemoService;
import com.muzhiliwu.utils.ActionMessage;

@IocBean
@At("/demo")
public class TestDemoModule {
	@Inject
	private TestDemoService testDemoService;
	
	@At
	@Ok("json")
	public ActionMessage getDemo(String id) {
		ActionMessage am = new ActionMessage();
		am.setType("success");
		am.setMessage("get message ok!");
		am.setObject(testDemoService.getDemo(id));
		return am;
	}
	
	@At
	@Ok("json")
	public ActionMessage saveDemo(String name, String email) {
		testDemoService.saveDemo(name, email);
		ActionMessage am = new ActionMessage();
		am.setType("success");
		am.setMessage("save message ok!");
		
		am.setObject(name);
		return am;
	}
}
