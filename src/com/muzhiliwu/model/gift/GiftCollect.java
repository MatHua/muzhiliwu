package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

@Table("t_gift_collect")
public class GiftCollect extends IdEntity {
	@Column
	private String giftId;// 商品id
	@Column
	private String collecterId;// 收藏者id

	@One(target = User.class, field = "collecterId")
	private User collecter;// 收藏者

	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 被收藏的礼品
}
