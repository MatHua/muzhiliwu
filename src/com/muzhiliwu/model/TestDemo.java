package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.gift.Gift;

//测试
@Table("t_test_demo")
public class TestDemo extends IdEntity {
	@Column
	private String giftId;
	@One(target = Gift.class, field = "giftId")
	private Gift tmpGift;
	
	@Column
	private String name;
	@Column
	private String email;

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
}
