package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_mess_comment")
public class MessComment extends IdEntity {
	@Column
	private String messId;// 留言id,用于联结"t_message"表的messageId(id)
	@Column
	private String commenterId;// 评论者id,用于联结"t_user"表的userId(id)
	
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评论内容

	@One(target = User.class, field = "id")
	private User commenter;// 便于记录评论者信息
	@One(target = MessComment.class, field = "id")
	private MessComment father;// 父级评论id,便于找到该评论的父级评论

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

	public MessComment getFather() {
		return father;
	}

	public void setFather(MessComment father) {
		this.father = father;
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

}
