package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.User;

//购物车~
@Table("t_shopping_cart")
@TableIndexes({ @Index(name = "idx_shopping_cart", fields = { "buyerId" }, unique = false) })
public class ShoppingCart extends IdEntity {

	@Column
	private String buyerId;// 购买者id
	@One(target = User.class, field = "buyerId")
	private User buyer;// 购买者

	@Column
	private String contactWayId;// 联结联系方式的表
	@One(target = ReceiveContactWay.class, field = "contactWayId")
	private ReceiveContactWay contactWay;// 保存联系方式

	private int orderNum;// 订单数
	@Many(target = OrderForm.class, field = "cartId")
	private List<OrderForm> orders;// 放进购物车的订单

	@Column
	private float totalPrice;// 总价

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

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public List<OrderForm> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderForm> orders) {
		this.orders = orders;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

}
