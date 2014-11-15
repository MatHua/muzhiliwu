package com.muzhiliwu.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Log4JListener implements ServletContextListener {

	public static final String log4jkey = "log4j";

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		System.getProperties().remove(log4jkey);

	}

	public void contextInitialized(ServletContextEvent servletcontextevent) {
		String log4j = servletcontextevent.getServletContext().getRealPath("/");
		// System.out.println("log4j:"+log4j);
		System.setProperty(log4jkey, log4j);
	}
}