package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("t_my_msg")
public class MyMsg extends IdEntity {
	@Column
	private String myTestId;
	@Column
	private String name;
	@Column
	private String value;

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

	public String getMyTestId() {
		return myTestId;
	}

	public void setMyTestId(String myTestId) {
		this.myTestId = myTestId;
	}

}
