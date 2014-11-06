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

import com.muzhiliwu.model.Share;
import com.muzhiliwu.model.ShareUnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.model.WishCollect;
import com.muzhiliwu.model.WishPraise;
import com.muzhiliwu.model.WishUnreadReply;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.Integral;
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
	public String publishOrUpdateWish(User publisher, Wish wish,
			HttpSession session) {
		if (Strings.isBlank(wish.getId())) {// 许愿
			long increment = Wish.Wish_Gift.equals(wish.getType()) ? Integral.Integral_For_Publish_Gift_Wish
					: Integral.Integral_For_Publish_No_Gift_Wish;
			if (!userService.okIntegral(publisher, increment, session)) {
				return ActionMessage.Not_Integral;// 不够积分
			}
			wish.setId(NumGenerator.getUuid());
			wish.setDate(DateUtils.now());
			wish.setWisherId(publisher.getId());// 许愿者id
			wish.setState(Wish.Unrealized);// 愿望未实现

			wish.setPraiseNum(0);// 点赞数为0
			wish.setCollectNum(0);// 被收藏数为0
			wish.setShareNum(0);

			dao.insert(wish);
		} else {// 修改愿望
			wish.setDate(DateUtils.now());
			dao.update(wish);
		}
		return ActionMessage.success;
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
	public boolean praiseWish(Wish wish, User praiser) {
		if (okPraise(wish.getId(), praiser.getId())) {// 点赞
			WishPraise praise = new WishPraise();
			praise.setId(NumGenerator.getUuid());
			praise.setDate(DateUtils.now());
			praise.setWishId(wish.getId());// 被点赞愿望的id
			praise.setPraiserId(praiser.getId());// 点赞者的id
			changePraiseNumber(wish, 1);// 点赞数+1

			createUnreadPraiseReply(praiser, wish);// 给许愿者发送点赞类未读信息

			dao.insert(praise);
			return true;
		} else {// 取消点赞
			deletePraise(wish, praiser);// 删除点赞记录
			changePraiseNumber(wish, -1);// 点赞数-1

			deletePraise(wish, praiser);// 删除对应的点赞信息
			return false;
		}
	}

	/**
	 * 被分享,分享数+1
	 * 
	 * @param wish
	 *            被分享的许愿
	 * @return
	 */
	public String shareWish(Wish wish) {
		changeShareNumber(wish, 1);
		return ActionMessage.success;
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
	public String collectWish(User collecter, Wish wish, HttpSession session) {
		if (!userService.okIntegral(collecter,
				Integral.Integral_For_Collect_Wish, session)) {
			return ActionMessage.Not_Integral;// 不够积分
		}
		if (okCollect(collecter, wish)) {
			// 模拟一次收藏
			WishCollect collect = new WishCollect();
			collect.setId(NumGenerator.getUuid());
			collect.setDate(DateUtils.now());
			collect.setCollecterId(collecter.getId());
			collect.setWishId(wish.getId());
			changeCollectNumber(wish, 1);// 被收藏数+1
			dao.insert(collect);

			createUnreadCollectReply(collecter, wish);// 给许愿者发送一个收藏类未读信息
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	/**
	 * 取消愿望收藏
	 * 
	 * @param collecter
	 *            愿望收藏者
	 * @param collect
	 *            一条收藏
	 * @return
	 */
	public boolean cancelCollectWish(User collecter, WishCollect collect) {
		if (okCancelCollect(collecter, collect)) {
			dao.delete(WishCollect.class, collect.getId());
			Wish wish = dao.fetch(Wish.class, collect.getWishId());
			deleteUnreadCollectReply(collecter, wish);// 删除对应的未读信息
			return true;
		}
		return false;
	}

	/**
	 * 获取一个许愿的详细信息
	 * 
	 * @param wish
	 * @return
	 */
	public Wish getDetail(Wish wish, User user) {
		wish = dao.fetch(Wish.class, wish.getId());
		// 还要判断一下是否已经收藏~~~~~~~~~~~~~~~
		wish.setCollected(false);
		// 判断用户有没有收藏该分享
		if (user != null) {
			if (!okCollect(user, wish)) {
				wish.setCollected(true);
			}
		}
		dao.fetchLinks(wish, "wisher");
		dao.fetchLinks(wish, "praises");// 加载点赞信息
		for (int i = 0; i < wish.getPraises().size(); i++) {
			// 加载点赞的点赞者信息
			dao.fetchLinks(wish.getPraises().get(i), "praiser");
		}
		dao.fetchLinks(wish, "collectes");// 获取收藏信息
		for (int i = 0; i < wish.getCollectes().size(); i++) {
			// 获取所有收藏者信息
			dao.fetchLinks(wish.getCollectes().get(i), "collecter");
		}
		// 还有一些详细信息
		// 加载评论
		// 加载评论者的信息
		// 加载父评论者的信息
		return wish;
	}

	/**
	 * 获取我的所有许愿
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	public QueryResult getMyWishes(Pager page, User user) {
		List<Wish> wishes = dao.query(Wish.class,
				Cnd.where("wisherId", "=", user.getId()).desc("date"), page);
		page.setRecordCount(dao.count(Wish.class,
				Cnd.where("wisherId", "=", user.getId())));

		for (int i = 0; i < wishes.size(); i++) {
			dao.fetchLinks(wishes.get(i), "wisher");
			// **********下面是详情~~~~~~~~~~~~~~~~
			// 加载点赞者
			// 加载收藏者
		}
		return new QueryResult(wishes, page);
	}

	/**
	 * 获取我收藏的愿望
	 * 
	 * @param page
	 * @param user
	 * @return
	 */
	public QueryResult getMyCollectWishes(Pager page, User user) {
		List<WishCollect> collectes = dao.query(WishCollect.class,
				Cnd.where("collecterId", "=", user.getId()), page);
		page.setRecordCount(dao.count(WishCollect.class,
				Cnd.where("collecterId", "=", user.getId())));
		for (int i = 0; i < collectes.size(); i++) {
			dao.fetchLinks(collectes.get(i), "collecter");
			dao.fetchLinks(collectes.get(i), "wish");
			dao.fetchLinks(collectes.get(i).getWish(), "wisher");
		}
		return new QueryResult(collectes, page);
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
		page.setRecordCount(dao.count(Wish.class));

		for (int i = 0; i < wishes.size(); i++) {
			// 判断用户有没有收藏该分享
			wishes.get(i).setCollected(false);
			if (user != null) {
				if (!okCollect(user, wishes.get(i))) {
					wishes.get(i).setCollected(true);
				}
			}
			// 加载分享发表者
			dao.fetchLinks(wishes.get(i), "wisher");
		}
		return new QueryResult(wishes, page);
	}

	/**
	 * 获取收藏的愿望的详细内容
	 * 
	 * @param collect
	 *            一个收藏
	 * @return
	 */
	public WishCollect getDetail(WishCollect collect) {
		collect = dao.fetch(WishCollect.class, collect.getId());
		dao.fetchLinks(collect, "collecter");
		dao.fetchLinks(collect, "wish");
		dao.fetchLinks(collect.getWish(), "wisher");
		return collect;
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
		// 许愿墙点赞类未读消息
		List<WishUnreadReply> replys = dao.query(
				WishUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", WishUnreadReply.Praise).desc("date"),
				page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				WishUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", WishUnreadReply.Praise)
						.and("state", "=", WishUnreadReply.Nuread)));
		// 获取相应的联结信息
		for (WishUnreadReply reply : replys) {
			dao.fetchLinks(reply, "replier");// 加载消息发出者
			reply.setWishTitle(getWishTitle(reply.getWishId()));// 获取被点赞分享墙的标题
		}
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
		// 许愿墙收藏类未读信息
		List<WishUnreadReply> replys = dao
				.query(WishUnreadReply.class,
						Cnd.where("receiverId", "=", user.getId())
								.and("type", "=", WishUnreadReply.Collect)
								.desc("date"), page);
		// 保存未读的消息条数
		page.setRecordCount(dao.count(
				WishUnreadReply.class,
				Cnd.where("receiverId", "=", user.getId())
						.and("type", "=", WishUnreadReply.Collect)
						.and("state", "=", WishUnreadReply.Nuread)));
		// 获取相应的联结信息
		for (WishUnreadReply reply : replys) {
			dao.fetchLinks(reply, "replier");// 加载消息发出者
			reply.setWishTitle(getWishTitle(reply.getWishId()));// 获取被收藏分享墙的标题
		}
		return new QueryResult(replys, page);
	}

	// 获取许愿的标题
	private String getWishTitle(String id) {
		Wish wish = dao.fetch(Wish.class, id);
		return wish.getTitle();
	}

	// 判断这个是否为收藏的分享
	private boolean okCancelCollect(User collecter, WishCollect collect) {
		collect = dao.fetch(WishCollect.class, collect.getId());// 获取这条收藏
		if (collect != null
				&& collect.getCollecterId().equals(collecter.getId())) {
			Wish from = dao.fetch(Wish.class, collect.getWishId());
			from.setCollectNum(from.getCollectNum() - 1);// 被收藏数-1
			dao.update(from);
			return true;
		}
		return false;
	}

	// 删除点赞记录
	private void deletePraise(Wish wish, User praiser) {
		WishPraise praise = dao.fetch(
				WishPraise.class,
				Cnd.where("wishId", "=", wish.getId()).and("praiserId", "=",
						praiser.getId()));
		if (praise != null) {
			dao.delete(praise);
		}
	}

	// 点赞数增减
	private void changePraiseNumber(Wish wish, int i) {
		wish = dao.fetch(Wish.class, wish.getId());
		wish.setPraiseNum(wish.getPraiseNum() + i);
		dao.update(wish);
	}

	// 检查是否已点赞
	private boolean okPraise(String wishId, String praiserId) {
		WishPraise praise = dao.fetch(
				WishPraise.class,
				Cnd.where("wishId", "=", wishId).and("praiserId", "=",
						praiserId));
		return praise == null ? true : false;
	}

	// 给分享的发表者发送一条收藏类未读信息
	private void createUnreadCollectReply(User collecter, Wish wish) {
		WishUnreadReply unread = new WishUnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(wish.getWisherId());
		unread.setReplierId(collecter.getId());
		unread.setState(ShareUnreadReply.Nuread);

		unread.setType(ShareUnreadReply.Collect);
		unread.setWishId(wish.getId());
		dao.insert(unread);
	}

	// 给分享的发表者发送一条收藏类未读信息
	private void createUnreadPraiseReply(User praiser, Wish wish) {
		WishUnreadReply unread = new WishUnreadReply();
		unread.setDate(DateUtils.now());
		unread.setId(NumGenerator.getUuid());
		unread.setReceiverId(wish.getWisherId());
		unread.setReplierId(praiser.getId());
		unread.setState(ShareUnreadReply.Nuread);

		unread.setType(ShareUnreadReply.Praise);
		unread.setWishId(wish.getId());
		dao.insert(unread);
	}

	// 删除对应的点赞信息
	private void deleteUnreadPraiseReply(User praiser, Wish wish) {
		WishUnreadReply reply = dao.fetch(
				WishUnreadReply.class,
				Cnd.where("replierId", "=", praiser.getId())
						.and("shareId", "=", wish.getId())
						.and("type", "=", WishUnreadReply.Praise));
		dao.delete(reply);
	}

	// 删除对应的点赞信息
	private void deleteUnreadCollectReply(User collecter, Wish wish) {
		WishUnreadReply reply = dao.fetch(
				WishUnreadReply.class,
				Cnd.where("replierId", "=", collecter.getId())
						.and("shareId", "=", wish.getId())
						.and("type", "=", WishUnreadReply.Collect));
		dao.delete(reply);
	}

	// 修改被收藏数
	private void changeCollectNumber(Wish wish, int increment) {
		wish = dao.fetch(Wish.class, wish.getId());
		wish.setCollectNum(wish.getCollectNum() + increment);
		dao.update(wish);
	}

	// 修改被分享数
	private void changeShareNumber(Wish wish, int increment) {
		wish = dao.fetch(Wish.class, wish.getId());
		wish.setShareNum(wish.getShareNum() + increment);
		dao.update(wish);
	}

	// 检查是否已经收藏
	private boolean okCollect(User collecter, Wish wish) {
		WishCollect tmp = dao.fetch(
				WishCollect.class,
				Cnd.where("collecterId", "=", collecter.getId()).and("wishId",
						"=", wish.getId()));
		return tmp == null ? true : false;
	}
}
