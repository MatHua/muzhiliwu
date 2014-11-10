package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.User;

//用户地址信息表
@Table("t_receive_contact_way")
@TableIndexes({ @Index(name = "idx_receive_contact_way", fields = { "creatorId" }, unique = false) })
public class ReceiveContactWay extends IdEntity {
	@Column
	private String creatorId;// 创建者id
	@One(target = User.class, field = "creatorId")
	private User creator;// 创建者

	@Column
	private String name;// 收货人姓名

	@Column
	@ColDefine(type = ColType.TEXT)
	private String address;// 收货人地址

	@Column
	private String mobile;// 收货人手机

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
