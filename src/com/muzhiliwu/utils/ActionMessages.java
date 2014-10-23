package com.muzhiliwu.utils;

public class ActionMessages {
	public static final String fail = "0";
	public static final String success = "1";
	public static final String AUTH_CODE_FAIL = "2";// 验证码错误
	public static final String ACCOUNT_FAIL = "3";// 用户名或密码错误
	public static final String NOT_LOGIN = "4";// 没有登陆

	private String type;
	private String message;
	private Object object;
	private int pageSize;
	private int pageNum;
	private int pageCount;

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

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
