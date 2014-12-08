package com.muzhiliwu.model.gift;

import java.text.DecimalFormat;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

//礼品商品表
@Table("t_gift")
@TableIndexes({ @Index(name = "idx_gift", fields = { "shopId" }, unique = false) })
public class Gift extends IdEntity {
	public static final DecimalFormat df = new DecimalFormat("0.0");

	public static final String FromOnlineShop = "线上加盟商户";// 来源网店
	public static final String FromEntityShop = "线下加盟商户";// 来源实体店

	public static final String OnSale = "on_sale";// 在销售
	public static final String NotSale = "not_sale";// 商品下架

	public static final String AuditSuccess = "AuditSuccess";// 审核成功
	public static final String AuditFail = "AuditFail";// 审核失败
	public static final String Auditing = "Auditing";// 审核中

	@Column
	private String bigPic;// 大图
	@Column
	private String samllPic;// 小图

	@Column
	private String name;// 商品名称
	private int tagNum;// 标签数
	@ManyMany(target = Tag.class, relation = "t_gift_tag", from = "giftId", to = "tagId")
	private List<Tag> tags;// 获取礼品被标注的标签
	private String giftTag;

	@Column
	private String url;// 商品购买链接~供网店使用

	@Column
	private Double price;// 商品价格
	@Column
	private Double cost;// 成本价
	@Column
	private String type;// 商品分类
	@Column
	private String suitSex;// 适合性别
	@Column
	private String suitStar;// 适合星座
	@Column
	private String suitAgeGroup;// 适合年龄段

	@Column
	@ColDefine(type = ColType.TEXT)
	private String descript;// 商品描述

	// **********************************************************

	@Column
	private String shopState;// 所在店家状态
	@Column
	private String shopOnBusiness;// 所在店家是否有被超级管理员禁止营业

	@Column
	private String auditState;// 审核状态
	@Column
	@ColDefine(type = ColType.TEXT)
	private String auditMess;// 记录审核失败原因
	@Column
	private boolean isDelete;// 被删除标识
	@Column
	private String saleState;// 销售状态

	@Column
	private String fromType;// 来源~网店、实体店

	@Column
	private int clickNum;// 点击量

	@Column
	private String shopId;// 商家id
	@One(target = Shop.class, field = "shopId")
	private Shop seller;// 商家

	@Column
	private int stock;// 库存量
	@Column
	private String packagePostal;// 包邮

	private int styleNum;// 款式数
	@Many(target = GiftStyle.class, field = "giftId")
	private List<GiftStyle> styles;// 礼物的款式

	private int sizeNum;// 礼品尺寸数
	@Many(target = GiftStyle.class, field = "giftId")
	private List<GiftSize> sizes;// 礼品尺寸

	private int commentNum;// 评价数
	@Many(target = GiftComment.class, field = "giftId")
	private List<GiftComment> comments;// 商品所有评价

	private int paramNum;// 商品规格参数数
	@Many(target = GiftParam.class, field = "giftId")
	private List<GiftParam> params;// 商品所有规格参数

	private int picNum;
	@Many(target = GiftPic.class, field = "giftId")
	private List<GiftPic> pics;

	private boolean isCollected;// 记录是否已被收藏
	private boolean isShared;// 记录是否已被分享
	private boolean isBuyed;// 是否购买过
	private boolean isWished;// 是否用它许愿过

	private int collectNum;// 收藏数
	private int shareNum;// 分享数

	@Column
	private int saleNum;// 销售总量

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public String getShopOnBusiness() {
		return shopOnBusiness;
	}

	public void setShopOnBusiness(String shopOnBusiness) {
		this.shopOnBusiness = shopOnBusiness;
	}

	public String getShopState() {
		return shopState;
	}

	public void setShopState(String shopState) {
		this.shopState = shopState;
	}

	public String getGiftTag() {
		return giftTag;
	}

	public void setGiftTag(String giftTag) {
		this.giftTag = giftTag;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSuitSex() {
		return suitSex;
	}

	public void setSuitSex(String suitSex) {
		this.suitSex = suitSex;
	}

	public String getSuitStar() {
		return suitStar;
	}

	public void setSuitStar(String suitStar) {
		this.suitStar = suitStar;
	}

	public String getSuitAgeGroup() {
		return suitAgeGroup;
	}

	public void setSuitAgeGroup(String suitAgeGroup) {
		this.suitAgeGroup = suitAgeGroup;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Shop getSeller() {
		return seller;
	}

	public void setSeller(Shop seller) {
		this.seller = seller;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<GiftStyle> getStyles() {
		return styles;
	}

	public void setStyles(List<GiftStyle> styles) {
		this.styles = styles;
	}

	public List<GiftSize> getSizes() {
		return sizes;
	}

	public void setSizes(List<GiftSize> sizes) {
		this.sizes = sizes;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GiftComment> getComments() {
		return comments;
	}

	public void setComments(List<GiftComment> comments) {
		this.comments = comments;
	}

	public int getStyleNum() {
		return styleNum;
	}

	public void setStyleNum(int styleNum) {
		this.styleNum = styleNum;
	}

	public int getSizeNum() {
		return sizeNum;
	}

	public void setSizeNum(int sizeNum) {
		this.sizeNum = sizeNum;
	}

	public int getTagNum() {
		return tagNum;
	}

	public void setTagNum(int tagNum) {
		this.tagNum = tagNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public int getParamNum() {
		return paramNum;
	}

	public void setParamNum(int paramNum) {
		this.paramNum = paramNum;
	}

	public List<GiftParam> getParams() {
		return params;
	}

	public void setParams(List<GiftParam> params) {
		this.params = params;
	}

	public String getSaleState() {
		return saleState;
	}

	public void setSaleState(String saleState) {
		this.saleState = saleState;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public boolean isShared() {
		return isShared;
	}

	public void setShared(boolean isShared) {
		this.isShared = isShared;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getAuditMess() {
		return auditMess;
	}

	public void setAuditMess(String auditMess) {
		this.auditMess = auditMess;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public List<GiftPic> getPics() {
		return pics;
	}

	public void setPics(List<GiftPic> pics) {
		this.pics = pics;
	}

	public String getPackagePostal() {
		return packagePostal;
	}

	public void setPackagePostal(String packagePostal) {
		this.packagePostal = packagePostal;
	}

	public boolean isBuyed() {
		return isBuyed;
	}

	public void setBuyed(boolean isBuyed) {
		this.isBuyed = isBuyed;
	}

	public boolean isWished() {
		return isWished;
	}

	public void setWished(boolean isWished) {
		this.isWished = isWished;
	}

	public String getBigPic() {
		return bigPic;
	}

	public void setBigPic(String bigPic) {
		this.bigPic = bigPic;
	}

	public String getSamllPic() {
		return samllPic;
	}

	public void setSamllPic(String samllPic) {
		this.samllPic = samllPic;
	}

	public int getClickNum() {
		return clickNum;
	}

	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}

}
