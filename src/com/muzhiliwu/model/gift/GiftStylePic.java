package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_style_pic")
public class GiftStylePic extends IdEntity {
	@Column
	private String styleId;// 联结id
	@Column
	private String pic_path;// 图片路径
}
