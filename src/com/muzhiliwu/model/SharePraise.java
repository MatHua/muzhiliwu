package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_share_praise")
public class SharePraise {
	@Column
	private String shareId;// 联结"t_share"表

	@One(target = User.class, field = "id")
	private User praiser;// 便于记录点赞者信息

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public User getPraiser() {
		return praiser;
	}

	public void setPraiser(User praiser) {
		this.praiser = praiser;
	}

}
