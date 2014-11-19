package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.muzhiliwu.model.IdEntity;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;

@Table("t_gift_share")
@TableIndexes({ @Index(name = "idx_gift_share", fields = { "giftId" }, unique = false) })
public class GiftShare extends IdEntity {
	@Column
	private String giftId;// 联结"t_wish"表
	@Column
	private String sharerId;// 联结"t_user"表

	@One(target = User.class, field = "sharerId")
	private User sharer;// 便于保存许愿收藏者的信息

	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 一个愿望,便于保存用户查找所收藏的愿望

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
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

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

}
