package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_share_praise")
@TableIndexes({ @Index(name = "idx_share_praise", fields = { "shareId" }, unique = false) })
public class SharePraise extends IdEntity {
	@Column
	private String shareId;// 联结"t_share"表

	@Column
	private String praiserId;
	@One(target = User.class, field = "praiserId")
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

	public String getPraiserId() {
		return praiserId;
	}

	public void setPraiserId(String praiserId) {
		this.praiserId = praiserId;
	}

}
