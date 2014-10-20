package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_wish_praise")
public class WishPraise {
	@Column
	private String wishId;// 联结"t_wish"表

	@One(target = User.class, field = "id")
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

}
