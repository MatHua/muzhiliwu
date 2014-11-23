package com.muzhiliwu.model;

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

import com.muzhiliwu.model.gift.OrderFormForWish;

@Table("t_wish")
@TableIndexes({ @Index(name = "idx_wish", fields = { "wisherId" }, unique = false) })
public class Wish extends IdEntity {
	// state
	public static final String Unrealized = "unrealized";// 为实现愿望
	public static final String Realized = "realized";// 已实现愿望

	// type
	public static final String Wish_Gift = "wish_gift";// 许愿要礼物
	public static final String Wish_Common = "wish_common";// 许愿非要礼物

	@Column
	private String wisherId;// 许愿者id
	@Column
	private String type;// 许愿类型
	@Column
	private String style;// 许愿类别
	@Column
	private String sex;// 许愿者性别
	@Column
	private String star;// 许愿者星座

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 许愿内容
	// @Column
	// private String title;// 标题
	@Column
	private String state;// 许愿状态

	private int praiseNum;// 点赞数
	@Many(target = WishPraise.class, field = "wishId")
	private List<WishPraise> praises;// 便于记录点赞记录

	private int shareNum;// 分享数
	@Many(target = WishShare.class, field = "wishId")
	private List<WishShare> shares;// 便于记录愿望的收集者

	@One(target = User.class, field = "wisherId")
	private User wisher;// 用于记录许愿者

	private boolean isShared;// 标记是否已被收藏
	private boolean isPraised;// 标记是否已点赞(喜欢)

	@Column
	private String wishOrderFormId;// 联结id
	@One(target = OrderFormForWish.class, field = "wishOrderFormId")
	private OrderFormForWish wishOrderForm;// 对应的许愿订单

	private int wantorNum;// 愿望实现的申请者数
	@ManyMany(target = User.class, relation = "t_wish_realization_of_wantor", from = "wishId", to = "wantorId")
	private List<User> wishWantors;// 想要帮忙实现愿望的人

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getWisherId() {
		return wisherId;
	}

	public void setWisherId(String wisherId) {
		this.wisherId = wisherId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<WishPraise> getPraises() {
		return praises;
	}

	public void setPraises(List<WishPraise> praises) {
		this.praises = praises;
	}

	public User getWisher() {
		return wisher;
	}

	public void setWisher(User wisher) {
		this.wisher = wisher;
	}

	// public String getTitle() {
	// return title;
	// }
	//
	// public void setTitle(String title) {
	// this.title = title;
	// }

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public String getWishOrderFormId() {
		return wishOrderFormId;
	}

	public void setWishOrderFormId(String wishOrderFormId) {
		this.wishOrderFormId = wishOrderFormId;
	}

	public OrderFormForWish getWishOrderForm() {
		return wishOrderForm;
	}

	public void setWishOrderForm(OrderFormForWish wishOrderForm) {
		this.wishOrderForm = wishOrderForm;
	}

	public boolean isPraised() {
		return isPraised;
	}

	public void setPraised(boolean isPraised) {
		this.isPraised = isPraised;
	}

	public List<WishShare> getShares() {
		return shares;
	}

	public void setShares(List<WishShare> shares) {
		this.shares = shares;
	}

	public boolean isShared() {
		return isShared;
	}

	public void setShared(boolean isShared) {
		this.isShared = isShared;
	}

	public int getWantorNum() {
		return wantorNum;
	}

	public void setWantorNum(int wantorNum) {
		this.wantorNum = wantorNum;
	}

	public List<User> getWishWantors() {
		return wishWantors;
	}

	public void setWishWantors(List<User> wishWantors) {
		this.wishWantors = wishWantors;
	}

}
