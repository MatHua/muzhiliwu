package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_share_comment")
// @TableIndexes(value = { @Index(fields = { "shareCommentId" }, unique = false,
// name = "otherShareComment") })//创建索引
public class ShareComment extends IdEntity {
	@Column
	private String shareId;// 分享id,用于联结"t_share"表的shareId(id)
	@Column
	private String commenterId;// 评论者id,用于联结"t_user"表的userId(id)

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评论内容

	@One(target = User.class, field = "commenterId")
	private User commenter;// 便于记录评论者

	@Column
	private String fatherCommenterId;
	@One(target = User.class, field = "fatherCommenterId")
	private User fatherCommenter;// 父级评论者id,便于找到该评论的父级评论者

	private boolean isMeComment;// 标记是否是本人评价的

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public boolean isMeComment() {
		return isMeComment;
	}

	public void setMeComment(boolean isMeComment) {
		this.isMeComment = isMeComment;
	}

}
