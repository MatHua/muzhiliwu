package com.muzhiliwu.web;

import java.awt.Desktop.Action;

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

import com.muzhiliwu.model.Share;
import com.muzhiliwu.model.ShareComment;
import com.muzhiliwu.model.User;
import com.muzhiliwu.service.ShareService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;

@IocBean
@At("share")
public class ShareModule {
	@Inject
	private ShareService shareService;
	@Inject
	private Dao dao;// 用于测试而已

	// 发表一条留言
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object publish(@Param("::share.") Share share, HttpSession session) {
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = shareService.publishShare(publisher, share, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("分享发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("积分不够,不能发表~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}

	// 更新留言信息
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object update(@Param("::share.") Share share, HttpSession session) {
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = shareService.updateShare(publisher, share);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("留言更改成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(result)) {
			am.setMessage("留言更改失败,请确保您是留言的发表者");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 收藏别人的的分享
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object collect(@Param("::share.") Share share, HttpSession session) {
		User collecter = (User) session.getAttribute("t_user");
		// User collecter = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = shareService.collectShare(collecter, share, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("收藏成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(result)) {
			am.setMessage("已收藏,请不要重复操作~");
			am.setType(ActionMessage.fail);
		} else if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("积分不够,不能收藏~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}

	// 取消收藏
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object cancelCollect(@Param("::share.") Share share,
			HttpSession session) {
		User collecter = (User) session.getAttribute("t_user");
		// User collecter = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		ActionMessage am = new ActionMessage();
		if (shareService.cancelCollectShare(collecter, share)) {
			am.setMessage("收藏取消成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("该收藏不存在~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 点赞或取消点赞
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object praise(@Param("::share.") Share share, HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		String tmp = shareService.praiseShare(share, praiser);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(tmp)) {
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
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object cancelPraise(@Param("::share.") Share share,
			HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		String tmp = shareService.cancelPraiseShare(share, praiser);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.cancel.equals(tmp)) {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞取消失败,您或许未曾点赞~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 评论一个分享或者评论别人的评论
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object comment(@Param("::share.") Share share,
			@Param("::comment.") ShareComment comment,
			@Param("::fatherCommenter.") User fatherCommenter,
			HttpSession session) {
		User commenter = (User) session.getAttribute("t_user");
		// User commenter = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		String result = shareService.commentShare(share, commenter, comment,
				fatherCommenter, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("评论成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("评论失败,积分不够~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}

	// 获取某一页留言
	@At
	@Ok("json")
	public Object list(@Param("::page.") Pager page, HttpSession session) {
		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		User user = (User) session.getAttribute("t_user");
		QueryResult result = shareService.getShares(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 获取用户的留言
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object mylist(@Param("::page.") Pager page, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		// User user = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		// 分页参数
		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		QueryResult result = shareService.getMyShares(user, page);

		// 封装查询结果集
		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 获取分享详细内容
	@At
	@Ok("json")
	public Object detail(@Param("::share.") Share share, HttpSession session) {
		User user = (User) session.getAttribute("t_user");

		ActionMessage am = new ActionMessage();
		am.setMessage("获取分享详细内容~");
		am.setObject(shareService.getDetail(share, user));
		am.setType(ActionMessage.success);
		return am;
	}

	// 获取@我的点赞类消息
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object getMyUnreadPraiseReply(@Param("::page.") Pager page,
			HttpSession session) {
		User user = (User) session.getAttribute("t_user");

		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		QueryResult result = shareService.getMyUnreadPraiseReply(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 获取@我的评论类的消息
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object getMyUnreadCommentReply(@Param("::page.") Pager page,
			HttpSession session) {
		User user = (User) session.getAttribute("t_user");

		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		QueryResult result = shareService.getMyUnreadCommentReply(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 获取@我的评论类的消息
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object getMyUnreadCollectReply(@Param("::page.") Pager page,
			HttpSession session) {
		User user = (User) session.getAttribute("t_user");

		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		QueryResult result = shareService.getMyUnreadCollectReply(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}
}
