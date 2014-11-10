package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;

//许愿商品订单
@Table("t_order_form_for_wish")
@TableIndexes({ @Index(name = "idx_order_form_for_wish", fields = {
		"creatorId", "wishId" }, unique = false) })
public class OrderFormForWish {
	@Name
	@Column
	private String orderId;// 订单号

	@Column
	private String date;// 订单生成时间

	@Column
	private String cartId;// 购物车id

	@Column
	private String creatorId;// 创建者id
	@One(target = User.class, field = "creatorId")
	private User creator;// 订单创建者~即许愿者

	@Column
	private String wishId;// 许愿单id
	@One(target = Wish.class, field = "wishId")
	private Wish wish;// 许愿单

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
	private String contactWayId;// 联结联系方式的表
	@One(target = ReceiveContactWay.class, field = "contactWayId")
	private ReceiveContactWay contactWay;// 保存联系方式

	@Column
	private int number;// 许愿者选择的商品数量
	@Column
	private float totalPrice;// 总价

	@Column
	private String state;// 订单状态

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Wish getWish() {
		return wish;
	}

	public void setWish(Wish wish) {
		this.wish = wish;
	}

	public String getContactWayId() {
		return contactWayId;
	}

	public void setContactWayId(String contactWayId) {
		this.contactWayId = contactWayId;
	}

	public ReceiveContactWay getContactWay() {
		return contactWay;
	}

	public void setContactWay(ReceiveContactWay contactWay) {
		this.contactWay = contactWay;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
