package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_share_collect")
public class ShareCollect extends IdEntity {
	@Column
	private String shareId;// 联结"t_share"表
	@Column
	private String collecterId;// 联结"t_user"表

	@Column
	private String userId;
	@One(target = User.class, field = "userId")
	private User collecter;// 便于保存分享收藏者的信息

	@One(target = Share.class, field = "shareId")
	private Share share;// 一个愿望,便于保存用户查找所收藏的分享

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public String getCollecterId() {
		return collecterId;
	}

	public void setCollecterId(String collecterId) {
		this.collecterId = collecterId;
	}

	public User getCollecter() {
		return collecter;
	}

	public void setCollecter(User collecter) {
		this.collecter = collecter;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
