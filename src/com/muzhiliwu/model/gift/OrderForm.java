package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.User;

//订单信息表
@Table("t_order_form")
@TableIndexes({ @Index(name = "idx_order_form", fields = { "buyerId" }, unique = false) })
public class OrderForm {
	public static final String WaitBuyerPay = "WAIT_BUYER_PAY";// 等待付款
	public static final String WaitSellerSendGoods = "WAIT_SELLER_SEND_GOODS";// 等待卖家发货
	public static final String WaitBuyerConfirmGoods = "WAIT_BUYER_CONFIRM_GOODS";// 等待卖家确认收货
	public static final String TradeFinished = "TRADE_FINISHED";// 交易完成

	@Name
	@Column
	private String orderId;// 订单号
	@Column
	private String date;// 订单成交时间

	@Column
	private String cartId;// 联结字段

	@Column
	private String shopId;// 该订单所属卖家的id
	@One(target = Shop.class, field = "shopId")
	private Shop shop;// 该订单所属卖家的id

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
	private String payState;// 订单状态

	@Column
	private String payType;// 付款类型(网银,支付宝,财付通)等~网银还分各种银行...
	@Column
	private String payCode;// 支付的账号...

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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

}
