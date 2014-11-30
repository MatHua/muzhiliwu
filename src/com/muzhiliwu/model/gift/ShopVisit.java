package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_shop_visit_${year}_${month}")
@TableIndexes({ @Index(name = "idx_shop_visit", fields = { "shopId" }, unique = false) })
public class ShopVisit extends IdEntity {
	@Column
	private String shopId;// 商家id
	@Column
	private int visitNum;// 计数

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

}
