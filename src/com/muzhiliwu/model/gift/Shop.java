package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

@Table("t_shop")
public class Shop extends IdEntity {
	public static final String Entity_Shop = "entity_shop";// 实体店
	public static final String Online_Store = "online_store";// 网店

	@Column
	private String shopName;// 店名
	@Column
	private String shopType;// 店类型
	@Column
	private String shopBoss;// 店主(负责人)
	@Column
	private String mobile;// 手机
	@Column
	private String email;// 邮箱
	@Column
	@ColDefine(type = ColType.TEXT)
	private String address;// 地址
	@Column
	@ColDefine(type = ColType.TEXT)
	private String ShopStory;// 品牌故事
	@Column
	private int goods_number;// 商品数量

	@Many(target = Gift.class, field = "shopId")
	private List<Gift> gifts;// 商品

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShopStory() {
		return ShopStory;
	}

	public void setShopStory(String shopStory) {
		ShopStory = shopStory;
	}

	public int getGoods_number() {
		return goods_number;
	}

	public void setGoods_number(int goods_number) {
		this.goods_number = goods_number;
	}

	public List<Gift> getGifts() {
		return gifts;
	}

	public void setGifts(List<Gift> gifts) {
		this.gifts = gifts;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getShopBoss() {
		return shopBoss;
	}

	public void setShopBoss(String shopBoss) {
		this.shopBoss = shopBoss;
	}

}
