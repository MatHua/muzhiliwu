package com.muzhiliwu.model.gift;

import java.text.DecimalFormat;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift")
public class Gift extends IdEntity {
	private static final DecimalFormat df = new DecimalFormat("0.00");
	public static final String FromOnlineShop = "from_online_shop";// 来源网店
	public static final String FromEntityShop = "from_entity_shop";// 来源实体店

	@Column
	private String fromType;// 来源~网店、实体店
	@Column
	private String url;// 商品购买链接~供网店使用

	@Column
	private String shopId;// 商家id
	@One(target = Shop.class, field = "shopId")
	private Shop seller;// 商家

	@Column
	private String name;// 商品名称
	@Column
	@ColDefine(type = ColType.TEXT)
	private String body;// 商品描述
	@Column
	private Double price;// 商品价格
	@Column
	private String type;// 商品类型
	@Column
	private long collectNum;// 收藏数
	// @Column
	// private long

	@Many(target = GiftStyle.class, field = "giftId")
	private List<GiftStyle> styles;// 礼物的款式

	@Many(target = GiftStyle.class, field = "giftId")
	private List<GiftSize> sizes;// 礼品款式

	@ManyMany(target = Tag.class, relation = "t_gift_tag", from = "giftId", to = "tagId")
	private List<Tag> tags;// 获取礼品被标注的标签

	@Column
	private int evaluatNum;// 评价数
	@Many(target = GiftComment.class, field = "giftId")
	private List<GiftComment> comments;// 商品所有评价

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

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Shop getSeller() {
		return seller;
	}

	public void setSeller(Shop seller) {
		this.seller = seller;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(long collectNum) {
		this.collectNum = collectNum;
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

	public int getEvaluatNum() {
		return evaluatNum;
	}

	public void setEvaluatNum(int evaluatNum) {
		this.evaluatNum = evaluatNum;
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

}
