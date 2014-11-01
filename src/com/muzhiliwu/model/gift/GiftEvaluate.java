package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

//商品评价表
@Table("t_gift_evaluate")
public class GiftEvaluate extends IdEntity {
	public static final String Good = "good";// 好评
	public static final String Bad = "bad";// 差评
	public static final String Middle = "mid";// 中评

	@Column
	private String giftId;// 被评价的礼品的id
	@One(target = Gift.class, field = "giftId")
	private Gift gift;// 被评价的礼品
	@Column
	private String valuatorId;// 评价发表者id
	@One(target = User.class, field = "valuatorId")
	private User valuator;// 评价发表者者
	@Column
	@ColDefine(type = ColType.TEXT)
	private String content;// 评价内容
	@Column
	private String type;// 评加类型
	@Column
	@ColDefine(type = ColType.TEXT)
	private String reply;// 商家回复内容
}
