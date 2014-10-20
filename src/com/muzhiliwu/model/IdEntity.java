package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;

public abstract class IdEntity {
	@Name
	@Column
	public String id;
	@Column
	protected String date;// 时间(注册时间,发表时间,回复时间)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
