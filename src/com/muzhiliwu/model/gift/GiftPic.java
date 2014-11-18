package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

//商品款式对应的照片
@Table("t_gift_pic")
@TableIndexes({ @Index(name = "idx_gift_pic", fields = { "giftId" }, unique = false) })
public class GiftPic extends IdEntity {
	@Column
	private String giftId;// 联结id
	@Column
	private String picPath;// 图片路径

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

}
