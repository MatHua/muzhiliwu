package com.muzhiliwu.model.gift;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

//商品标签表
@Table("t_tag")
public class Tag extends IdEntity {
	@Column
	private String creatorId;// 创建者id
	@One(target = Shop.class, field = "creatorId")
	private Shop creator;// 创建者

	@Column
	private String name;// 标签名
	@Column
	private int searchCount;// 被搜索次数

	@ManyMany(target = Gift.class, relation = "t_gift_tag", from = "tagId", to = "giftId")
	private List<Gift> gifts;// 方便找到该标签对应的商品

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Shop getCreator() {
		return creator;
	}

	public void setCreator(Shop creator) {
		this.creator = creator;
	}

	public List<Gift> getGifts() {
		return gifts;
	}

	public void setGifts(List<Gift> gifts) {
		this.gifts = gifts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}

}
