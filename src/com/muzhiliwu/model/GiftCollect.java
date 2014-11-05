package com.muzhiliwu.model;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.gift.Gift;

@Table("t_gift_collect")
public class GiftCollect extends IdEntity {
	@Column
	private String collectorId;// 收藏者id
	@One(target = User.class, field = "collectorId")
	private User collector;// 收藏者
	@Column
	private String giftId;// 被收藏的礼品的id
	@One(target = Gift.class, field = "giftId")
	private Gift collect;// 被收藏的礼物

	@Column
	private int commentNum;// 评论数
	@Many(target = GiftCollectComment.class, field = "collectId")
	private List<GiftCollectComment> comments;// 评论

	@Column
	private int praiseNum;// 点赞数
	@Many(target = GiftCollectPraise.class, field = "collectId")
	private List<GiftCollectPraise> praises;// 点赞
}