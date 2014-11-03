package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

@Table("t_shop")
public class Shop extends IdEntity {
	public static final String Entity_Shop = "entity_shop";// 实体店
	public static final String Online_Store = "online_store";// 网店

	@Column
	private String shop_name;// 店名
	@Column
	private String shop_type;// 店类型
	@Column
	private String shop_boss;// 店主(负责人)
	@Column
	private String mobile;// 手机
	@Column
	private String email;// 邮箱
	@Column
	@ColDefine(type = ColType.TEXT)
	private String address;// 地址
	@Column
	@ColDefine(type = ColType.TEXT)
	private String ShopStory;// 品牌故事
	@Column
	private int goods_number;// 商品数量

	@Many(target = Gift.class, field = "shopId")
	private List<Gift> gifts;// 商品
}
