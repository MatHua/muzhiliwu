package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_user_tag")
@TableIndexes({ @Index(name = "idx_user_tag", fields = { "userId" }, unique = false) })
public class UserTag extends IdEntity {
	@Column
	private String userId;// 联结id
	@Column
	private String name;// 标签名

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
