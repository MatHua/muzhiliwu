package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_style")
public class GiftStyle extends IdEntity {
	@Column
	private String giftId;// 联结id

	@Column
	private String styleName;// 商品款式名

	@Column
	private float price;// 单价

	@Many(target = GiftStylePic.class, field = "styleId")
	private List<GiftStylePic> stylePics;// 款式对应的图片

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public List<GiftStylePic> getStylePics() {
		return stylePics;
	}

	public void setStylePics(List<GiftStylePic> stylePics) {
		this.stylePics = stylePics;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
