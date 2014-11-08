package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_collect_unread_reply")
public class GiftCollectUnreadReply extends IdEntity {
	public static final String Nuread = "unread";// 未读
	public static final String Read = "read";// 已读

	public static final String Comment = "comment";// 评论类未读信息
	public static final String Praise = "praise";// 点赞类未读信息

	@Column
	private String receiverId;// 接收者id,用于联结"t_user"表

	@Column
	private String replierId;// 回复者id
	@One(target = User.class, field = "replierId")
	private User replier;// 便于记录回复者

	@Column
	private String collectId;// 对应礼品收藏的id
	@Column
	private String collectTitle;// 采集描述信息

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;

	@Column
	private String state;// 状态

	@Column
	private String type;// 未读信息类型

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReplierId() {
		return replierId;
	}

	public void setReplierId(String replierId) {
		this.replierId = replierId;
	}

	public User getReplier() {
		return replier;
	}

	public void setReplier(User replier) {
		this.replier = replier;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getCollectTitle() {
		return collectTitle;
	}

	public void setCollectTitle(String collectTitle) {
		this.collectTitle = collectTitle;
	}

}