package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.gift.OrderFormForWish;

@Table("t_wish")
public class Wish extends IdEntity {
	// state
	public static final String Unrealized = "unrealized";// 为实现愿望
	public static final String Realized = "realized";// 已实现愿望

	// type
	public static final String Wish_Gift = "wish_gift";// 许愿要礼物
	public static final String WIsh_Other = "wish_other";// 许愿非要礼物

	@Column
	private String wisherId;// 许愿者id
	@Column
	private String type;// 许愿类型
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 许愿内容
	@Column
	private String title;// 标题
	@Column
	private String state;// 许愿状态

	@Column
	private int praiseNum;// 点赞数
	@Many(target = WishPraise.class, field = "wishId")
	private List<WishPraise> praises;// 便于记录点赞记录

	@Column
	private int collectNum;// 收藏数
	@Many(target = WishCollect.class, field = "wishId")
	private List<WishCollect> collectes;// 便于记录愿望的收集者

	@Column
	private int shareNum;// 分享数

	@One(target = User.class, field = "wisherId")
	private User wisher;// 用于记录许愿者

	private boolean collected;// 标记是否已被收藏

	@Column
	private String wishOrderFormId;// 联结id
	@One(target = OrderFormForWish.class, field = "wishOrderFormId")
	private OrderFormForWish wishOrderForm;// 对应的许愿订单

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

	public List<WishCollect> getCollectes() {
		return collectes;
	}

	public void setCollectes(List<WishCollect> collectes) {
		this.collectes = collectes;
	}

	public User getWisher() {
		return wisher;
	}

	public void setWisher(User wisher) {
		this.wisher = wisher;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
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

}
