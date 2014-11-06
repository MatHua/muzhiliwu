package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_comment")
public class GiftCollectComment extends IdEntity {
	@Column
	private String collectId;// 被评论礼物收藏的id

	@Column
	private String commenterId;// 评论者id
	@One(target = User.class, field = "commenterId")
	private User commenter;// 评论者

	@Column
	private String fatherCommenterId;// 父评论的id
	@One(target = User.class, field = "fatherCommenterId")
	private User fatherCommenter;// 父级评论者

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评论内容

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public String getFatherCommenterId() {
		return fatherCommenterId;
	}

	public void setFatherCommenterId(String fatherCommenterId) {
		this.fatherCommenterId = fatherCommenterId;
	}

	public User getFatherCommenter() {
		return fatherCommenter;
	}

	public void setFatherCommenter(User fatherCommenter) {
		this.fatherCommenter = fatherCommenter;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}