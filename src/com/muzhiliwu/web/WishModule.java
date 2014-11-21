package com.muzhiliwu.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.service.WishService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;
import com.muzhiliwu.utils.MuzhiCoin;

@IocBean
@At("wish")
public class WishModule {
	@Inject
	private Dao dao;// 用于测试而已
	@Inject
	private WishService wishService;
	private static Log log = LogFactory.getLog(WishModule.class);

	// 许愿
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object publish(@Param("::wish.") Wish wish, HttpSession session,
			HttpServletRequest request) {
		User publisher = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (publisher != null ? publisher.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "正在尝试许愿~]");

		ActionMessage am = new ActionMessage();

		if (wish == null
				|| !(Wish.Wish_Common.equals(wish.getType()) && !Wish.Wish_Gift
						.equals(wish.getType()))) {
			am.setType(ActionMessage.fail);
			am.setMessage("您所许愿望类型不是普通愿望和礼物愿望,许愿失败~");
			return am;
		}
		if (Strings.isBlank(wish.getContent())) {
			am.setType(ActionMessage.fail);
			am.setMessage("许愿内容不能为空~");
			return am;
		}

		String result = wishService.publishWish(publisher, wish, session);

		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_MuzhiCoin.equals(result)) {
			am.setMessage("积分不够,不能发表愿望~");
			am.setType(ActionMessage.Not_MuzhiCoin);
			am.setAddMuZhiCoin(MuzhiCoin.MuzhiCoin_For_Publish_No_Gift_Wish);
		}
		return am;
	}

	// 修改愿望
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object update(@Param("::wish.") Wish wish, HttpSession session) {
		User publisher = (User) session.getAttribute("t_user");

		String result = wishService.updateWish(publisher, wish);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望修改成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(result)) {
			am.setMessage("愿望修改失败,您或许不是愿望发表者~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 将愿望分享到社交网站
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object share(@Param("::wish.") Wish wish, HttpSession session,
			HttpServletRequest request) {
		User sharer = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (sharer != null ? sharer.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "将愿望分享到社交网站~]");

		ActionMessage am = new ActionMessage();
		if (wish == null || Strings.isBlank(wish.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("wish.id不能为空~");
			return am;
		}
		String result = wishService.shareWish(wish, sharer, session);
		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望分享成功~");
			am.setAddMuZhiCoin(MuzhiCoin.MuzhiCoin_for_Share_Wish);
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_MuzhiCoin.equals(result)) {
			am.setMessage("分享失败,拇指币不够用~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}

	// 获取愿望的详细信息
	@At
	@Ok("json")
	@POST
	public Object detail(@Param("::wish.") Wish wish, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		ActionMessage am = new ActionMessage();
		am.setMessage("获取愿望的详细信息");
		am.setObject(wishService.getDetail(wish, user));
		am.setType(ActionMessage.success);
		return am;
	}

	// 获取收藏的愿望的详细信息
	// @At
	// @Ok("json")
	// @POST
	// public Object detail(@Param("::collect.") WishShare share) {
	// ActionMessage am = new ActionMessage();
	// am.setMessage("获取愿望的详细信息");
	// am.setObject(wishService.getDetail(share));
	// am.setType(ActionMessage.success);
	// return am;
	// }

	// 取消点赞
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object cancelPraise(@Param("::wish.") Wish wish,
			HttpSession session, HttpServletRequest request) {
		User praiser = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (praiser != null ? praiser.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "尝试取消点赞~]");

		ActionMessage am = new ActionMessage();
		if (wish == null || Strings.isBlank(wish.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("wish.id不能为空~");
			return am;
		}
		String tmp = wishService.cancelPraiseWish(wish, praiser);
		if (ActionMessage.cancel.equals(tmp)) {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞取消失败,您或许还未点赞~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 获取某愿望的点赞数
	@At
	@Ok("json")
	@POST
	public Object getPraiseNum(@Param("::wish.") Wish wish,
			HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取点赞数~]");
		ActionMessage am = new ActionMessage();
		if (wish == null || Strings.isBlank(wish.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("wish.id不能为空~");
			return am;
		}
		am.setType(ActionMessage.success);
		am.setMessage(wish.getId());
		am.setObject(wishService.getPraiseNumber(wish));
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object getShareNum(@Param("::wish.") Wish wish, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取分享数~]");
		ActionMessage am = new ActionMessage();
		if (wish == null || Strings.isBlank(wish.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("wish.id不能为空~");
			return am;
		}
		am.setType(ActionMessage.success);
		am.setMessage(wish.getId());
		am.setObject(wishService.getShareNumber(wish));
		return am;
	}

	// 点赞
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object praise(@Param("::wish.") Wish wish, HttpSession session,
			HttpServletRequest request) {
		User praiser = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (praiser != null ? praiser.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "尝试点赞~]");
		ActionMessage am = new ActionMessage();
		if (wish == null || Strings.isBlank(wish.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("wish.id不能为空~");
			return am;
		}

		String tmp = wishService.praiseWish(wish, praiser, session);
		if (ActionMessage.success.equals(tmp)) {
			am.setMessage("点赞成功~");
			am.setAddMuZhiCoin(MuzhiCoin.MuzhiCoin_For_Praise_Wish);
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞失败,您或许已经点赞~");
			am.setType(ActionMessage.fail);
		} else if (ActionMessage.Not_MuzhiCoin.equals(tmp)) {
			am.setMessage("拇指币不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}

	// 获取某一页许愿
	@At
	@Ok("json")
	@POST
	public Object list(@Param("::page.") Pager page, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "首页获取许愿列表]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = wishService.getWishes(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());

		return ams;
	}

	// 获取想要帮忙实现愿望的人
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myWishWantor(@Param("::page.") Pager page,
			HttpSession session, HttpServletRequest request) {

		User user = (User) session.getAttribute("t_user");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取想要帮我许愿的人]");

		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = wishService.getMyWishWantor(user, page);

		ActionMessages ams = new ActionMessages();

		ams.setMessCount(result.getPager().getPageCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageNum(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());
		return ams;
	}

	// 获取我的待实现许愿
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myUnrealizedWishes(@Param("::page.") Pager page,
			HttpSession session, HttpServletRequest request) {
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		User user = (User) session.getAttribute("t_user");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取我的待实现愿望]");
		QueryResult result = wishService.getMyWishes(page, user,
				Wish.Unrealized);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());

		return ams;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object delete(@Param("::wish.") Wish wish, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "删除我的愿望]");
		ActionMessage am = new ActionMessage();
		if (wish == null || Strings.isBlank(wish.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请求参数wish.id不能为空~");
			return am;
		}
		String resule = wishService.deleteWish(user, wish);
		if (ActionMessage.fail.equals(resule)) {
			am.setType(ActionMessage.fail);
			am.setMessage("删除失败,您或许不是愿望创建者~");
			return am;
		}
		am.setType(ActionMessage.success);
		am.setMessage("愿望删除成功^_^");
		return am;
	}

	// 获取我的待实现许愿
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myRealizedWishes(@Param("::page.") Pager page,
			HttpSession session, HttpServletRequest request) {
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ user.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取我的已实现愿望]");

		QueryResult result = wishService.getMyWishes(page, user, Wish.Realized);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());

		return ams;
	}

	// 获取我收藏的愿望
	// @At
	// @Ok("json")
	// @POST
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object myCollectList(@Param("::page.") Pager page,
	// HttpSession session) {
	// if (page != null)
	// page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
	// .getPageNumber());
	//
	// User user = (User) session.getAttribute("t_user");
	// QueryResult result = wishService.getMyCollectWishes(page, user);
	//
	// ActionMessages ams = new ActionMessages();
	// ams.setMessCount(result.getPager().getRecordCount());
	// ams.setPageNum(result.getPager().getPageNumber());
	// ams.setPageSize(result.getPager().getPageSize());
	// ams.setObject(result.getList());
	//
	// return ams;
	// }

	// 收藏愿望
	// @At
	// @Ok("json")
	// @POST
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object collect(@Param("::wish.") Wish wish, HttpSession session) {
	// User collecter = (User) session.getAttribute("t_user");
	// // User collecter = dao
	// // .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试
	// String result = wishService.collectWish(collecter, wish, session);
	//
	// ActionMessage am = new ActionMessage();
	// if (ActionMessage.Not_MuzhiCoin.equals(result)) {
	// am.setMessage("积分不够,不能收藏~");
	// am.setType(ActionMessage.Not_MuzhiCoin);
	// } else if (ActionMessage.success.equals(result)) {
	// am.setMessage("愿望收藏成功~");
	// am.setType(ActionMessage.success);
	// } else if (ActionMessage.fail.equals(result)) {
	// am.setMessage("该愿望您已经收藏,不能重复收藏~");
	// am.setType(ActionMessage.fail);
	// }
	// return am;
	// }

	// 取消收藏
	// @At
	// @Ok("json")
	// @POST
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object cancelCollect(@Param("::collect.") WishCollect collect,
	// HttpSession session) {
	// User collecter = (User) session.getAttribute("t_user");
	// // User collecter = dao.fetch(User.class,
	// // "360c732435c84ab48ea16fe02b9ba420");// 用来测试
	//
	// ActionMessage am = new ActionMessage();
	// if (wishService.cancelCollectWish(collecter, collect)) {
	// am.setMessage("收藏取消成功~");
	// am.setType(ActionMessage.success);
	// } else {
	// am.setMessage("该收藏不存在~");
	// am.setType(ActionMessage.fail);
	// }
	// return am;
	// }

	// 获取@我的点赞类消息
	// @At
	// @Ok("json")
	// @POST
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object getMyUnreadPraiseReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	// if(page !=null)
	// page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());
	//
	// QueryResult result = wishService.getMyUnreadPraiseReply(user, page);
	//
	// ActionMessages ams = new ActionMessages();
	// ams.setPageCount(result.getPager().getRecordCount());
	// ams.setPageNum(result.getPager().getPageNumber());
	// ams.setPageSize(result.getPager().getPageSize());
	// ams.setObject(result.getList());
	// return ams;
	// }

	// 获取@我的评论类的消息
	// @At
	// @Ok("json")
	// @POST
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object getMyUnreadCollectReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	// if(page !=null)
	// page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());
	//
	// QueryResult result = wishService.getMyUnreadCollectReply(user, page);
	//
	// ActionMessages ams = new ActionMessages();
	// ams.setPageCount(result.getPager().getRecordCount());
	// ams.setPageNum(result.getPager().getPageNumber());
	// ams.setPageSize(result.getPager().getPageSize());
	// ams.setObject(result.getList());
	// return ams;
	// }
}
