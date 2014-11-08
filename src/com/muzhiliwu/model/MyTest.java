package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

@Table("t_my_test")
public class MyTest extends IdEntity {
	@Column
	private String testId;
	@Column
	private String name;
	@Column
	private String value;

	@Many(target = MyMsg.class, field = "myTestId")
	private List<MyMsg> msgs;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<MyMsg> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<MyMsg> msgs) {
		this.msgs = msgs;
	}

}
