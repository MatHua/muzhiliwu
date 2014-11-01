package com.muzhiliwu.model.gift;

import java.text.DecimalFormat;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift")
public class Gift extends IdEntity {
	private static final DecimalFormat df = new DecimalFormat("0.00");

	@Column
	private String shopId;// 商家id
	@One(target = Shop.class, field = "shopId")
	private Shop seller;// 商家

	@Column
	private String subject;// 商品名称
	@Column
	@ColDefine(type = ColType.TEXT)
	private String body;// 商品描述
	@Column
	private Double price;// 商品价格
	@Column
	private String type;// 商品类型
	@Column
	private long collectNum;// 收藏数
	// @Column
	// private long

	@Many(target = GiftStyle.class, field = "giftId")
	private List<GiftStyle> styles;// 礼物的款式

	@ManyMany(target = Tag.class, relation = "t_gift_tag", from = "giftId", to = "tagId")
	private List<Tag> tags;// 获取礼品被标注的标签

	@Column
	private int evaluatNum;// 评价数
	@Many(target = GiftEvaluate.class, field = "giftId")
	private List<GiftEvaluate> evaluates;// 商品所有评价
}
