package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

//商品款式表
@Table("t_gift_style")
@TableIndexes({ @Index(name = "idx_gift_style", fields = { "giftId" }, unique = false) })
public class GiftStyle extends IdEntity {
	@Column
	private String giftId;// 联结id

	@Column
	private String name;// 商品款式名

	@Column
	private float price;// 单价

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
