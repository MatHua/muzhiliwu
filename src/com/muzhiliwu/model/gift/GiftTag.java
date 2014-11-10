package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

//n对n的中间表~  【商品-中间表-商品标签表】
@Table("t_gift_tag")
@TableIndexes({ @Index(name = "idx_gift_tag", fields = { "giftId", "tagId" }, unique = false) })
public class GiftTag extends IdEntity {
	@Column
	private String giftId;// 礼品id
	@Column
	private String tagId;// 礼品的标签

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

}
