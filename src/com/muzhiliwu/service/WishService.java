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

import com.muzhiliwu.model.UnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.model.WishPraise;
import com.muzhiliwu.model.WishRealizationOfWantor;
import com.muzhiliwu.model.WishShare;
import com.muzhiliwu.model.gift.OrderForm;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.MuzhiCoin;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class WishService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;

	/**
	 * 发表或者更新一个许愿
	 * 
	 * @param publisher
	 *            发布者
	 * @param wish
	 *            一个许愿
	 * @param session
	 *            修改积分
	 * @return
	 */
	public String publishWish(User publisher, Wish wish, HttpSession session) {
		// 许愿
		int increment = Wish.Wish_Gift.equals(wish.getType()) ? MuzhiCoin.MuzhiCoin_For_Publish_Gift_Wish
				: MuzhiCoin.MuzhiCoin_For_Publish_No_Gift_Wish;
		if (!userService.okMuzhiCoin(publisher, increment, session)) {
			return ActionMessage.Not_MuzhiCoin;// 不够积分
		}
		wish.setId(NumGenerator.getUuid());
		wish.setDate(DateUtils.now());
		wish.setWisherId(publisher.getId());// 许愿者id
		wish.setState(Wish.Unrealized);// 愿望未实现

		dao.insert(wish);
		return ActionMessage.success;
	}

	/**
	 * 更新一个愿望内容
	 * 
	 * @param publisher
	 * @param tmp
	 * @return
	 */
	public String updateWish(User publisher, Wish tmp) {
		Wish wish = dao.fetch(Wish.class, tmp.getId());
		if (wish.getWisherId().equals(publisher.getId())) {
			wish.setContent(tmp.getContent());
			wish.setDate(DateUtils.now());
			// wish.setTitle(tmp.getTitle());
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 点赞一个许愿
	 * 
	 * @param wish
	 *            被点赞的许愿
	 * @param praiser
	 *            点赞者
	 * @return
	 */
	public String praiseWish(Wish wish, User praiser, HttpSession session) {
		if (okPraise(wish, praiser)) {// 点赞
			if (!userService.okMuzhiCoin(praiser,
					MuzhiCoin.MuzhiCoin_For_Praise_Wish, session)) {
				return ActionMessage.Not_MuzhiCoin;
			}
			WishPraise praise = new WishPraise();
			praise.setId(NumGenerator.getUuid());
			praise.setDate(DateUtils.now());
			praise.setWishId(wish.getId());// 被点赞愿望的id
			praise.setPraiserId(praiser.getId());// 点赞者的id

			createUnreadPraiseReply(praiser, wish);// 给许愿者发送点赞类未读信息

			dao.insert(praise);
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 取消点赞
	 * 
	 * @param wish
	 *            被取消点赞的愿望
	 * @param praiser
	 *            点赞者
	 * @return
	 */
	public String cancelPraiseWish(Wish wish, User praiser) {
		if (!okPraise(wish, praiser)) {// 点赞
			// 取消点赞
			deletePraise(wish, praiser);// 删除点赞记录
			// changePraiseNumber(wish, -1);// 点赞数-1
			deletePraise(wish, praiser);// 删除对应的点赞信息
			return ActionMessage.cancel;
		}
		return ActionMessage.fail;
	}

	/**
	 * 收藏一个愿望
	 * 
	 * @param collecter
	 *            收藏者
	 * @param wish
	 *            一个愿望
	 * @param session
	 * @return
	 */
	// public String collectWish(User collecter, Wish wish, HttpSession session)
	// {
	// if (okCollect(collecter, wish)) {
	// if (!userService.okMuzhiCoin(collecter,
	// MuzhiCoin.MuzhiCoin_For_Collect_Wish, session)) {
	// return ActionMessage.Not_MuzhiCoin;// 不够积分
	// }
	// // 模拟一次收藏
	// WishCollect collect = new WishCollect();
	// collect.setId(NumGenerator.getUuid());
	// collect.setDate(DateUtils.now());
	// collect.setCollecterId(collecter.getId());
	// collect.setWishId(wish.getId());
	// // changeCollectNumber(wish, 1);// 被收藏数+1
	// dao.insert(collect);
	//
	// createUnreadCollectReply(collecter, wish);// 给许愿者发送一个收藏类未读信息
	// return ActionMessage.success;
	// }
	// return ActionMessage.fail;
	// }

	/**
	 * 取消愿望收藏
	 * 
	 * @param collecter
	 *            愿望收藏者
	 * @param collect
	 *            一条收藏
	 * @return
	 */
	// public boolean cancelCollectWish(User collecter, WishCollect collect) {
	// if (okCancelCollect(collecter, collect)) {
	// dao.delete(WishCollect.class, collect.getId());
	// Wish wish = dao.fetch(Wish.class, collect.getWishId());
	// deleteUnreadCollectReply(collecter, wish);// 删除对应的未读信息
	// return true;
	// }
	// return false;
	// }

	/**
	 * 获取一个许愿的详细信息
	 * 
	 * @param wish
	 * @return
	 */
	public Wish getDetail(Wish wish, User user) {
		wish = dao.fetch(Wish.class, wish.getId());

		// 判断用户有没有收藏该分享
		wish.setShared(false);
		if (user != null) {
			// if (!okCollect(user, wish)) {
			// wish.setCollected(true);
			// }
			// 到时候要判断是否被分享,点赞
		}
		dao.fetchLinks(wish, "wisher");
		dao.fetchLinks(wish, "praises");// 加载点赞信息

		// 加载点赞的点赞者信息
		dao.fetchLinks(wish.getPraises(), "praiser");

		// 获取收藏信息
		dao.fetchLinks(wish, "collectes");

		// 获取所有收藏者信息
		dao.fetchLinks(wish.getShares(), "sharer");

		// 还有一些详细信息
		// 加载评论
		// 加载评论者的信息
		// 加载父评论者的信息
		return wish;
	}

	public String deleteWish(User user, Wish wish) {
		wish = dao.fetch(Wish.class, wish.getId());
		if (!wish.getWisherId().equals(user.getId())) {
			return ActionMessage.fail;
		}
		List<WishRealizationOfWantor> wantors = dao.query(
				WishRealizationOfWantor.class,
				Cnd.where("wishId", "=", wish.getId()));
		if (wish != null) {// 删除关联的信息
			dao.delete(wantors);// 删除所有请求实现该愿望的申请
			dao.fetchLinks(wish, "praises");// 点赞
			dao.fetchLinks(wish, "shares");// 分享
			dao.fetchLinks(wish, "wishOrderForm");// 许愿单

			dao.deleteLinks(wish, "praises");
			dao.deleteLinks(wish, "shares");// 分享
			dao.deleteWith(wish, "wishOrderForm");// 许愿单
		}
		return ActionMessage.success;
	}

	/**
	 * 获取我的许愿
	 * 
	 * @param page
	 *            分页参数
	 * @param user
	 *            用户
	 * @param state
	 *            已实现or未实现
	 * @return
	 */
	public QueryResult getMyWishes(Pager page, User user, String state) {
		List<Wish> wishes = dao.query(
				Wish.class,
				Cnd.where("wisherId", "=", user.getId())
						.and("state", "=", state).desc("date"), page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(
				Wish.class,
				Cnd.where("wisherId", "=", user.getId()).and("state", "=",
						state)));

		// dao.fetchLinks(wishes, "wisher");
		dao.fetchLinks(wishes, "wishWantors");// 加载想要帮助实现愿望的人
		for (Wish wish : wishes) {// 获取想要帮助实现该愿望的人数
			wish.setWantorNum(wish.getWishWantors().size());
		}
		// **********下面是详情~~~~~~~~~~~~~~~~
		// 加载点赞者
		// 加载收藏者
		return new QueryResult(wishes, page);
	}

	/**
	 * 获取我收藏的愿望
	 * 
	 * @param page
	 * @param user
	 * @return
	 */
	// public QueryResult getMyCollectWishes(Pager page, User user) {
	// List<WishCollect> collectes = dao.query(WishCollect.class,
	// Cnd.where("collecterId", "=", user.getId()), page);
	// if (page == null)
	// page = new Pager();
	// page.setRecordCount(dao.count(WishCollect.class,
	// Cnd.where("collecterId", "=", user.getId())));
	//
	// dao.fetchLinks(collectes, "collecter");
	// dao.fetchLinks(collectes, "wish");
	// for (WishCollect collect : collectes) {
	// dao.fetchLinks(collect.getWish(), "wisher");
	// }
	// return new QueryResult(collectes, page);
	// }

	/**
	 * 获取想要帮助实现礼物的人
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	public QueryResult getMyWishWantor(User user, Pager page) {
		List<WishRealizationOfWantor> wantors = dao.query(
				WishRealizationOfWantor.class,
				Cnd.where("wisherId", "=", user.getId()).desc("date"), page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(WishRealizationOfWantor.class,
				Cnd.where("wisherId", "=", user.getId())));
		dao.fetchLinks(wantors, "wantor");
		return new QueryResult(wantors, page);
	}

	/**
	 * 获取某一页许愿
	 * 
	 * @param page
	 * @return
	 */
	public QueryResult getWishes(Pager page, User user) {
		List<Wish> wishes = dao.query(Wish.class, Cnd.orderBy().desc("date"),
				page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(Wish.class));

		// 加载分享发表者
		dao.fetchLinks(wishes, "wisher");
		for (Wish wish : wishes) {
			wish.setPraiseNum(getPraiseNumber(wish));// 获取点赞数
			wish.setShareNum(getShareNumber(wish));// 获取分享数
			// 判断用户有没有收藏该分享
			wish.setShared(false);
			wish.setPraised(false);
			if (user != null) {
				// 到时候要判断是否被分享

				if (hasShared(user, wish)) {
					wish.setShared(true);
				}
				if (!okPraise(wish, user)) {
					wish.setPraised(true);
				}
			}
		}
		return new QueryResult(wishes, page);
	}

	// 获取点赞数
	public int getPraiseNumber(Wish wish) {
		return dao.count(WishPraise.class,
				Cnd.where("wishId", "=", wish.getId()));
	}

	// 修改被分享数
	public int getShareNumber(Wish wish) {
		return dao.count(WishShare.class,
				Cnd.where("wishId", "=", wish.getId()));
	}

	/**
	 * @param wish
	 *            被分享的许愿
	 * @return
	 */
	public String shareWish(Wish wish, User sharer, HttpSession session) {
		// 分享到社交网有一定积分奖励
		if (sharer != null
				&& !userService.okMuzhiCoin(sharer,
						MuzhiCoin.MuzhiCoin_for_Share_Wish, session)) {
			return ActionMessage.Not_MuzhiCoin;
		}
		WishShare share = new WishShare();
		share.setDate(DateUtils.now());
		share.setId(NumGenerator.getUuid());
		share.setSharerId(sharer.getId());
		share.setWishId(wish.getId());
		dao.insert(share);
		return ActionMessage.success;
	}

	/**
	 * 获取收藏的愿望的详细内容
	 * 
	 * @param collect
	 *            一个收藏
	 * @return
	 */
	// public WishCollect getDetail(WishCollect collect) {
	// collect = dao.fetch(WishCollect.class, collect.getId());
	// dao.fetchLinks(collect, "collecter");
	// dao.fetchLinks(collect, "wish");
	// dao.fetchLinks(collect.getWish(), "wisher");
	// return collect;
	// }

	// /**
	// * 获取@到自己的点赞类消息
	// *
	// * @param user
	// * 消息接收
	// * @param page
	// * 分页参数
	// * @return
	// */
	// public QueryResult getMyUnreadPraiseReply(User user, Pager page) {
	// // 许愿墙点赞类未读消息
	// List<WishUnreadReply> replys = dao.query(
	// WishUnreadReply.class,
	// Cnd.where("receiverId", "=", user.getId())
	// .and("type", "=", WishUnreadReply.Praise).desc("date"),
	// page);
	// // 保存未读的消息条数
	// if (page == null)
	// page = new Pager();
	// page.setRecordCount(dao.count(
	// WishUnreadReply.class,
	// Cnd.where("receiverId", "=", user.getId())
	// .and("type", "=", WishUnreadReply.Praise)
	// .and("state", "=", WishUnreadReply.Nuread)));
	//
	// // 加载消息发出者
	// dao.fetchLinks(replys, "replier");
	// return new QueryResult(replys, page);
	// }

	// /**
	// * 获取@到自己的收藏类的未读信息
	// *
	// * @param user
	// * 未读信息接受者
	// * @param page
	// * 分页参数
	// * @return
	// */
	// public QueryResult getMyUnreadCollectReply(User user, Pager page) {
	// // 许愿墙收藏类未读信息
	// List<WishUnreadReply> replys = dao
	// .query(WishUnreadReply.class,
	// Cnd.where("receiverId", "=", user.getId())
	// .and("type", "=", WishUnreadReply.Collect)
	// .desc("date"), page);
	// // 保存未读的消息条数
	// if (page == null)
	// page = new Pager();
	// page.setRecordCount(dao.count(
	// WishUnreadReply.class,
	// Cnd.where("receiverId", "=", user.getId())
	// .and("type", "=", WishUnreadReply.Collect)
	// .and("state", "=", WishUnreadReply.Nuread)));
	//
	// // 加载消息发出者
	// dao.fetchLinks(replys, "replier");
	// return new QueryResult(replys, page);
	// }

	// 获取许愿的标题
	// private String getWishTitle(String id) {
	// Wish wish = dao.fetch(Wish.class, id);
	// return wish.getTitle();
	// }

	// 判断这个是否为收藏的分享
	// private boolean okCancelCollect(User collecter, WishCollect collect) {
	// collect = dao.fetch(WishCollect.class, collect.getId());// 获取这条收藏
	// if (collect != null
	// && collect.getCollecterId().equals(collecter.getId())) {
	// Wish from = dao.fetch(Wish.class, collect.getWishId());
	// from.setCollectNum(from.getCollectNum() - 1);// 被收藏数-1
	// dao.update(from);
	// return true;
	// }
	// return false;
	// }

	// 删除点赞记录
	private void deletePraise(Wish wish, User praiser) {
		WishPraise praise = dao.fetch(
				WishPraise.class,
				Cnd.where("wishId", "=", wish.getId()).and("praiserId", "=",
						praiser.getId()));
		if (praise != null)
			dao.delete(praise);
	}

	// 点赞数增减
	// private void changePraiseNumber(Wish wish, int i) {
	// wish = dao.fetch(Wish.class, wish.getId());
	// wish.setPraiseNum(wish.getPraiseNum() + i);
	// dao.update(wish);
	// }

	// 检查是否已点赞
	private boolean okPraise(Wish wish, User praiser) {
		WishPraise praise = dao.fetch(
				WishPraise.class,
				Cnd.where("wishId", "=", wish.getId()).and("praiserId", "=",
						praiser.getId()));
		return praise == null ? true : false;
	}

	// 给分享的发表者发送一条收藏类未读信息
	private void createUnreadCollectReply(User collecter, Wish wish) {
		// if (Strings.isBlank(wish.getTitle())) {
		// wish = dao.fetch(Wish.class, wish.getId());
		// }
		UnreadReply unread = new UnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(wish.getWisherId());
		unread.setReplierId(collecter.getId());
		unread.setState(UnreadReply.Unread);

		unread.setType(UnreadReply.Collect);
		unread.setLinkId(wish.getId());
		// unread.setLinkTitle(wish.getTitle());
		unread.setReplyFrom(UnreadReply.FromWish);
		dao.insert(unread);
	}

	// 给分享的发表者发送一条收藏类未读信息
	private void createUnreadPraiseReply(User praiser, Wish wish) {
		// if (Strings.isBlank(wish.getTitle())) {
		// wish = dao.fetch(Wish.class, wish.getId());
		// }
		UnreadReply unread = new UnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(wish.getWisherId());
		unread.setReplierId(praiser.getId());
		unread.setState(UnreadReply.Unread);

		unread.setType(UnreadReply.Praise);
		unread.setLinkId(wish.getId());
		// unread.setLinkTitle(wish.getTitle());
		unread.setReplyFrom(UnreadReply.FromWish);
		dao.insert(unread);
	}

	// 删除对应的点赞信息
	private void deleteUnreadPraiseReply(User praiser, Wish wish) {
		UnreadReply reply = dao.fetch(
				UnreadReply.class,
				Cnd.where("replierId", "=", praiser.getId())
						.and("linkId", "=", wish.getId())
						.and("type", "=", UnreadReply.Praise)
						.and("replyFrom", "=", UnreadReply.FromWish));
		if (reply != null)
			dao.delete(reply);
	}

	// 删除对应的点赞信息
	private void deleteUnreadCollectReply(User collecter, Wish wish) {
		UnreadReply reply = dao.fetch(
				UnreadReply.class,
				Cnd.where("replierId", "=", collecter.getId())
						.and("shareId", "=", wish.getId())
						.and("type", "=", UnreadReply.Collect)
						.and("replyFrom", "=", UnreadReply.FromWish));
		if (reply != null)
			dao.delete(reply);
	}

	// 检查是否已经分享
	private boolean hasShared(User sharer, Wish wish) {
		int tmp = dao.count(
				WishShare.class,
				Cnd.where("sharerId", "=", sharer.getId()).and("wishId", "=",
						wish.getId()));
		return tmp == 0 ? false : true;
	}
}
