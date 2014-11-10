package com.muzhiliwu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_mess_praise")
@TableIndexes({ @Index(name = "idx_mess_praise", fields = { "messId" }, unique = false) })
public class MessPraise extends IdEntity {
	@Column
	private String messId;// 所点赞的评论的id,用于联结"t_message"的messageId(id)

	@Column
	private String praiserId;
	@One(target = User.class, field = "praiserId")
	private User praiser;// 便于记录点赞者

	public String getMessId() {
		return messId;
	}

	public void setMessId(String messId) {
		this.messId = messId;
	}

	public User getPraiser() {
		return praiser;
	}

	public void setPraiser(User praiser) {
		this.praiser = praiser;
	}

	public String getPraiserId() {
		return praiserId;
	}

	public void setPraiserId(String praiserId) {
		this.praiserId = praiserId;
	}

}
