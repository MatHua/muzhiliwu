package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

//保存商家的表
@Table("t_shop")
@TableIndexes({ @Index(name = "idx_shop", fields = { "code" }, unique = false) })
public class Shop extends IdEntity {
	public static final String EntityShop = "线下加盟商家";// 实体店
	public static final String OnlineStore = "线上加盟商家";// 网店

	public static final String ShopOpen = "open";// 营业状态
	public static final String ShopRest = "rest";// 店主休息

	public static final String CanBusiness = "can_business";// 可营业状态
	public static final String BanBusiness = "ban_business";// 被超级管理员禁止营业

	// ************************店铺信息************************
	@Column
	private String logo;// 商家头像路径
	@Column
	private String shopName;// 店名
	@Column
	private String shopType;// 店类型
	@Column
	private String url;// 网址
	@Column
	private String alipayCode;// 支付宝账号(用于收款)
	@Column
	@ColDefine(type = ColType.TEXT)
	private String shopIntroduct;// 店家简介

	// ****************个人账号******************************
	@Column
	private String code;// 商店账号
	@Column
	private String pass;// 密码
	@Column
	private String shopBoss;// 店主(负责人)
	@Column
	private String mobile;// 联系方式
	@Column
	private String email;// 邮箱
	@Column
	private String IDCard;// 身份证号

	// *********商家logo(已有)
	private int unreadReplyNum;// 未读信息数 ------
	private List<ShopUnreadReply> shopUnreadReplies;
	// *********店家名称----如:礼品汇(已有)
	// *********店家类型----如:线上加盟商户(已有)
	private int monthVisitNum;// 月访问量
	private int monthVisitRank;// 月访问排行

	@Column
	private int tolVisitNum;// 店家总访问量

	@Column
	@ColDefine(type = ColType.TEXT)
	private String address;// 地址
	@Column
	@ColDefine(type = ColType.TEXT)
	private String shopStory;// 品牌故事
	@Column
	private int salesNumber;// 销售数量
	@Column
	private String businessState;// 营业状态
	@Column
	private String okBusiness;// 超级管理员控制该店是否可营业

	private int giftNum;// 商品数量
	@Many(target = Gift.class, field = "shopId")
	private List<Gift> gifts;// 商品

	private int unpayOrderNum;// 未完成订单数
	@Many(target = OrderForm.class, field = "shopId")
	private List<OrderForm> unpayOrders;// 未完成订单数

	private int historyOrderNum;// 历史订单
	@Many(target = OrderForm.class, field = "shopId")
	private List<OrderForm> historyOrders;// 历史订单数

	public int getUnreadReplyNum() {
		return unreadReplyNum;
	}

	public void setUnreadReplyNum(int unreadReplyNum) {
		this.unreadReplyNum = unreadReplyNum;
	}

	public List<ShopUnreadReply> getShopUnreadReplies() {
		return shopUnreadReplies;
	}

	public void setShopUnreadReplies(List<ShopUnreadReply> shopUnreadReplies) {
		this.shopUnreadReplies = shopUnreadReplies;
	}

	public int getMonthVisitNum() {
		return monthVisitNum;
	}

	public void setMonthVisitNum(int monthVisitNum) {
		this.monthVisitNum = monthVisitNum;
	}

	public int getMonthVisitRank() {
		return monthVisitRank;
	}

	public void setMonthVisitRank(int monthVisitRank) {
		this.monthVisitRank = monthVisitRank;
	}

	public int getTolVisitNum() {
		return tolVisitNum;
	}

	public void setTolVisitNum(int tolVisitNum) {
		this.tolVisitNum = tolVisitNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShopIntroduct() {
		return shopIntroduct;
	}

	public void setShopIntroduct(String shopIntroduct) {
		this.shopIntroduct = shopIntroduct;
	}

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

	public int getSalesNumber() {
		return salesNumber;
	}

	public void setSalesNumber(int salesNumber) {
		this.salesNumber = salesNumber;
	}

	public String getBusinessState() {
		return businessState;
	}

	public void setBusinessState(String businessState) {
		this.businessState = businessState;
	}

	public String getOkBusiness() {
		return okBusiness;
	}

	public void setOkBusiness(String okBusiness) {
		this.okBusiness = okBusiness;
	}

	public int getGiftNum() {
		return giftNum;
	}

	public void setGiftNum(int giftNum) {
		this.giftNum = giftNum;
	}

	public String getAlipayCode() {
		return alipayCode;
	}

	public void setAlipayCode(String alipayCode) {
		this.alipayCode = alipayCode;
	}

	public String getShopStory() {
		return shopStory;
	}

	public void setShopStory(String shopStory) {
		this.shopStory = shopStory;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getUnpayOrderNum() {
		return unpayOrderNum;
	}

	public void setUnpayOrderNum(int unpayOrderNum) {
		this.unpayOrderNum = unpayOrderNum;
	}

	public List<OrderForm> getUnpayOrders() {
		return unpayOrders;
	}

	public void setUnpayOrders(List<OrderForm> unpayOrders) {
		this.unpayOrders = unpayOrders;
	}

	public int getHistoryOrderNum() {
		return historyOrderNum;
	}

	public void setHistoryOrderNum(int historyOrderNum) {
		this.historyOrderNum = historyOrderNum;
	}

	public List<OrderForm> getHistoryOrders() {
		return historyOrders;
	}

	public void setHistoryOrders(List<OrderForm> historyOrders) {
		this.historyOrders = historyOrders;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
