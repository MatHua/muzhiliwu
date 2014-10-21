package com.muzhiliwu.utils;

public class ActionMessage {
	public static final String success = "1";
	public static final String fail = "0";
	public static final String AUTH_CODE_FAIL = "3";// 验证码错误
	public static final String ACCOUNT_FAIL = "4";// 用户名或密码错误
	public static final String NOT_LOGIN = "5";// 没有登陆

	private String type;
	private String message;
	private Object object;

	public ActionMessage() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
