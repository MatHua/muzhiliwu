package com.muzhiliwu.utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.view.AbstractPathView;

public class LogView extends AbstractPathView {

	public LogView(String dest) {
		super(dest != null ? dest.replace('\\', '/') : null);
	}

	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Exception {
		String path = evalPath(req, obj);
		String args = "";
		if (path != null && path.contains("?")) {
			args = path.substring(path.indexOf('?'));
			path = path.substring(0, path.indexOf('?'));
		}
		String ext = getExt();
		if (Strings.isBlank(path)) {
			path = Mvcs.getRequestPath(req);
			path = (new StringBuilder("/WEB-INF"))
					.append(path.startsWith("/") ? "" : "/")
					.append(Files.renameSuffix(path, ext)).toString();
		} else if (path.charAt(0) == '/') {
			if (!path.toLowerCase().endsWith(ext))
				path = (new StringBuilder(String.valueOf(path))).append(ext)
						.toString();
		} else {
			path = (new StringBuilder("/WEB-INF/")).append("logs/")
					.append(path).append(ext).toString();
		}
		path = (new StringBuilder(String.valueOf(path))).append(args)
				.toString();
		RequestDispatcher rd = req.getRequestDispatcher(path);
		if (rd == null) {
			throw Lang.makeThrow("Fail to find Forward '%s'",
					new Object[] { path });
		} else {
			rd.forward(req, resp);
			return;
		}
	}

	protected String getExt() {
		return "";
	}
}
