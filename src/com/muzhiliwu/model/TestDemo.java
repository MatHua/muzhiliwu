package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

@Table("t_test_demo")
public class TestDemo extends IdEntity {
	@Column
	private String name;
	@Column
	private String email;

	@Many(target = MyTest.class, field = "testId")
	private List<MyTest> myTests;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<MyTest> getMyTests() {
		return myTests;
	}

	public void setMyTests(List<MyTest> myTests) {
		this.myTests = myTests;
	}
}
