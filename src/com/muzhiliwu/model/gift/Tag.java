package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_tag")
public class Tag extends IdEntity {
	@Column
	private String tag_name;// 标签名
	@Column
	private String creatorId;// 创建者id
	@One(target = Shop.class, field = "creatorId")
	private Shop creator;// 创建者

	@ManyMany(target = Gift.class, relation = "t_gift_tag", from = "tagId", to = "giftId")
	private List<Gift> gifts;// 方便找到该标签对应的商品
}
