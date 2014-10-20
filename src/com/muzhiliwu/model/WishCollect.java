package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_wish_collect")
public class WishCollect extends IdEntity {
	@Column
	private String wishId;// 联结"t_wish"表
	@Column
	private String collecterId;// 联结"t_user"表

	@Column
	private String userId;
	@One(target = User.class, field = "userId")
	private User collecter;// 便于保存许愿收藏者的信息
	
	@One(target = Wish.class, field = "wishId")
	private Wish wish;// 一个愿望,便于保存用户查找所收藏的愿望

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
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

	public Wish getWish() {
		return wish;
	}

	public void setWish(Wish wish) {
		this.wish = wish;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
