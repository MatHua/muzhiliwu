package com.muzhiliwu.web.gift;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.service.gift.GiftService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;

@IocBean
@At("gift")
public class GiftModule {
	@Inject
	private GiftService giftService;
	private static Log log = LogFactory.getLog(GiftModule.class);

	// 首页获取礼物商品列表
	@At
	@Ok("json")
	public Object list(@Param("::page.") Pager page, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取礼物商品列表]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = giftService.getGiftList(user, page);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setObject(result.getList());
		return ams;
	}

	// 获取某条礼品商品的被收藏数
	@At
	@Ok("json")
	public Object collectNum(@Param("::gift.") Gift gift, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取某商品的被收藏数~]");
		
		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		am.setType(ActionMessage.success);
		am.setMessage(gift.getId());
		am.setObject(giftService.getGiftCollectNum(gift));
		return am;
	}
}
