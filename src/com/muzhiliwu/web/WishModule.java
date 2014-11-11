package com.muzhiliwu.web;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.model.WishCollect;
import com.muzhiliwu.service.WishService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;

@IocBean
@At("wish")
public class WishModule {
	@Inject
	private WishService wishService;
	@Inject
	private Dao dao;// 用于测试而已

	// 许愿
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object publish(@Param("::wish.") Wish wish, HttpSession session) {

		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.publishWish(publisher, wish, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_MuzhiCoin.equals(result)) {
			am.setMessage("积分不够,不能发表愿望~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}

	// 修改愿望
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object update(@Param("::wish.") Wish wish, HttpSession session) {
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试

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
	public Object share(@Param("::wish.") Wish wish, HttpSession session) {
		User sharer = (User) session.getAttribute("t_user");
		String result = wishService.shareWish(wish, sharer, session);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望分享成功~");
			am.setType(ActionMessage.success);
		}
		return am;
	}

	// 获取愿望的详细信息
	@At
	@Ok("json")
	public Object detail(@Param("::wish.") Wish wish, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		ActionMessage am = new ActionMessage();
		am.setMessage("获取愿望的详细信息");
		am.setObject(wishService.getDetail(wish, user));
		am.setType(ActionMessage.success);
		return am;
	}

	// 获取收藏的愿望的详细信息
	@At
	@Ok("json")
	public Object detail(@Param("::collect.") WishCollect collect) {
		ActionMessage am = new ActionMessage();
		am.setMessage("获取愿望的详细信息");
		am.setObject(wishService.getDetail(collect));
		am.setType(ActionMessage.success);
		return am;
	}

	public Object cancelPraise(@Param("::wish.") Wish wish, HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		String tmp = wishService.cancelPraiseWish(wish, praiser);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.cancel.equals(tmp)) {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞取消失败,您或许还未点赞~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 点赞或取消点赞
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object praise(@Param("::wish.") Wish wish, HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		String tmp = wishService.praiseWish(wish, praiser, session);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(tmp)) {
			am.setMessage("点赞成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞失败,您或许已经点赞~");
			am.setType(ActionMessage.fail);
		} else if (ActionMessage.Not_MuzhiCoin.equals(tmp)) {
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}

	// 获取某一页许愿
	@At
	@Ok("json")
	public Object list(@Param("::page.") Pager page, HttpSession session) {
		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		User user = (User) session.getAttribute("t_user");
		QueryResult result = wishService.getWishes(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());

		return ams;
	}

	// 获取我的许愿
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object mylist(@Param("::page.") Pager page, HttpSession session) {
		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		User user = (User) session.getAttribute("t_user");
		QueryResult result = wishService.getMyWishes(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());

		return ams;
	}

	// 获取我收藏的愿望
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object myCollectList(@Param("::page.") Pager page,
			HttpSession session) {
		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		User user = (User) session.getAttribute("t_user");
		QueryResult result = wishService.getMyCollectWishes(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());

		return ams;
	}

	// 收藏愿望
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object collect(@Param("::wish.") Wish wish, HttpSession session) {
		User collecter = (User) session.getAttribute("t_user");
		// User collecter = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.collectWish(collecter, wish, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.Not_MuzhiCoin.equals(result)) {
			am.setMessage("积分不够,不能收藏~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		} else if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望收藏成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(result)) {
			am.setMessage("该愿望您已经收藏,不能重复收藏~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 取消收藏
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object cancelCollect(@Param("::collect.") WishCollect collect,
			HttpSession session) {
		User collecter = (User) session.getAttribute("t_user");
		// User collecter = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		ActionMessage am = new ActionMessage();
		if (wishService.cancelCollectWish(collecter, collect)) {
			am.setMessage("收藏取消成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("该收藏不存在~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 获取@我的点赞类消息
	// @At
	// @Ok("json")
	// @Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp"
	// }))
	// public Object getMyUnreadPraiseReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	//
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
	// @Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp"
	// }))
	// public Object getMyUnreadCollectReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	//
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
