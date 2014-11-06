package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

//商品的一些具体参数,用户可以任意创建
@Table("t_gift_param")
public class GiftParam extends IdEntity {
	@Column
	private String giftId;// 商品id
	@Column
	private String param_name;// 参数名
	@Column
	private String param_value;// 参数值

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

}
