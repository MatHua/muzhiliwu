package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

//商品款式对应的照片
@Table("t_gift_style_pic")
public class GiftStylePic extends IdEntity {
	@Column
	private String styleId;// 联结id
	@Column
	private String picPath;// 图片路径

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
