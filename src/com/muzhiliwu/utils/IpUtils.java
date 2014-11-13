package com.muzhiliwu.utils;

import javax.servlet.http.HttpServletRequest;

//查找客户端ip地址
public class IpUtils {
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;

		// if (request.getHeader("x-forwarded-for") == null) {
		// return request.getRemoteAddr();
		// }
		// return request.getHeader("x-forwarded-for");
	}
}
