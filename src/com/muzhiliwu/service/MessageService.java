package com.muzhiliwu.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.muzhiliwu.model.MessComment;
import com.muzhiliwu.model.MessPraise;
import com.muzhiliwu.model.MessUnreadReply;
import com.muzhiliwu.model.Message;
import com.muzhiliwu.model.Share;
import com.muzhiliwu.model.User;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class MessageService {
	@Inject
	private Dao dao;

	/**
	 * 发布一条留言
	 * 
	 * @param publisher
	 *            发布者
	 * @param msg
	 *            一条留言
	 * @return
	 */
	public boolean publishOrUpdateMessage(User publisher, Message msg) {
		if (Strings.isBlank(msg.getId())) {// 发布一条留言
			msg.setId(NumGenerator.getUuid());
			msg.setDate(DateUtils.now());
			msg.setPublisherId(publisher.getId());
			dao.insert(msg);
		} else {// 修改一条留言
			msg.setDate(DateUtils.now());
			msg.setPublisherId(publisher.getId());
			dao.update(msg);
		}
		return true;
	}

	/**
	 * 留言点赞
	 * 
	 * @param msg
	 *            被点赞的留言
	 * @param praiser
	 *            点赞者
	 * @return
	 */
	public boolean praiseMessage(Message msg, User praiser) {
		if (okPraise(msg.getId(), praiser.getId())) {// 点赞
			MessPraise praise = new MessPraise();
			praise.setId(NumGenerator.getUuid());
			praise.setDate(DateUtils.now());
			praise.setMessId(msg.getId());// 这是联结的id
			praise.setPraiserId(praiser.getId());// 这是联结id
			dao.insert(praise);
			return true;
		} else {// 取消点赞
			MessPraise praise = dao.fetch(
					MessPraise.class,
					Cnd.where("messId", "=", msg.getId()).and("praiserId", "=",
							praiser.getId()));
			dao.delete(MessPraise.class, praise.getId());
			return false;
		}
	}

	// 点赞数增减
	private void changePraiseNumber(Message msg, int i) {
		msg = dao.fetch(Message.class, msg.getId());
		msg.setPraiseNum(msg.getPraiseNum() + i);
		dao.update(msg);
	}

	/**
	 * 检查是否已点赞
	 * 
	 * @param messId
	 *            留言的id
	 * @param pariserId
	 *            点赞者的id
	 * @return
	 */
	public boolean okPraise(String messId, String praiserId) {
		MessPraise praise = dao.fetch(
				MessPraise.class,
				Cnd.where("messId", "=", messId).and("praiserId", "=",
						praiserId));
		return praise == null ? true : false;
	}

	/**
	 * 留言墙评论
	 * 
	 * @param msg
	 *            要评论的留言
	 * @param commenter
	 *            评论者
	 * @param comment
	 *            一条评论信息
	 * @param fatherCommenter
	 *            该评论的父评论(因为该评论可能是一条回复别人评论的评论)
	 * @return
	 */
	public boolean commentMessage(Message msg, User commenter,
			MessComment comment, User fatherCommenter) {
		comment.setId(NumGenerator.getUuid());
		comment.setCommenterId(commenter.getId());// 联结id
		comment.setDate(DateUtils.now());
		comment.setMessId(msg.getId());// 联结id
		// 如果父评论存在,则为父评论插入子评论
		if (!Strings.isBlank(fatherCommenter.getId())) {
			comment.setFatherCommenterId(fatherCommenter.getId());
			createUnreadReply(commenter, fatherCommenter, comment);
		}
		dao.insert(comment);// 插入一条评论
		return true;
	}

	/**
	 * 创建一条未读信息
	 * 
	 * @param commenter
	 *            评论发表者
	 * @param fatherCommenter
	 *            被评论者
	 * @param comment
	 *            发表的评论
	 */
	private void createUnreadReply(User commenter, User fatherCommenter,
			MessComment comment) {
		MessUnreadReply unread = new MessUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(fatherCommenter.getId());
		unread.setReplierId(commenter.getId());
		unread.setState(MessUnreadReply.NUREAD);
		dao.insert(unread);
	}

	/**
	 * 获取某一页留言
	 * 
	 * @param page
	 * @return
	 */
	public QueryResult getMessages(Pager page) {
		List<Message> msgs = dao.query(Message.class, Cnd.orderBy()
				.desc("date"), page);
		page.setRecordCount(dao.count(Message.class));
		Message msg;
		List<Message> results = new ArrayList<Message>();
		for (int i = 0; i < msgs.size(); i++) {
			// 加载点赞者
			dao.fetchLinks(msgs.get(i), "praises", Cnd.orderBy().desc("date"));
			// 加载评论者
			dao.fetchLinks(msgs.get(i), "comments", Cnd.orderBy().desc("date"));
			// 加载每条评论的父评论
			for (int j = 0; j < msgs.get(i).getComments().size(); j++) {
				dao.fetchLinks(msgs.get(i).getComments().get(j),
						"fatherCommenter");
			}
		}
		return new QueryResult(msgs, page);
	}

	/**
	 * 获得用户某一页的留言
	 * 
	 * @param user
	 *            留言发表者
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyMessages(User user, Pager page) {
		List<Message> msgs = dao.query(
				Message.class,
				Cnd.where("publisherId", "=", user.getId()).orderBy()
						.desc("date"), page);
		// dao.fetchLinks(user, "myMessages", Cnd.orderBy().desc("date"));
		page.setRecordCount(dao.count(Message.class,
				Cnd.where("publisherId", "=", user.getId())));
		Message msg;
		List<Message> results = new ArrayList<Message>();
		for (int i = 0; i < msgs.size(); i++) {
			// 加载点赞者
			dao.fetchLinks(msgs.get(i), "praises", Cnd.orderBy().desc("date"));
			// 加载评论者
			dao.fetchLinks(msgs.get(i), "comments", Cnd.orderBy().desc("date"));
			// 加载每条评论的父评论
			for (int j = 0; j < msgs.get(i).getComments().size(); j++) {
				dao.fetchLinks(msgs.get(i).getComments().get(j),
						"fatherCommenter");
			}
		}
		return new QueryResult(msgs, page);
	}

}
