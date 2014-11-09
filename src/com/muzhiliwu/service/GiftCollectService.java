package com.muzhiliwu.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.muzhiliwu.model.GiftCollect;
import com.muzhiliwu.model.GiftCollectComment;
import com.muzhiliwu.model.GiftCollectPraise;
import com.muzhiliwu.model.GiftCollectUnreadReply;
import com.muzhiliwu.model.MessUnreadReply;
import com.muzhiliwu.model.Message;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.service.gift.GiftService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.Integral;
import com.muzhiliwu.utils.NumGenerator;
import com.muzhiliwu.web.GiftCollectModule;

@IocBean
public class GiftCollectService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;
	@Inject
	private GiftService giftService;

	/**
	 * 收藏一个礼品商品
	 * 
	 * @param collector
	 *            收藏者
	 * @param gift
	 *            被收藏的礼品商品
	 * @return
	 */
	public String collectGift(User collector, Gift gift, GiftCollect collect,
			HttpSession session) {
		if (okCollect(collector, gift)) {// 可以被收藏
			if (!userService.okIntegral(collector,
					Integral.Integral_For_Collect_Gift, session)) {
				return ActionMessage.Not_Integral;
			}
			collect.setId(NumGenerator.getUuid());// 主键id
			collect.setDate(DateUtils.now());// 被收藏时间
			collect.setCollectorId(collector.getId());// 收藏者id
			collect.setGiftId(gift.getId());// 礼品id

			collect.setPraiseNum(0);// 点赞数为0
			collect.setCommentNum(0);// 评论数为0
			dao.insert(collect);

			giftService.changeGiftCollectNum(gift, 1);// 被收藏的礼品商品被收藏数+1
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 删除收藏某商品~~还没写~~
	 * 
	 * @param collector
	 * @param gift
	 * @return
	 */
	// public String deleteCollectGift(User collector, Gift gift) {
	// if (!okCollect(collector, gift)) {// 不可被收藏的才是已经被收藏的,才是可以被取消的
	//
	// return ActionMessage.success;
	// }
	// return ActionMessage.fail;
	// }

	/**
	 * 评论礼品收藏
	 * 
	 * @param collect
	 *            被评论的礼品收藏
	 * @param commenter
	 *            评论者
	 * @param comment
	 *            评论内容
	 * @param fatherCommenter
	 *            父评论者
	 * @param session
	 * @return
	 */
	public String commentGiftCollect(GiftCollect collect, User commenter,
			GiftCollectComment comment, User fatherCommenter,
			HttpSession session) {
		if (!userService.okIntegral(commenter,
				Integral.Integral_For_Collect_Gift, session)) {
			return ActionMessage.Not_Integral;
		}
		comment.setId(NumGenerator.getUuid());
		comment.setCollectId(collect.getId());
		comment.setCommenterId(commenter.getId());
		comment.setDate(DateUtils.now());
		// 如果父评论存在,则为父评论插入子评论
		if (fatherCommenter != null
				&& !Strings.isBlank(fatherCommenter.getId())) {
			comment.setFatherCommenterId(fatherCommenter.getId());
			createUnreadCommentReply(commenter, fatherCommenter, comment,
					collect);// 给评论者发送一条评论类未读信息
		}
		createUnreadCommentReply(commenter, comment, collect);// 给礼品收藏者发送一条评论类未读信息
		changeCommentNumber(collect, 1);// 评论数+1
		dao.insert(comment);// 插入一条评论
		return ActionMessage.success;
	}

	/**
	 * 点赞礼品收藏
	 * 
	 * @param praiser
	 *            点赞者
	 * @param collect
	 *            被点赞的礼品收藏
	 * @return
	 */
	public String praiseGiftCollect(User praiser, GiftCollect collect,
			HttpSession session) {
		if (okPraise(praiser, collect)) {
			if (!userService.okIntegral(praiser,
					Integral.Integral_For_Praise_Gift, session)) {
				return ActionMessage.Not_Integral;
			}

			GiftCollectPraise praise = new GiftCollectPraise();
			praise.setId(NumGenerator.getUuid());// 主键id
			praise.setDate(DateUtils.now());// 点赞时间
			praise.setCollectId(collect.getId());// 被点赞的礼品收藏的id
			praise.setPraiserId(praiser.getId());// 点赞者的id

			changeGiftCollectPraiseNum(collect, 1);// 对应商品收藏的点赞数+1
			createUnreadPraiseReply(praiser, collect);// 给礼品收藏者发送一条点赞类未读信息
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 取消礼品收藏的点赞
	 * 
	 * @param praiser
	 *            点赞者
	 * @param collect
	 *            被取消点赞的礼品收藏
	 * @return
	 */
	public String cancelPraiseGiftCollect(User praiser, GiftCollect collect) {
		if (!okPraise(praiser, collect)) {
			deletePraise(praiser, collect);// 删除对应的点赞记录
			changeGiftCollectPraiseNum(collect, -1);// 对应的点赞数-1
			deleteUnreadPraiseReply(praiser, collect);// 删除对应的点赞类未读信息
			return ActionMessage.cancel;
		}
		return ActionMessage.fail;
	}

	/**
	 * 获取我的礼品收藏
	 * 
	 * @param user
	 *            指代"自己"
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyGiftCollects(User user, Pager page) {
		List<GiftCollect> collects = dao.query(GiftCollect.class,
				Cnd.where("collectorId", "=", user.getId()).desc("date"), page);
		page.setRecordCount(dao.count(GiftCollect.class,
				Cnd.where("collectorId", "=", user.getId())));
		dao.fetchLinks(collects, "collector");
		dao.fetchLinks(collects, "gift");

		// **********下面是详情~~~~~~~~~~~~~~~~
		// 加载点赞者
		// 加载评论者
		// 加载每条评论的父评论
		return new QueryResult(collects, page);
	}

	/**
	 * 获取某一页礼品收藏
	 * 
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getGiftCollects(Pager page) {
		List<GiftCollect> collects = dao.query(GiftCollect.class, Cnd.orderBy()
				.desc("date"), page);
		page.setRecordCount(dao.count(GiftCollect.class));
		dao.fetchLinks(collects, "collector");
		dao.fetchLinks(collects, "gift");

		// **********下面是详情~~~~~~~~~~~~~~~~
		// 加载点赞者
		// 加载评论者
		// 加载每条评论的父评论
		return new QueryResult(collects, page);
	}

	public GiftCollect getDetails(GiftCollect collect, User user) {
		collect = dao.fetch(GiftCollect.class, collect.getId());
		dao.fetchLinks(collect, "collector");// 加载收藏者
		dao.fetchLinks(collect, "gift");// 加载收藏的商品

		dao.fetchLinks(collect, "praises");// 加载点赞信息
		dao.fetchLinks(collect.getPraises(), "praiser");// 加载点赞者信息

		dao.fetchLinks(collect, "comments");// 加载评论信息
		dao.fetchLinks(collect.getComments(), "commenter");// 加载所有评论者信息
		dao.fetchLinks(collect.getComments(), "fatherCommenter");// 加载父评论信息

		for (GiftCollectComment comment : collect.getComments()) {
			// 判断这条评论是否是本人发出的
			comment.setMeComment(false);
			if (user != null && comment.getCommenterId().equals(user.getId())) {
				comment.setMeComment(true);
			}
		}
		return collect;
	}

	/**
	 * 获取@到自己的点赞类信息
	 * 
	 * @param user
	 *            消息接受者
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyUnreadPraiseReply(User user, Pager page) {
		// 礼品收藏点赞类未读信息
		List<GiftCollectUnreadReply> replies = dao.query(
				GiftCollectUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", GiftCollectUnreadReply.Praise)
						.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				GiftCollectUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", GiftCollectUnreadReply.Praise)
						.and("state", "=", GiftCollectUnreadReply.Nuread)));
		// 获取相应的联结消息
		dao.fetchLinks(replies, "replier");// 加载消息发出者
		return new QueryResult(replies, page);
	}

	/**
	 * 获取@到自己的评论类未读信息
	 * 
	 * @param user
	 *            信息接收者
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyUnreadCommentReply(User user, Pager page) {
		// 礼品收藏点赞类未读信息
		List<GiftCollectUnreadReply> replies = dao.query(
				GiftCollectUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", GiftCollectUnreadReply.Comment)
						.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				GiftCollectUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", GiftCollectUnreadReply.Comment)
						.and("state", "=", GiftCollectUnreadReply.Nuread)));
		// 获取相应的联结消息
		dao.fetchLinks(replies, "replier");// 加载消息发出者
		return new QueryResult(replies, page);
	}

	// 改变礼品收藏的点赞数
	private void changeGiftCollectPraiseNum(GiftCollect collect, int increment) {
		collect = dao.fetch(GiftCollect.class, collect.getId());
		collect.setPraiseNum(collect.getPraiseNum() + increment);
		dao.update(collect);
	}

	// 判断是否已点赞
	private boolean okPraise(User praiser, GiftCollect collect) {
		GiftCollectPraise praise = dao.fetch(
				GiftCollectPraise.class,
				Cnd.where("praiserId", "=", praiser.getId()).and("collectId",
						"=", collect.getId()));
		return praise == null ? true : false;
	}

	// 删除对应的点赞
	private void deletePraise(User praiser, GiftCollect collect) {
		GiftCollectPraise praise = dao.fetch(
				GiftCollectPraise.class,
				Cnd.where("praiserId", "=", praiser.getId()).and("collectId",
						"=", collect.getId()));
		dao.delete(praise);
	}

	// 判断该商品是否已被该用户收藏~
	private boolean okCollect(User collector, Gift gift) {
		GiftCollect collect = dao.fetch(
				GiftCollect.class,
				Cnd.where("collectorId", "=", collector.getId()).and("giftId",
						"=", gift.getId()));
		return collect == null ? true : false;
	}

	// 创建一条未读的点赞类信息
	private void createUnreadPraiseReply(User praiser, GiftCollect collect) {
		if (Strings.isBlank(collect.getCollectorId())) {
			collect = dao.fetch(GiftCollect.class, collect.getId());
		}
		GiftCollectUnreadReply unread = new GiftCollectUnreadReply();
		unread.setCollectId(collect.getId());
		unread.setCollectTitle(collect.getTitle());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(collect.getCollectorId());
		unread.setReplierId(praiser.getId());
		unread.setState(GiftCollectUnreadReply.Nuread);
		unread.setType(GiftCollectUnreadReply.Praise);
		dao.insert(unread);
	}

	// 创建一条未读的评论类信息~回复某人的评论
	private void createUnreadCommentReply(User commenter, User fatherCommenter,
			GiftCollectComment comment, GiftCollect collect) {
		if (Strings.isBlank(collect.getCollectorId())) {
			collect = dao.fetch(GiftCollect.class, collect.getId());
		}
		GiftCollectUnreadReply unread = new GiftCollectUnreadReply();
		unread.setCollectId(collect.getId());
		unread.setCollectTitle(collect.getTitle());
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(fatherCommenter.getId());
		unread.setReplierId(commenter.getId());
		unread.setState(GiftCollectUnreadReply.Nuread);
		unread.setType(GiftCollectUnreadReply.Comment);
		dao.insert(unread);
	}

	// 创建一条未读的评论类信息~发送给留言的发表者
	private void createUnreadCommentReply(User commenter,
			GiftCollectComment comment, GiftCollect collect) {
		if (Strings.isBlank(collect.getCollectorId())) {
			collect = dao.fetch(GiftCollect.class, collect.getId());
		}
		GiftCollectUnreadReply unread = new GiftCollectUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(collect.getCollectorId());
		unread.setReceiverId(commenter.getId());
		unread.setState(GiftCollectUnreadReply.Nuread);
		unread.setType(GiftCollectUnreadReply.Comment);
		unread.setCollectId(collect.getId());
		unread.setCollectTitle(collect.getTitle());
		dao.insert(unread);
	}

	// 修改评论数
	private void changeCommentNumber(GiftCollect collect, int increment) {
		collect = dao.fetch(GiftCollect.class, collect.getId());
		collect.setCommentNum(collect.getCommentNum() + increment);
		dao.update(collect);
	}

	// 删除对应的未读的点赞类消息
	private void deleteUnreadPraiseReply(User praiser, GiftCollect collect) {
		GiftCollectUnreadReply reply = dao.fetch(
				GiftCollectUnreadReply.class,
				Cnd.where("replierId", "=", praiser.getId())
						.and("collectId", "=", collect.getId())
						.and("type", "=", GiftCollectUnreadReply.Praise));
		dao.delete(reply);
	}

}
