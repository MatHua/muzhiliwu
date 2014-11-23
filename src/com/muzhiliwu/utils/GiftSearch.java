package com.muzhiliwu.utils;

//许愿条件筛选
public class GiftSearch {
	private String fromType;// 商店类型(线上商品,线下商品)
	private String type;// 礼品类型
	private String suitSex;// 性别
	private String suitStar;// 星座
	private String suitAgeGroup;// 年龄段

	private Double minPrice;// 最低价
	private Double maxPrice;// 最高价

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSuitSex() {
		return suitSex;
	}

	public void setSuitSex(String suitSex) {
		this.suitSex = suitSex;
	}

	public String getSuitStar() {
		return suitStar;
	}

	public void setSuitStar(String suitStar) {
		this.suitStar = suitStar;
	}

	public String getSuitAgeGroup() {
		return suitAgeGroup;
	}

	public void setSuitAgeGroup(String suitAgeGroup) {
		this.suitAgeGroup = suitAgeGroup;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

}
