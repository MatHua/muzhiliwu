package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

//商品的一些具体参数
@Table("t_gift_param")
public class GiftParam extends IdEntity {
	@Column
	private String giftId;// 商品id
	@Column
	private String name;// 参数名
	@Column
	private String value;// 参数值

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
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

}
