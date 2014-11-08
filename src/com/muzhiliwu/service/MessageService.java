package com.muzhiliwu.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.muzhiliwu.model.User;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.Integral;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class MessageService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;

	/**
	 * 发布一条留言
	 * 
	 * @param publisher
	 *            发布者
	 * @param msg
	 *            一条留言
	 * @param session
	 *            修改积分
	 * @return
	 */
	public String publishOrUpdateMessage(User publisher, Message msg,
			HttpSession session) {
		if (Strings.isBlank(msg.getId())) {// 发布一条留言
			if (!userService.okIntegral(publisher,
					Integral.Integral_For_Publish_Message, session)) {
				return ActionMessage.Not_Integral;// 不够积分
			}
			msg.setId(NumGenerator.getUuid());
			msg.setDate(DateUtils.now());
			msg.setPublisherId(publisher.getId());
			msg.setPraiseNum(0);// 点赞数为0
			msg.setCommentNum(0);// 评论数
			dao.insert(msg);
		} else {// 修改一条留言
			msg.setDate(DateUtils.now());
			dao.update(msg);
		}
		return ActionMessage.success;
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
		if (okPraise(msg, praiser)) {// 点赞
			MessPraise praise = new MessPraise();
			praise.setId(NumGenerator.getUuid());
			praise.setDate(DateUtils.now());
			praise.setMessId(msg.getId());// 这是联结的id
			praise.setPraiserId(praiser.getId());// 这是联结id
			changePraiseNumber(msg, 1);// 点赞数+1
			dao.insert(praise);

			createUnreadPraiseReply(praiser, msg);// 给留言发表者发送一条未读的点赞信息
			return true;
		} else {// 取消点赞
			deletePraise(msg, praiser);// 删除点赞记录
			changePraiseNumber(msg, -1);// 点赞数-1

			deleteUnreadPraiseReply(praiser, msg);// 删除对应的未读的点赞信息
			return false;
		}
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
	public String commentMessage(Message msg, User commenter,
			MessComment comment, User fatherCommenter, HttpSession session) {
		if (!userService.okIntegral(commenter,
				Integral.Integral_For_Comment_Message, session)) {
			return ActionMessage.Not_Integral;// 不够积分
		}
		comment.setId(NumGenerator.getUuid());
		comment.setCommenterId(commenter.getId());// 联结id
		comment.setDate(DateUtils.now());
		comment.setMessId(msg.getId());// 联结id
		// 如果父评论存在,则为父评论插入子评论
		if (fatherCommenter != null
				&& !Strings.isBlank(fatherCommenter.getId())) {
			comment.setFatherCommenterId(fatherCommenter.getId());
			createUnreadCommentReply(commenter, fatherCommenter, comment, msg);// 给父评论者发送一个未读信息
		}
		createUnreadCommentReply(commenter, comment, msg);// 给消息发表者发布评论信息
		changeCommentNumber(msg, 1);// 评论数+1
		dao.insert(comment);// 插入一条评论
		return ActionMessage.success;
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

		// 加载发表者
		dao.fetchLinks(msgs, "publisher");
		// **********下面是详情~~~~~~~~~~~~~~~~
		// 加载点赞者
		// 加载评论者
		// 加载每条评论的父评论
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
		List<Message> msgs = dao.query(Message.class,
				Cnd.where("publisherId", "=", user.getId()).desc("date"), page);
		// .orderBy()
		// .desc("date"), page
		// dao.fetchLinks(user, "myMessages", Cnd.orderBy().desc("date"));
		page.setRecordCount(dao.count(Message.class,
				Cnd.where("publisherId", "=", user.getId())));

		// 加载发表者
		dao.fetchLinks(msgs, "publisher");
		// **********下面是详情~~~~~~~~~~~~~~~~
		// 加载点赞者
		// 加载评论者
		// 加载每条评论的父评论
		return new QueryResult(msgs, page);
	}

	/**
	 * 获取留言详情
	 * 
	 * @param msg
	 * @return
	 */
	public Message getDetails(Message msg, User user) {
		msg = dao.fetch(Message.class, msg.getId());
		dao.fetchLinks(msg, "publisher");// 加载发表者信息
		dao.fetchLinks(msg, "praises");// 加载点赞信息

		// 加载点赞的点赞者信息
		dao.fetchLinks(msg.getPraises(), "praiser");
		dao.fetchLinks(msg, "comments");// 加载评论

		// 加载评论者的信息
		dao.fetchLinks(msg.getComments(), "commenter");
		// 加载父评论者的信息
		dao.fetchLinks(msg.getComments(), "fatherCommenter");
		for (int i = 0; i < msg.getComments().size(); i++) {
			// 判断这条评论是否是本人发出的
			msg.getComments().get(i).setMeComment(false);
			if (user != null
					&& msg.getComments().get(i).getCommenterId()
							.equals(user.getId())) {
				msg.getComments().get(i).setMeComment(true);
			}
		}
		return msg;
	}

	/**
	 * 获取@到自己的点赞类消息
	 * 
	 * @param user
	 *            消息接收
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyUnreadPraiseReply(User user, Pager page) {
		// 留言板点赞类未读消息
		List<MessUnreadReply> replys = dao.query(
				MessUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", MessUnreadReply.Praise).desc("date"),
				page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				MessUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", MessUnreadReply.Praise)
						.and("state", "=", MessUnreadReply.Nuread)));
		// 获取相应的联结信息
		dao.fetchLinks(replys, "replier");// 加载消息发出者
		return new QueryResult(replys, page);
	}

	/**
	 * 获取@到自己的评论类消息
	 * 
	 * @param user
	 *            消息接收者
	 * @param page
	 *            分类参数
	 * @return
	 */
	public QueryResult getMyUnreadCommentReply(User user, Pager page) {
		// 留言板评论类未读消息
		List<MessUnreadReply> replys = dao
				.query(MessUnreadReply.class,
						Cnd.where("receiverId", "=", user.getId())
								.and("type", "=", MessUnreadReply.Comment)
								.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				MessUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", MessUnreadReply.Comment)
						.and("state", "=", MessUnreadReply.Nuread)));
		// 获取相应的联结信息
		dao.fetchLinks(replys, "replier");// 加载消息发出者
		return new QueryResult(replys, page);
	}

	/**
	 * 删除一个留言
	 * 
	 * @param operator
	 *            操作者
	 * @param msg
	 *            要被删除的留言
	 * @return
	 */
	// public String deleteMessage(User operator, Message msg) {
	// msg = dao.fetch(Message.class, msg.getId());
	// if (msg.getPublisherId().equals(operator.getId())) {// 是发表者才能删
	// dao.deleteLinks(msg, "praises");// 删除对应的点赞信息
	// dao.deleteLinks(msg, "comments");// 删除对应的评论信息
	// dao.delete(msg);
	// return ActionMessage.success;
	// }
	// return ActionMessage.fail;
	// }

	// 删除点赞记录
	private void deletePraise(Message msg, User praiser) {
		MessPraise praise = dao.fetch(
				MessPraise.class,
				Cnd.where("messId", "=", msg.getId()).and("praiserId", "=",
						praiser.getId()));
		if (praise != null) {
			dao.delete(MessPraise.class, praise.getId());
		}
	}

	// 点赞数增减
	private void changePraiseNumber(Message msg, int i) {
		msg = dao.fetch(Message.class, msg.getId());
		msg.setPraiseNum(msg.getPraiseNum() + i);
		dao.update(msg);
	}

	// 检查是否已点赞
	private boolean okPraise(Message msg, User praiser) {
		MessPraise praise = dao.fetch(
				MessPraise.class,
				Cnd.where("messId", "=", msg.getId()).and("praiserId", "=",
						praiser.getId()));
		return praise == null ? true : false;
	}

	// 改变评论数
	private void changeCommentNumber(Message msg, int i) {
		msg = dao.fetch(Message.class, msg.getId());
		msg.setCommentNum(msg.getCommentNum() + i);
		dao.update(msg);
	}

	// 创建一条未读的评论类信息~回复某人的评论
	private void createUnreadCommentReply(User commenter, User fatherCommenter,
			MessComment comment, Message msg) {
		if (Strings.isBlank(msg.getTitle())) {
			msg = dao.fetch(Message.class, msg.getId());
		}
		MessUnreadReply unread = new MessUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(fatherCommenter.getId());
		unread.setReplierId(commenter.getId());
		unread.setState(MessUnreadReply.Nuread);

		unread.setType(MessUnreadReply.Comment);
		unread.setMessId(msg.getId());
		unread.setMessTitle(msg.getTitle());
		dao.insert(unread);
	}

	// 创建一条未读的评论类信息~发送给留言的发表者
	private void createUnreadCommentReply(User commenter, MessComment comment,
			Message msg) {
		if (Strings.isBlank(msg.getTitle())) {
			msg = dao.fetch(Message.class, msg.getId());
		}
		MessUnreadReply unread = new MessUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(msg.getPublisherId());
		unread.setReplierId(commenter.getId());
		unread.setState(MessUnreadReply.Nuread);

		unread.setType(MessUnreadReply.Comment);
		unread.setMessId(msg.getId());
		unread.setMessTitle(msg.getTitle());
		dao.insert(unread);
	}

	// 创建一条未读的点赞类消息
	private void createUnreadPraiseReply(User praiser, Message msg) {
		if (Strings.isBlank(msg.getTitle())) {
			msg = dao.fetch(Message.class, msg.getId());
		}
		MessUnreadReply unread = new MessUnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setMessId(msg.getId());
		unread.setMessTitle(msg.getTitle());
		unread.setReceiverId(msg.getPublisherId());
		unread.setReplierId(praiser.getId());
		unread.setState(MessUnreadReply.Nuread);
		unread.setType(MessUnreadReply.Praise);
		dao.insert(unread);
	}

	// 删除对应的未读的点赞类消息
	private void deleteUnreadPraiseReply(User praiser, Message msg) {
		MessUnreadReply reply = dao.fetch(
				MessUnreadReply.class,
				Cnd.where("replierId", "=", praiser.getId())
						.and("messId", "=", msg.getId())
						.and("type", "=", MessUnreadReply.Praise));
		dao.delete(reply);
	}

	// 根据消息id获取消息的标题
	// private String getMessTitle(String id) {
	// Message msg = dao.fetch(Message.class, id);
	// return msg.getTitle();
	// }
}
