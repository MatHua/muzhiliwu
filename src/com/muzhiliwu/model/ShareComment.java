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
	private String shareCommentId;
	@One(target = ShareComment.class, field = "shareCommentId")
	private ShareComment father;// 父级评论id,便于找到该评论的父级评论

	@Column
	private String fatherCommentId;
	@Many(target = ShareComment.class, field = "fatherCommentId")
	private List<ShareComment> sons;// 所有子评论

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

	public ShareComment getFather() {
		return father;
	}

	public void setFather(ShareComment father) {
		this.father = father;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public String getShareCommentId() {
		return shareCommentId;
	}

	public void setShareCommentId(String shareCommentId) {
		this.shareCommentId = shareCommentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFatherCommentId() {
		return fatherCommentId;
	}

	public void setFatherCommentId(String fatherCommentId) {
		this.fatherCommentId = fatherCommentId;
	}

	public List<ShareComment> getSons() {
		return sons;
	}

	public void setSons(List<ShareComment> sons) {
		this.sons = sons;
	}

}
