package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_gift_click_${year}_${month}")
@TableIndexes({ @Index(name = "idx_gift_click", fields = { "giftId" }, unique = false) })
public class GiftClick extends IdEntity {
	@Column
	private String giftId;// 礼品id
}
