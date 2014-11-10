package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_wish_praise")
@TableIndexes({ @Index(name = "idx_wish_praise", fields = { "wishId",
		"praiserId" }, unique = false) })
public class WishPraise extends IdEntity {
	@Column
	private String wishId;// 联结"t_wish"表

	@Column
	private String praiserId;// 点赞者id

	@One(target = User.class, field = "praiserId")
	private User praiser;// 便于记录点赞者信息

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
	}

	public User getPraiser() {
		return praiser;
	}

	public void setPraiser(User praiser) {
		this.praiser = praiser;
	}

	public String getPraiserId() {
		return praiserId;
	}

	public void setPraiserId(String praiserId) {
		this.praiserId = praiserId;
	}

}
