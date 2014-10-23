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
		ActionMessage am = new ActionMessage();
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		if (shareService.publishOrUpdateShare(publisher, share)) {
			am.setMessage("分享发表成功~");
			am.setType(ActionMessage.success);
		}
		return am;
	}

	// 收藏别人的的分享
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object collect(@Param("::share.") Share share,
			@Param("::fromer.") User fromer, HttpSession session) {
		User collecter = (User) session.getAttribute("t_user");
		// User collecter = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		ActionMessage am = new ActionMessage();
		if (shareService.collectShare(collecter, share, fromer)) {
			am.setMessage("收藏成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("已收藏,请不要重复操作~");
			am.setType(ActionMessage.fail);
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
		ActionMessage am = new ActionMessage();
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		if (shareService.praiseShare(share, praiser)) {
			am.setMessage("点赞成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
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

		ActionMessage am = new ActionMessage();
		if (shareService.commentShare(share, commenter, comment,
				fatherCommenter)) {
			am.setMessage("评论成功~");
			am.setType(ActionMessage.success);
		}
		return am;
	}

	// 获取某一页留言
	@At
	@Ok("json")
	public Object list(int pageNum, HttpSession session) {
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

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
	public Object mylist(int pageNum, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		// User user = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试

		// 分页参数
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

		QueryResult result = shareService.getMyShares(user, page);

		// 封装查询结果集
		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}
}
