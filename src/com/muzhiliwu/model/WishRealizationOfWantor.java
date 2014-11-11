package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_wish_realization_of_wantor")
@TableIndexes({ @Index(name = "idx_wish_realization_of_wantor", fields = {
		"wishId", "wisherId" }, unique = false) })
public class WishRealizationOfWantor extends IdEntity {
	@Column
	private String wantorId;// 想要帮忙实现愿望的人的id
	@One(target = User.class, field = "wantorId")
	private User wantor;// 想要帮忙实现愿望的人

	@Column
	private String wisherId;// 愿望发表者
	@One(target = User.class, field = "wisherId")
	private User wisher;// 愿望发表者

	@Column
	private String wishId;// 愿望id
	@One(target = Wish.class, field = "wishId")
	private Wish wish;// 被申请实现的愿望

	public String getWantorId() {
		return wantorId;
	}

	public void setWantorId(String wantorId) {
		this.wantorId = wantorId;
	}

	public User getWantor() {
		return wantor;
	}

	public void setWantor(User wantor) {
		this.wantor = wantor;
	}

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
	}

	public Wish getWish() {
		return wish;
	}

	public void setWish(Wish wish) {
		this.wish = wish;
	}

	public String getWisherId() {
		return wisherId;
	}

	public void setWisherId(String wisherId) {
		this.wisherId = wisherId;
	}

	public User getWisher() {
		return wisher;
	}

	public void setWisher(User wisher) {
		this.wisher = wisher;
	}

}
