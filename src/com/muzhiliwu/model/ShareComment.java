package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_share_commeny")
public class ShareComment {
	@Column
	private String shareId;// 分享id,用于联结"t_share"表的shareId(id)
	@Column
	private String commenterId;// 评论者id,用于联结"t_user"表的userId(id)
	
	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	@Column
	@ColDefine(type = ColType.TEXT)
	private String conteng;// 评论内容

	@One(target = User.class, field = "id")
	private User commenter;// 便于记录评论者
	@One(target = ShareComment.class, field = "id")
	private ShareComment father;// 父级评论id,便于找到该评论的父级评论

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public String getConteng() {
		return conteng;
	}

	public void setConteng(String conteng) {
		this.conteng = conteng;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public ShareComment getFather() {
		return father;
	}

	public void setFather(ShareComment father) {
		this.father = father;
	}

}
