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

@Table("t_share")
@TableIndexes({ @Index(name = "idx_share", fields = { "sharerId" }, unique = false) })
public class Share extends IdEntity {
	public static final String From_Other = "from_other";// 收藏别人的
	public static final String From_Own = "from_own";// 原创的
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 分享内容
	@Column
	private String title;// 标题
	@Column
	private String type;// 类型,原创/收藏

	@Column
	private String fromerId;// 收藏来源者的id
	@One(target = User.class, field = "fromerId")
	private User fromer;// 收藏来源者

	@Column
	private String sharerId;// 分享者id
	@One(target = User.class, field = "sharerId")
	private User sharer;// 用于记录该分享的发布者

	@Column
	private String collectId;// 记录收藏的分享id
	public boolean collected;// 标记是否被当前用户收藏

	@Column
	private int commentNum;// 评论数
	@Many(target = ShareComment.class, field = "shareId")
	private List<ShareComment> comments;// 该分享的所有评论

	@Column
	private int praiseNum;// 点赞数
	@Many(target = SharePraise.class, field = "shareId")
	private List<SharePraise> praises;// 便于找到该分享的所有点赞者

	@Column
	private int collectNum;// 被收藏数

	// @Many(target = ShareCollect.class, field = "shareId")
	// private List<ShareCollect> collectes;// 便于记录分享的收藏者

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

	// public List<ShareCollect> getCollectes() {
	// return collectes;
	// }
	//
	// public void setCollectes(List<ShareCollect> collectes) {
	// this.collectes = collectes;
	// }

	public User getSharer() {
		return sharer;
	}

	public void setSharer(User sharer) {
		this.sharer = sharer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFromerId() {
		return fromerId;
	}

	public void setFromerId(String fromerId) {
		this.fromerId = fromerId;
	}

	public User getFromer() {
		return fromer;
	}

	public void setFromer(User fromer) {
		this.fromer = fromer;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public List<SharePraise> getPraises() {
		return praises;
	}

	public void setPraises(List<SharePraise> praises) {
		this.praises = praises;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

}
