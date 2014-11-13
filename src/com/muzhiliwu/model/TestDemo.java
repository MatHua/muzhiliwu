package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Table;

//测试
@Table("t_test_demo")
public class TestDemo extends IdEntity {
	@Column
	private String name;
	@Column
	private String email;

	@ManyMany(target = TestDemo.class, relation = "t_midd", from = "testId", to = "demoId")
	private List<TestDemo> myDemos;

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

	public List<TestDemo> getMyDemos() {
		return myDemos;
	}

	public void setMyDemos(List<TestDemo> myDemos) {
		this.myDemos = myDemos;
	}

}
