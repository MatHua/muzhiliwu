package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_gift_collect_praise")
@TableIndexes({ @Index(name = "idx_gift_collect_praise", fields = { "collectId" }, unique = false) })
public class GiftCollectPraise extends IdEntity {
	@Column
	private String collectId;// 被点赞礼物收藏的id

	@Column
	private String praiserId;
	@One(target = User.class, field = "praiserId")
	private User praiser;// 便于记录点赞者信息

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getPraiserId() {
		return praiserId;
	}

	public void setPraiserId(String praiserId) {
		this.praiserId = praiserId;
	}

	public User getPraiser() {
		return praiser;
	}

	public void setPraiser(User praiser) {
		this.praiser = praiser;
	}

}