package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;

public abstract class IdEntity {
	@Name
	@Column
	public String id;
	@Column
	protected String date;// 时间(创建时间,订单生产时间,商品上传时间)

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
