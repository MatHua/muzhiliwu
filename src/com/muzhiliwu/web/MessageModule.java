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
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "7f74f863a1474866ac1151bc179c60ab");// 用来测试

		String result = messageService.publishMessage(publisher, msg, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {// 发表成功
			am.setMessage("留言发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_MuzhiCoin.equals(result)) {// 积分不够
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}

	// 更新一条留言
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object update(@Param("::msg.") Message msg, HttpSession session) {
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "7f74f863a1474866ac1151bc179c60ab");// 用来测试

		String result = messageService.updateMessage(publisher, msg);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {// 发表成功
			am.setMessage("留言修改成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(result)) {
			am.setMessage("留言修改失败,您或许不是留言发表者~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 获取某一页留言
	@At
	@Ok("json")
	public Object list(@Param("::page.") Pager page) {
		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());
		// page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

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
	public Object mylist(@Param("::page.") Pager page, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		// User user = dao.fetch(User.class,
		// "7f74f863a1474866ac1151bc179c60ab");// 用来测试

		page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());

		QueryResult result = messageService.getMyMessages(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object cancelPraise(@Param("::msg.") Message msg, HttpSession session) {
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "7f74f863a1474866ac1151bc179c60ab");// 用来测试

		String tmp = messageService.cancelPraiseMessage(msg, praiser);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.cancel.equals(tmp)) {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞取消失败,也许您未曾点赞");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 点赞
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object praise(@Param("::msg.") Message msg, HttpSession session) {

		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "7f74f863a1474866ac1151bc179c60ab");// 用来测试

		String tmp = messageService.praiseMessage(msg, praiser, session);
		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(tmp)) {
			am.setMessage("点赞成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(tmp)) {
			am.setMessage("点赞失败,请不要重复点赞~");
			am.setType(ActionMessage.fail);
		} else if (ActionMessage.Not_MuzhiCoin.equals(tmp)) {
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
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
		User commenter = (User) session.getAttribute("t_user");
		// User commenter = dao.fetch(User.class,
		// "7f74f863a1474866ac1151bc179c60ab");// 用来测试
		String result = messageService.commentMessage(msg, commenter, comment,
				fatherCommenter, session);

		ActionMessage am = new ActionMessage();
		if (ActionMessage.success.equals(result)) {
			am.setMessage("评论成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_MuzhiCoin.equals(result)) {
			am.setMessage("积分不够~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}

	// 获取留言的详细信息
	@At
	@Ok("json")
	public Object detail(@Param("::msg.") Message msg, HttpSession session) {
		User user = (User) session.getAttribute("t_user");
		ActionMessage am = new ActionMessage();
		am.setMessage("获取留言的详细信息~");
		am.setObject(messageService.getDetails(msg, user));
		am.setType(ActionMessage.success);
		return am;
	}

	// 获取@我的点赞类未读信息消息
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
	// QueryResult result = messageService.getMyUnreadPraiseReply(user, page);
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
	// public Object getMyUnreadCommentReply(@Param("::page.") Pager page,
	// HttpSession session) {
	// User user = (User) session.getAttribute("t_user");
	//
	// page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page.getPageNumber());
	//
	// QueryResult result = messageService.getMyUnreadCommentReply(user, page);
	//
	// ActionMessages ams = new ActionMessages();
	// ams.setPageCount(result.getPager().getRecordCount());
	// ams.setPageNum(result.getPager().getPageNumber());
	// ams.setPageSize(result.getPager().getPageSize());
	// ams.setObject(result.getList());
	// return ams;
	// }
}
