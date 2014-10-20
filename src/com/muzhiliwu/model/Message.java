package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_mess")
public class Message extends IdEntity {// 留言墙
	@Column
	private String publisherId;// 发表者id,用于联结"t_user"的userId(id)
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 留言内容
	@Column
	private String type;// 留言类型

	@One(target = User.class, field = "id")
	private User publisher;// 便于记录留言的 发表者
	@Many(target = MessPraise.class, field = "messId")
	private List<MessPraise> praisers;// 便于记录点赞记录
	@Many(target = MessComment.class, field = "messId")
	private List<MessComment> comments;// 留言评论,便于找到留言对应的所有评论

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MessComment> getComments() {
		return comments;
	}

	public void setComments(List<MessComment> comments) {
		this.comments = comments;
	}

	public List<MessPraise> getPraisers() {
		return praisers;
	}

	public void setPraisers(List<MessPraise> praisers) {
		this.praisers = praisers;
	}

	public User getPublisher() {
		return publisher;
	}

	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

}
