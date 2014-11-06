package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

@Table("t_order_form_for_wish")
public class OrderFormForWish extends IdEntity {
	@Column
	private String wisherId;// 许愿者id
	@One(target = User.class, field = "wisherId")
	private User wisher;// 许愿者

	@Column
	private String giftId;// 联结礼物
	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 许愿者选择的礼品

	@Column
	private String styleId;// 用于联结款式~
	@One(target = GiftStyle.class, field = "styleId")
	private GiftStyle style;// 许愿者选择的款式

	@Column
	private String sizeId;// 用于联结尺寸
	@One(target = GiftSize.class, field = "sizeId")
	private GiftSize size;// 许愿者选择的尺寸

	@Column
	private int number;// 许愿者选择的商品数量
	private float totalPrice;// 总价

	public String getWisherId() {
		return wisherId;
	}

	public void setWisherId(String wisherId) {
		this.wisherId = wisherId;
	}

	public User getWisher() {
		return wisher;
	}

	public void setWisher(User wisher) {
		this.wisher = wisher;
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

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public GiftStyle getStyle() {
		return style;
	}

	public void setStyle(GiftStyle style) {
		this.style = style;
	}

	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	public GiftSize getSize() {
		return size;
	}

	public void setSize(GiftSize size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

}
