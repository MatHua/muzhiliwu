package com.muzhiliwu.utils;

public class ActionMessage {
	public static final String success = "1";
	public static final String fail = "0";
	public static final String cancel = "2";// 取消
	public static final String Not_MuzhiCoin = "3";// 不够积分
	public static final String Auth_Code_Fail = "4";// 验证码错误
	public static final String Account_Fail = "5";// 用户名或密码错误
	public static final String Not_Login = "6";// 没有登陆

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
