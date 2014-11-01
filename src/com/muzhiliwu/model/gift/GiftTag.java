package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_tag")
public class GiftTag extends IdEntity {
	@Column
	private String giftId;
	@Column
	private String tagId;
}
