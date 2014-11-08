package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_mess_unread_reply")
public class MessUnreadReply extends IdEntity {
	public static final String Nuread = "unread";// 未读
	public static final String Read = "read";// 已读

	public static final String Praise = "praise";// 点赞类未读信息
	public static final String Comment = "comment";// 评论类未读信息
	@Column
	private String receiverId;// 接收者id,用于联结"t_user"表

	@Column
	private String replierId;// 回复者id

	@One(target = User.class, field = "replierId")
	private User replier;// 便于记录回复者

	@Column
	private String messId;// 被评论的留言的id
	@Column
	private String messTitle;// 被评论的留言的标题

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;

	@Column
	private String state;// 状态

	@Column
	private String type;// 未读消息类型

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

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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

	public String getMessId() {
		return messId;
	}

	public void setMessId(String messId) {
		this.messId = messId;
	}

	public String getMessTitle() {
		return messTitle;
	}

	public void setMessTitle(String messTitle) {
		this.messTitle = messTitle;
	}

}
