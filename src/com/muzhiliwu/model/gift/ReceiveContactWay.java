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
	private String fullName;// 收货人姓名

	@Column
	private String townName;// 镇
	@Column
	private String areaName;// 区
	@Column
	private String cityName;// 城市
	@Column
	private String provinceName;// 省
	@Column
	private boolean defaultAddress;// 是否是默认地址

	@Column
	@ColDefine(type = ColType.TEXT)
	private String addressDetail;// 收货人地址
	@Column
	@ColDefine(type = ColType.TEXT)
	private String remarks;// 备注

	@Column
	private String mobile;// 收货人手机
	@Column
	private String postCode;// 邮政编码

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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public boolean isDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(boolean defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
}
