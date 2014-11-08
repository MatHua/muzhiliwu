package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

//订单信息表
@Table("t_order_form")
public class OrderForm {
	@Name
	@Column
	private String orderId;// 订单号
	@Column
	private String date;// 订单成交时间

	@Column
	private String cartId;// 联结字段

	@Column
	private String buyerId;// 创建者id
	@One(target = User.class, field = "buyerId")
	private User buyer;// 购物者

	@Column
	private String giftId;// 礼品id
	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 被选中的礼品

	@Column
	private String styleId;// 款式id
	@One(target = GiftStyle.class, field = "styleId")
	private GiftStyle style;// 选中的款式

	@Column
	private String sizeId;// 尺寸id
	@One(target = GiftSize.class, field = "sizeId")
	private GiftSize size;// 选中的尺寸

	@Column
	private String contactWayId;// 联结联系方式的表
	@One(target = ReceiveContactWay.class, field = "contactWayId")
	private ReceiveContactWay contactWay;// 保存联系方式

	@Column
	private int number;// 选中的商品数量
	@Column
	private float subtotal;// 小计

	@Column
	private String state;// 订单状态

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

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

}
