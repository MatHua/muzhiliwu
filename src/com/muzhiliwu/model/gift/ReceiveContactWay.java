package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

@Table("t_receive_contact_way")
public class ReceiveContactWay extends IdEntity {
	@Column
	private String creatorId;// 创建者id
	@One(target = User.class, field = "creatorId")
	private User creator;// 创建者
	@Column
	private String receive_name;// 收货人姓名
	@Column
	@ColDefine(type = ColType.TEXT)
	private String receive_address;// 收货人地址
	@Column
	private String receive_zip;// 收货人邮编
	@Column
	private String receive_phone;// 收货人电话
	@Column
	private String receive_mobile;// 收货人手机

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getReceive_name() {
		return receive_name;
	}

	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}

	public String getReceive_address() {
		return receive_address;
	}

	public void setReceive_address(String receive_address) {
		this.receive_address = receive_address;
	}

	public String getReceive_zip() {
		return receive_zip;
	}

	public void setReceive_zip(String receive_zip) {
		this.receive_zip = receive_zip;
	}

	public String getReceive_phone() {
		return receive_phone;
	}

	public void setReceive_phone(String receive_phone) {
		this.receive_phone = receive_phone;
	}

	public String getReceive_mobile() {
		return receive_mobile;
	}

	public void setReceive_mobile(String receive_mobile) {
		this.receive_mobile = receive_mobile;
	}

}
