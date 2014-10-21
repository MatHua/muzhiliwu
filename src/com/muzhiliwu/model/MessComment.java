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

@Table("t_mess_comment")
// @TableIndexes(value = { @Index(fields = { "messCommentId" }, unique = false,
// name = "otherMessComment") })
public class MessComment extends IdEntity {
	@Column
	private String messId;// 留言id,用于联结"t_message"表的messageId(id)

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评论内容

	@Column
	private String commenterId;// 评论者id,用于联结"t_user"表的userId(id)
	@One(target = User.class, field = "commenterId")
	private User commenter;// 便于记录评论者信息

	@Column
	private String fatherCommenterId;
	@One(target = User.class, field = "fatherCommenterId")
	private User fatherCommenter;// 父评论者id,便于找到该评论的父级评论者

	public String getMessId() {
		return messId;
	}

	public void setMessId(String messId) {
		this.messId = messId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
