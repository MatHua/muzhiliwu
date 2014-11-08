package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

//商品评价表
@Table("t_gift_comment")
public class GiftComment extends IdEntity {
	public static final String Good = "good";// 好评
	public static final String Bad = "bad";// 差评
	public static final String Middle = "mid";// 中评

	@Column
	private String giftId;// 被评价的礼品的id
	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 被评价的礼品
	
	@Column
	private String commenterId;// 评价发表者id
	@One(target = User.class, field = "commenterId")
	private User commenter;// 评价发表者者
	
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评价内容
	@Column
	private String type;// 评加类型
	@Column
	@ColDefine(type = ColType.TEXT)
	private String reply;// 商家回复内容

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

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

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

}
