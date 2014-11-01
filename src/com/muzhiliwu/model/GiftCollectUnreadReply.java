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

	@Column
	private String receiverId;// 接收者id,用于联结"t_user"表

	@Column
	private String replierId;// 回复者id
	@One(target = User.class, field = "replierId")
	private User replier;// 便于记录回复者

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;

	@Column
	private String state;// 状态

}
