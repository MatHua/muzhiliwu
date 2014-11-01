package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_comment")
public class GiftCollectComment extends IdEntity {
	@Column
	private String collectId;// 被评论礼物收藏的id

	@Column
	private String commenterId;// 评论者id
	@One(target = User.class, field = "commenterId")
	private User commenter;// 评论者

	@Column
	private String fatherCommenterId;// 父评论的id
	@One(target = User.class, field = "fatherCommenterId")
	private User fatherCommenter;// 父级评论者

	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评论内容
}
