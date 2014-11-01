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
	private String style_name;// 商品款式名
	@Many(target = GiftStylePic.class, field = "styleId")
	private List<GiftStylePic> stylePics;
}
