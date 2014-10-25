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

	// 许愿~已测试
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object publish(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.publishOrUpdateWish(publisher, wish,
				session);

		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望发表成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("积分不够,不能发表愿望~");
			am.setType(ActionMessage.Not_Integral);
		}
		return am;
	}

	// 修改愿望~已测试
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object update(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User publisher = (User) session.getAttribute("t_user");
		// User publisher = dao.fetch(User.class,
		// "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.publishOrUpdateWish(publisher, wish,
				session);
		if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望修改成功~");
			am.setType(ActionMessage.success);
		}
		return am;
	}

	// 获取愿望的详细信息~已测试
	@At
	@Ok("json")
	public Object detail(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		am.setMessage("获取愿望的详细信息");
		User user = (User) session.getAttribute("t_user");
		am.setObject(wishService.getDetail(wish, user));
		am.setType(ActionMessage.success);
		return am;
	}

	// 获取收藏的愿望的详细信息~已测试
	@At
	@Ok("json")
	public Object detail(@Param("::collect.") WishCollect collect) {
		ActionMessage am = new ActionMessage();
		am.setMessage("获取愿望的详细信息");
		am.setObject(wishService.getDetail(collect));
		am.setType(ActionMessage.success);
		return am;
	}

	// 点赞或取消点赞~已测试
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object praise(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User praiser = (User) session.getAttribute("t_user");
		// User praiser = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		if (wishService.praiseWish(wish, praiser)) {
			am.setMessage("点赞成功~");
			am.setType(ActionMessage.success);
		} else {
			am.setMessage("点赞取消成功~");
			am.setType(ActionMessage.cancel);
		}
		return am;
	}

	// 获取某一页许愿
	@At
	@Ok("json")
	public Object list(int pageNum, HttpSession session) {
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

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
	public Object mylist(int pageNum, HttpSession session) {
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

		User user = (User) session.getAttribute("t_user");
		QueryResult result = wishService.getMyWishes(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());

		return ams;
	}
	
	//获取我收藏的愿望
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object myCollectList(int pageNum, HttpSession session) {
		Pager page = new Pager();
		pageNum = (pageNum <= 0) ? 1 : pageNum;
		page.setPageNumber(pageNum);
		page.setPageSize(Pager.DEFAULT_PAGE_SIZE);

		User user = (User) session.getAttribute("t_user");
		QueryResult result = wishService.getMyCollectWishes(page, user);

		ActionMessages ams = new ActionMessages();
		ams.setPageCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());

		return ams;
	}

	// 收藏愿望~已测试
	@At
	@Ok("json")
	@Filters(@By(type = CheckSession.class, args = { "t_user", "/login.jsp" }))
	public Object collect(@Param("::wish.") Wish wish, HttpSession session) {
		ActionMessage am = new ActionMessage();
		User collecter = (User) session.getAttribute("t_user");
		// User collecter = dao
		// .fetch(User.class, "360c732435c84ab48ea16fe02b9ba420");// 用来测试
		String result = wishService.collectWish(collecter, wish, session);
		if (ActionMessage.Not_Integral.equals(result)) {
			am.setMessage("积分不够,不能收藏~");
			am.setType(ActionMessage.Not_Integral);
		} else if (ActionMessage.success.equals(result)) {
			am.setMessage("愿望收藏成功~");
			am.setType(ActionMessage.success);
		} else if (ActionMessage.fail.equals(result)) {
			am.setMessage("该愿望您已经收藏,不能重复收藏~");
			am.setType(ActionMessage.fail);
		}
		return am;
	}

	// 取消收藏~已测试
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
}
