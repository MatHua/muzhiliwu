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

import com.muzhiliwu.model.MessPraise;
import com.muzhiliwu.model.MessUnreadReply;
import com.muzhiliwu.model.Share;
import com.muzhiliwu.model.ShareComment;
import com.muzhiliwu.model.SharePraise;
import com.muzhiliwu.model.ShareUnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.Integral;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class ShareService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;

	/**
	 * 发布或修改一条分享
	 * 
	 * @param sharer
	 *            分享者
	 * @param msg
	 *            一条留言
	 * @return
	 */
	public String publishShare(User sharer, Share share, HttpSession session) {

		if (!userService.okIntegral(sharer,
				Integral.Integral_For_Publish_Share, session)) {
			return ActionMessage.Not_Integral;// 不够积分
		}
		share.setId(NumGenerator.getUuid());
		share.setDate(DateUtils.now());
		share.setSharerId(sharer.getId());
		share.setType(Share.From_Own);// 原创
		share.setCollectNum(0);// 收藏数为0
		share.setCommentNum(0);// 评论数为0
		share.setPraiseNum(0);// 点赞数为0
		dao.insert(share);
		return ActionMessage.success;
	}

	/**
	 * 更新一条分享
	 * 
	 * @param sharer
	 *            分享者
	 * @param tmp
	 *            要更新的内容
	 * @return
	 */
	public String updateShare(User sharer, Share tmp) {
		Share share = dao.fetch(Share.class, tmp.getId());
		if (share.getSharerId().equals(sharer.getId())) {
			share.setContent(tmp.getContent());
			share.setDate(DateUtils.now());
			share.setTitle(tmp.getTitle());
			dao.update(share);
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 收藏一个分享
	 * 
	 * @param collecter
	 *            收藏者
	 * @param share
	 *            一条分享
	 * @param fromer
	 *            分享来源者
	 * @return
	 */
	public String collectShare(User collecter, Share share, HttpSession session) {
		if (okCollect(collecter, share)) {
			if (!userService.okIntegral(collecter,
					Integral.Integral_For_Collect_Share, session)) {
				return ActionMessage.Not_Integral;// 不够积分
			}
			// 模拟一次转载收藏
			share = dao.fetch(Share.class, share.getId());
			Share collect = new Share();
			collect.setId(NumGenerator.getUuid());// 主键
			collect.setDate(DateUtils.now());// 收藏的时间

			collect.setTitle(share.getTitle());// 收藏的标题
			collect.setContent(share.getContent());// 收藏的内容
			collect.setType(Share.From_Other);// 分享的类型:来自收藏

			collect.setFromerId(share.getSharerId());// 收藏的来源者
			collect.setSharerId(collecter.getId());// 收藏者
			collect.setCollectId(share.getId());// 收藏的分享对应的id

			collect.setCollectNum(0);// 被收藏数
			collect.setPraiseNum(0);// 被点赞数
			collect.setCommentNum(0);// 被评论数

			changeCollectNumber(share, 1);// 被收藏数+1
			createUnreadCollectReply(collecter, share);// 给分享的发表者发送一条收藏类的未读信息

			dao.insert(collect);
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 取消收藏
	 * 
	 * @param collecter
	 * @param share
	 * @return
	 */
	public boolean cancelCollectShare(User collecter, Share share) {
		if (okCancelCollect(collecter, share)) {
			dao.delete(Share.class, share.getId());
			deleteUnreadCollectReply(collecter, share);
			return true;
		}
		return false;
	}

	/**
	 * 点赞某条分享
	 * 
	 * @param share
	 *            被点赞的分享
	 * @param praiser
	 *            点赞者
	 * @return
	 */
	public String praiseShare(Share share, User praiser) {
		if (okPraise(share, praiser)) {// 点赞
			SharePraise praise = new SharePraise();
			praise.setId(NumGenerator.getUuid());
			praise.setDate(DateUtils.now());
			praise.setShareId(share.getId());// 这是联结的id
			praise.setPraiserId(praiser.getId());// 这是联结id
			changePraiseNumber(share, 1);

			createUnreadPraiseReply(praiser, share);// 给分享的发表者发送一条未读的点赞信息
			dao.insert(praise);
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 取消点赞
	 * 
	 * @param share
	 *            被取消点赞的分享
	 * @param praiser
	 *            点赞者
	 * @return
	 */
	public String cancelPraiseShare(Share share, User praiser) {
		if (!okPraise(share, praiser)) {
			// 删除点赞记录
			deletePraise(share, praiser);
			changePraiseNumber(share, -1);

			deleteUnreadPraiseReply(praiser, share);// 给分享的发表者发送一条未读的点赞信息
			return ActionMessage.cancel;
		}
		return ActionMessage.fail;
	}

	/**
	 * 分享墙评论
	 * 
	 * @param share
	 *            要评论的分享
	 * @param commenter
	 *            评论者
	 * @param comment
	 *            一条评论信息
	 * @param fatherCommenter
	 *            该评论的父评论(因为该评论可能是一条回复别人评论的评论)
	 * @return
	 */
	public String commentShare(Share share, User commenter,
			ShareComment comment, User fatherCommenter, HttpSession session) {
		if (!userService.okIntegral(commenter,
				Integral.Integral_For_Comment_Share, session)) {
			return ActionMessage.Not_Integral;// 不够积分
		}
		comment.setId(NumGenerator.getUuid());
		comment.setCommenterId(commenter.getId());// 联结id
		comment.setDate(DateUtils.now());
		comment.setShareId(share.getId());// 联结id
		// 如果父评论存在,则为父评论插入子评论
		if (fatherCommenter != null
				&& !Strings.isBlank(fatherCommenter.getId())) {
			comment.setFatherCommenterId(fatherCommenter.getId());
			createUnreadCommentReply(commenter, fatherCommenter, comment, share);// 给父评论者发送一条未读信息
		}
		createUnreadCommentReply(commenter, comment, share);// 给分享的发表者发送一条未读信息
		changeCommentNumber(share, 1);// 评论数+1
		dao.insert(comment);// 插入一条评论
		return ActionMessage.success;
	}

	/**
	 * 获取某一页分享
	 * 
	 * @param page
	 * @return
	 */
	public QueryResult getShares(Pager page, User user) {
		List<Share> shares = dao.query(Share.class, Cnd.orderBy().desc("date"),
				page);
		page.setRecordCount(dao.count(Share.class));

		// 加载分享发表者
		dao.fetchLinks(shares, "sharer");
		// 如果该分享是收藏别人的,加载收藏的来源
		dao.fetchLinks(shares, "fromer");
		for (Share share : shares) {
			share.setCollected(false);
			// 判断用户有没有收藏该分享
			if (!okCollect(user, share)) {
				share.setCollected(true);
			}
			// **********下面是详情~~~~~~~~~~~~~~~~
			// 加载点赞者
			// 加载评论者
			// 加载每条评论的父评论
		}
		return new QueryResult(shares, page);
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
	public QueryResult getMyShares(User user, Pager page) {
		List<Share> shares = dao.query(Share.class,
				Cnd.where("sharerId", "=", user.getId()).desc("date"), page);
		page.setRecordCount(dao.count(Share.class,
				Cnd.where("sharerId", "=", user.getId())));

		dao.fetchLinks(shares, "sharer");
		// 如果该分享是收藏别人的,加载收藏的来源
		dao.fetchLinks(shares, "fromer");
		// **********下面是详情~~~~~~~~~~~~~~~~
		// 加载点赞者
		// 加载评论者
		// 加载每条评论的父评论
		return new QueryResult(shares, page);
	}

	/**
	 * 分享详情
	 * 
	 * @param share
	 * @return
	 */
	public Share getDetail(Share share, User user) {
		share = dao.fetch(Share.class, share.getId());

		// 判断用户有没有收藏该分享
		share.setCollected(false);
		if (user != null && !okCollect(user, share)) {
			share.setCollected(true);
		}

		dao.fetchLinks(share, "sharer");
		if (!Strings.isBlank(share.getFromerId())) {
			// 如果是收藏的,加载收藏的来源者
			dao.fetchLinks(share, "fromer");
		}
		dao.fetchLinks(share, "praises");// 加载点赞信息

		// 加载点赞的点赞者信息
		dao.fetchLinks(share.getPraises(), "praiser");
		// 加载评论
		dao.fetchLinks(share, "comments");
		// 加载评论者的信息
		dao.fetchLinks(share.getComments(), "commenter");
		// 加载父评论者的信息
		dao.fetchLinks(share.getComments(), "fatherCommenter");
		for (ShareComment comment : share.getComments()) {
			// 判断这条评论是否是本人发出的
			comment.setMeComment(false);
			if (user != null && comment.getCommenterId().equals(user.getId())) {
				comment.setMeComment(true);
			}
		}
		return share;
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
		// 分享墙点赞类未读消息
		List<ShareUnreadReply> replys = dao
				.query(ShareUnreadReply.class,
						Cnd.where("receiverId", "=", user.getId())
								.and("type", "=", ShareUnreadReply.Praise)
								.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				ShareUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", ShareUnreadReply.Praise)
						.and("state", "=", ShareUnreadReply.Nuread)));
		// 加载消息发出者
		dao.fetchLinks(replys, "replier");
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
		// 分享墙评论类未读消息
		List<ShareUnreadReply> replys = dao.query(
				ShareUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", ShareUnreadReply.Comment)
						.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				ShareUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", ShareUnreadReply.Comment)
						.and("state", "=", ShareUnreadReply.Nuread)));

		// 加载消息发出者
		dao.fetchLinks(replys, "replier");
		return new QueryResult(replys, page);
	}

	/**
	 * 获取@到自己的收藏类的未读信息
	 * 
	 * @param user
	 *            未读信息接受者
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getMyUnreadCollectReply(User user, Pager page) {
		// 分享墙收藏类未读信息
		List<ShareUnreadReply> replys = dao.query(
				ShareUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", ShareUnreadReply.Collect)
						.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				ShareUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", ShareUnreadReply.Collect)
						.and("state", "=", ShareUnreadReply.Nuread)));

		dao.fetchLinks(replys, "replier");// 加载消息发出者
		return new QueryResult(replys, page);
	}

	// 给分享的发表者发送一条收藏类未读信息
	private void createUnreadCollectReply(User collecter, Share share) {
		ShareUnreadReply unread = new ShareUnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(share.getSharerId());
		unread.setReplierId(collecter.getId());
		unread.setState(ShareUnreadReply.Nuread);

		unread.setType(ShareUnreadReply.Collect);
		unread.setShareId(share.getId());
		dao.insert(unread);
	}

	// 判断这个是否为收藏的分享
	private boolean okCancelCollect(User collecter, Share share) {
		share = dao.fetch(Share.class, share.getId());// 获取这条分享
		if (share != null && !share.From_Other.equals(share.getType())
				&& share.getSharerId().equals(collecter.getId())) {
			Share collect = dao.fetch(Share.class, share.getCollectId());
			collect.setCollectNum(collect.getCollectNum() - 1);// 被收藏数-1
			dao.update(collect);
			return true;
		}
		return false;
	}

	// 根据分享id获取分享的标题
	// private String getShareTitle(String id) {
	// Share share = dao.fetch(Share.class, id);
	// return share.getTitle();
	// }

	// 删除对应的点赞信息
	private void deleteUnreadCollectReply(User collecter, Share share) {
		ShareUnreadReply reply = dao.fetch(
				ShareUnreadReply.class,
				Cnd.where("replierId", "=", collecter.getId())
						.and("shareId", "=", share.getId())
						.and("type", "=", ShareUnreadReply.Collect));
		dao.delete(reply);
	}

	// 修改被收藏数
	private void changeCollectNumber(Share share, int increment) {
		share = dao.fetch(Share.class, share.getId());
		share.setCollectNum(share.getCollectNum() + increment);
		dao.update(share);
	}

	// 删除点赞记录
	private void deletePraise(Share share, User praiser) {
		MessPraise praise = dao.fetch(
				MessPraise.class,
				Cnd.where("shareId", "=", share.getId()).and("praiserId", "=",
						praiser.getId()));

		dao.delete(MessPraise.class, praise.getId());
	}

	// 点赞数增减
	private void changePraiseNumber(Share share, int i) {
		share = dao.fetch(Share.class, share.getId());
		share.setPraiseNum(share.getPraiseNum() + i);
		dao.update(share);
	}

	// 检查是否已点赞
	private boolean okPraise(Share share, User praiser) {
		SharePraise praise = dao.fetch(
				SharePraise.class,
				Cnd.where("shareId", "=", share.getId()).and("praiserId", "=",
						praiser.getId()));
		return praise == null ? true : false;
	}

	// 改变评论数
	private void changeCommentNumber(Share share, int i) {
		share = dao.fetch(Share.class, share.getId());
		share.setCommentNum(share.getCommentNum() + i);
		dao.update(share);
	}

	// 给父评论者创建一条未读信息
	private void createUnreadCommentReply(User commenter, User fatherCommenter,
			ShareComment comment, Share share) {
		ShareUnreadReply unread = new ShareUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(fatherCommenter.getId());
		unread.setReplierId(commenter.getId());
		unread.setState(ShareUnreadReply.Nuread);

		unread.setType(ShareUnreadReply.Comment);
		unread.setShareId(share.getId());
		unread.setShareTitle(share.getTitle());
		dao.insert(unread);
	}

	// 给消息发表者创建一条未读信息
	private void createUnreadCommentReply(User commenter, ShareComment comment,
			Share share) {
		if (Strings.isBlank(share.getSharerId())) {
			share = dao.fetch(Share.class, share.getId());
		}
		ShareUnreadReply unread = new ShareUnreadReply();
		unread.setContent(comment.getContent());
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(share.getSharerId());
		unread.setReplierId(commenter.getId());
		unread.setState(ShareUnreadReply.Nuread);

		unread.setType(ShareUnreadReply.Comment);
		unread.setShareId(share.getId());
		unread.setShareTitle(share.getTitle());
		dao.insert(unread);
	}

	// 给消息发表者创建一条未读点赞信息
	private void createUnreadPraiseReply(User praiser, Share share) {
		ShareUnreadReply unread = new ShareUnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(share.getSharerId());
		unread.setReplierId(praiser.getId());
		unread.setState(ShareUnreadReply.Nuread);

		unread.setType(ShareUnreadReply.Praise);
		unread.setShareId(share.getId());
		unread.setShareTitle(share.getTitle());
		dao.insert(unread);
	}

	// 删除对应的点赞信息
	private void deleteUnreadPraiseReply(User praiser, Share share) {
		ShareUnreadReply reply = dao.fetch(
				ShareUnreadReply.class,
				Cnd.where("replierId", "=", praiser.getId())
						.and("shareId", "=", share.getId())
						.and("type", "=", ShareUnreadReply.Praise));
		dao.delete(reply);
	}

	// 检查是否已经收藏
	private boolean okCollect(User collecter, Share share) {
		Share tmp = dao.fetch(
				Share.class,
				Cnd.where("sharerId", "=", collecter.getId()).and("collectId",
						"=", share.getId()));
		return tmp == null ? true : false;
	}
}
