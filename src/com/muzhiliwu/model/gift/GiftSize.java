package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

//商品尺寸表
@Table("t_gift_size")
@TableIndexes({ @Index(name = "idx_gift_size", fields = { "giftId" }, unique = false) })
public class GiftSize extends IdEntity {
	@Column
	private String giftId;// 商品id
	@Column
	private String size;// 商品尺寸

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}