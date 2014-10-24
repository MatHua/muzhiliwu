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

import com.muzhiliwu.model.MessComment;
import com.muzhiliwu.model.Message;
import com.muzhiliwu.model.User;
import com.muzhiliwu.service.MessageService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;

@IocBean
@At("mess")
public class MessageModule {
	@Inject
	private MessageService messageService;
	@Inject
	private Dao dao;// 用于测试而已

	// 发表一条留言
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object publish(@Param("::msg.") Message msg, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");//用来测试

		String result = messageService.publishOrUpdateMessage(publisher, msg,
				session);
		if (ActionMessage.success.equals(result)) {// 发表成功
			am.setMessage("留言发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_Integral.equals(result)) {// 积分不够
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}

	// 获取某一页留言
	@At
	@Ok("json")
	public Object list(int pageNum) {
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

		QueryResult result = messageService.getMessages(page);

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
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

		QueryResult result = messageService.getMyMessages(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 点赞或取消点赞
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object praise(@Param("::msg.") Message msg, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		if (messageService.praiseMessage(msg, praiser)) {
			am.setMessage("点赞成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		}
		return am;
	}

	// 评论留言或者评论别人的评论
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object comment(@Param("::msg.") Message msg,
			@Param("::comment.") MessComment comment,
			@Param("::fatherCommenter.") User fatherCommenter,
			HttpSession session) {
		ActionMessage am = new ActionMessage();
		User commenter = (User) session.getAttribute("t_user");
		// User commenter = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = messageService.commentMessage(msg, commenter, comment,
				fatherCommenter, session);
		if (ActionMessage.success.equals(result)) {
			am.setMessage("评论成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}
}
