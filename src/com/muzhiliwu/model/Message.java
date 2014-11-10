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

@Table("t_mess")
@TableIndexes({ @Index(name = "idx_mess", fields = { "publisherId" }, unique = false) })
public class Message extends IdEntity {// 留言墙
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 留言内容
	@Column
	private String type;// 留言类型
	@Column
	private String title;// 标题

	@Column
	private String publisherId;// 发表者id,用于联结"t_user"的userId(id)
	@One(target = User.class, field = "publisherId")
	private User publisher;// 便于记录留言的 发表者

	@Column
	private int praiseNum;// 点赞数
	@Many(target = MessPraise.class, field = "messId")
	private List<MessPraise> praises;// 便于记录点赞记录

	@Column
	private int commentNum;// 评论数
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

	public List<MessPraise> getPraises() {
		return praises;
	}

	public void setPraises(List<MessPraise> praises) {
		this.praises = praises;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

}
