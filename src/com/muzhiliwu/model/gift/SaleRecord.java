package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.User;

//成交记录表
@Table("t_sale_record")
@TableIndexes({ @Index(name = "idx_sale_record", fields = { "giftId" }, unique = false) })
public class SaleRecord extends IdEntity {
	@Column
	private String giftId;// 用于联结
	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 成功被购买的礼品

	@Column
	private String buyerId;// 购买者id
	@One(target = User.class, field = "buyerId")
	private User buyer;// 买家

	@Column
	private String style;// 买家选择的款式
	@Column
	private String size;// 买家选择的尺寸(型号)
	@Column
	private int number;// 买家购买的数量

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
