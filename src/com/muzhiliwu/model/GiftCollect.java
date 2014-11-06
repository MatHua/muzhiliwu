package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.gift.Gift;

@Table("t_gift_collect")
public class GiftCollect extends IdEntity {
	@Column
	private String collectorId;// 收藏者id
	@One(target = User.class, field = "collectorId")
	private User collector;// 收藏者
	@Column
	private String giftId;// 被收藏的礼品的id
	@One(target = Gift.class, field = "giftId")
	private Gift collect;// 被收藏的礼物

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

	public Gift getCollect() {
		return collect;
	}

	public void setCollect(Gift collect) {
		this.collect = collect;
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

}