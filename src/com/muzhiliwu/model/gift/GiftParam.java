package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

//商品的一些具体参数,用户可以任意创建
@Table("t_gift_param")
public class GiftParam {
	@Column
	private String giftId;// 商品id
	@Column
	private String param_name;// 参数名
	@Column
	private String param_value;// 参数值
}
