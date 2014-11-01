package com.muzhiliwu.model.gift;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

import com.muzhiliwu.model.User;

@Table("t_receive_contact_way")
public class ReceiveContactWay {
	@Column
	private String creatorId;// 创建者id
	@One(target = User.class, field = "creatorId")
	private User creator;// 创建者
	@Column
	private String receive_name;// 收货人姓名
	@Column
	@ColDefine(type = ColType.TEXT)
	private String receive_address;// 收货人地址
	@Column
	private String receive_zip;// 收货人邮编
	@Column
	private String receive_phone;// 收货人电话
	@Column
	private String receive_mobile;// 收货人手机

}
