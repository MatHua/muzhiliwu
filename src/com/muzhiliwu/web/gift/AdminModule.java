package com.muzhiliwu.web.gift;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.view.ViewWrapper;

import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.LogFileFilter;
import com.muzhiliwu.utils.LogView;

@At("/admin")
public class AdminModule {
	@At
	@Ok("json")
	public Object logList(ServletContext context) {
		String path = context.getRealPath("/") + "/WEB-INF/logs/";
		String regx = ".log";
		File f2 = new File(path);
		File[] s = f2.listFiles(new LogFileFilter(regx));
		ActionMessage am = new ActionMessage();
		List<String> list = new ArrayList<String>();
		for (File file : s) {
			list.add(file.getName());
		}
		am.setObject(list);
		am.setMessage("获取日志文件目录成功~");
		am.setType(ActionMessage.success);
		return am;
		// return new ViewWrapper(new LogView("logs.muzhiliwu_error"), null);
	}

	@At
	public ViewWrapper lookLog(String file) {
		return new ViewWrapper(new LogView(file), null);
	}
}
