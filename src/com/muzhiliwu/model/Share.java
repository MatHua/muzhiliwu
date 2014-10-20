package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_share")
public class Share extends IdEntity {
	@Column
	private String sharerId;// 分享者id
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 分享内容

	@Many(target = ShareComment.class, field = "shareId")
	private List<ShareComment> comments;// 该分享的所有评论
	@Many(target = SharePraise.class, field = "shareId")
	private List<SharePraise> praisers;// 便于找到该分享的所有点赞者
	@Many(target = ShareCollect.class, field = "shareId")
	private List<ShareCollect> collectes;// 便于记录分享的收藏者

	@Column
	private String userId;
	@One(target = User.class, field = "userId")
	private User sharer;// 用于记录该分享的发布者

	public String getSharerId() {
		return sharerId;
	}

	public void setSharerId(String sharerId) {
		this.sharerId = sharerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ShareComment> getComments() {
		return comments;
	}

	public void setComments(List<ShareComment> comments) {
		this.comments = comments;
	}

	public List<SharePraise> getPraisers() {
		return praisers;
	}

	public void setPraisers(List<SharePraise> praisers) {
		this.praisers = praisers;
	}

	public List<ShareCollect> getCollectes() {
		return collectes;
	}

	public void setCollectes(List<ShareCollect> collectes) {
		this.collectes = collectes;
	}

	public User getSharer() {
		return sharer;
	}

	public void setSharer(User sharer) {
		this.sharer = sharer;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
