package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_wish_share")
@TableIndexes({ @Index(name = "idx_wish_share", fields = { "wishId" }, unique = false) })
public class WishShare extends IdEntity {
	@Column
	private String wishId;// 联结"t_wish"表
	@Column
	private String sharerId;// 联结"t_user"表

	@One(target = User.class, field = "sharerId")
	private User sharer;// 便于保存许愿收藏者的信息

	@One(target = Wish.class, field = "wishId")
	private Wish wish;// 一个愿望,便于保存用户查找所收藏的愿望

	public String getWishId() {
		return wishId;
	}

	public Wish getWish() {
		return wish;
	}

	public void setWish(Wish wish) {
		this.wish = wish;
	}

	public String getSharerId() {
		return sharerId;
	}

	public void setSharerId(String sharerId) {
		this.sharerId = sharerId;
	}

	public User getSharer() {
		return sharer;
	}

	public void setSharer(User sharer) {
		this.sharer = sharer;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
	}

}
