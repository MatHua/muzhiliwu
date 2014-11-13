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

import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.model.GiftCollect;
import com.muzhiliwu.model.GiftCollectComment;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.service.GiftCollectService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;

@IocBean
@At("giftCollect")
public class GiftCollectModule {
	@Inject
	private GiftCollectService giftCollectService;
	@Inject
	private Dao dao;// 仅用于测试

	// 收藏一件礼品商品
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object collect(@Param("::gift.") Gift gift,
			@Param("::collect.") GiftCollect collect, HttpSession session) {
		User collector = (User) session.getAttribute("t_user");
		String tmp = giftCollectService.collectGift(collector, gift, collect,
				session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.Not_MuzhiCoin.equals(tmp)) {
			am.setMessage("积分不够,收藏失败~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		} else if (ActionMessage.success.equals(tmp)) {
			am.setMessage("收藏成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("该礼品您已收藏,不能重复收藏~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 评论一个礼品收藏
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object comment(@Param("::collect.") GiftCollect collect,
			@Param("::comment.") GiftCollectComment comment,
			@Param("::fatherCommenter.") User fatherCommenter,
			HttpSession session) {
		User commenter = (User) session.getAttribute("t_user");

		String tmp = giftCollectService.commentGiftCollect(collect, commenter,
				comment, fatherCommenter, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.Not_MuzhiCoin.equals(tmp)) {
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		} else if (ActionMessage.success.equals(tmp)) {
			am.setMessage("评论成功~");
			am.setType(ActionMessage.success);
		}
		return am;
	}

	// 点赞一个礼品收藏
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object praise(@Param("::collect.") GiftCollect collect,
			HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		String tmp = giftCollectService.praiseGiftCollect(praiser, collect,
				session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.Not_MuzhiCoin.equals(tmp)) {
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		} else if (ActionMessage.success.equals(tmp)) {
			am.setMessage("点赞成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞失败,您或许已经点赞~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object cancelPraise(@Param("::collect.") GiftCollect collect,
			HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		String tmp = giftCollectService.cancelPraiseGiftCollect(praiser,
				collect);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.cancel.equals(tmp)) {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞取消失败,您或许未点赞~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 获取@我的点赞类未读信息消息
	// @At
	// @Ok("json")
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object getMyUnreadPraiseReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	// if(page !=null)
	// page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());
	//
	// QueryResult result = giftCollectService.getMyUnreadPraiseReply(user,
	// page);
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
	// @Filters(@By(type = CheckLoginFilter.class, args = {
	// "ioc:checkLoginFilter" }))
	// public Object getMyUnreadCommentReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	// if(page !=null)
	// page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());
	//
	// QueryResult result = giftCollectService.getMyUnreadCommentReply(user,
	// page);
	//
	// ActionMessages ams = new ActionMessages();
	// ams.setPageCount(result.getPager().getRecordCount());
	// ams.setPageNum(result.getPager().getPageNumber());
	// ams.setPageSize(result.getPager().getPageSize());
	// ams.setObject(result.getList());
	// return ams;
	// }

	// 获取我的礼品收藏
	@At
	@Ok("json")
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object mylist(@Param("::page.") Pager page, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = giftCollectService.getMyGiftCollects(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 获取某一页礼品收藏
	@At
	@Ok("json")
	public Object mylist(@Param("::page.") Pager page) {
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = giftCollectService.getGiftCollects(page);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	@At
	@Ok("json")
	public Object detail(@Param("::collect.") GiftCollect collect,
			HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		ActionMessage am = new ActionMessage();
		am.setMessage("获取留言的详细信息~");
		am.setObject(giftCollectService.getDetails(collect, user));
		am.setType(ActionMessage.success);
		return am;
	}
}
