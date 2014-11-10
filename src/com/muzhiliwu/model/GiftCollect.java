package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.gift.Gift;

@Table("t_gift_collect")
@TableIndexes({ @Index(name = "idx_gift_collect", fields = { "collectorId" }, unique = false) })
public class GiftCollect extends IdEntity {
	@Column
	private String collectorId;// 收藏者id
	@One(target = User.class, field = "collectorId")
	private User collector;// 收藏者

	@Column
	private String title;// 采集标题

	@Column
	@ColDefine(type = ColType.TEXT)
	private String descript;// 采集描述

	@Column
	private String giftId;// 被收藏的礼品的id
	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 被收藏的礼物

	@Column
	private int commentNum;// 评论数
	@Many(target = GiftCollectComment.class, field = "collectId")
	private List<GiftCollectComment> comments;// 评论

	@Column
	private int praiseNum;// 点赞数
	@Many(target = GiftCollectPraise.class, field = "collectId")
	private List<GiftCollectPraise> praises;// 点赞

	public String getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(String collectorId) {
		this.collectorId = collectorId;
	}

	public User getCollector() {
		return collector;
	}

	public void setCollector(User collector) {
		this.collector = collector;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public List<GiftCollectComment> getComments() {
		return comments;
	}

	public void setComments(List<GiftCollectComment> comments) {
		this.comments = comments;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public List<GiftCollectPraise> getPraises() {
		return praises;
	}

	public void setPraises(List<GiftCollectPraise> praises) {
		this.praises = praises;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

}