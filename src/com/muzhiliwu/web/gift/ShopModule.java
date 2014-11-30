package com.muzhiliwu.web.gift;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.muzhiliwu.listener.CheckShopLoginFilter;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.service.gift.ShopService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.IpUtils;

@IocBean
@At("shop")
public class ShopModule {
	@Inject
	private ShopService shopService;
	private static Log log = LogFactory.getLog(ShopModule.class);

	// 获取商家详细信息
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object myDetail(HttpSession session, HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [商家:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取个人详细信息]");
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(shopService.getMyDetail(shop));
		return am;
	}

	// 获取商家基本信息
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object me(HttpSession session, HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [商家:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "获取个人基本信息]");
		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(shopService.me(shop));
		return am;
	}

	// 上传商家logo
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object uploadShopLogo(@Param("userpic") TempFile tfs,
			ServletContext context, HttpSession session,
			HttpServletRequest request) {

		Shop shop = (Shop) session.getAttribute("s_shop");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [商家:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "上传头像]");

		ActionMessage am = new ActionMessage();
		if (tfs == null) {
			am.setType(ActionMessage.fail);
			am.setMessage("您还没选择logo~");
			return am;
		}
		boolean result = shopService.uploadPhoto(shop.getCode(), tfs,
				context.getRealPath("/"));
		if (result) {
			am.setMessage("logo上传成功^_^");
			am.setType(ActionMessage.success);
		}
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object editSelf(@Param("::shop.") Shop shop, HttpSession session,
			HttpServletRequest request) {
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "尝试修改商家资料~]");
		ActionMessage am = new ActionMessage();
		shop.setId(((Shop) session.getAttribute("s_shop")).getId());
		String tmp = shopService.editShop(shop);
		am.setType(ActionMessage.success);
		am.setMessage("商家信息修改成功~");
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object deleteMyGifts(String ids, HttpSession session,
			HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "删除自己的商品~]");

		ActionMessage am = new ActionMessage();
		if (Strings.isBlank(ids)) {
			am.setType(ActionMessage.fail);
			am.setMessage("您未选中删除任何商品~_~");
			return am;
		}
		shopService.deleteMyGifts(shop, ids);
		am.setType(ActionMessage.success);
		am.setMessage("商品删除成功^_^");
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object setGiftNotSale(String ids, HttpSession session,
			HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "下架自己的商品~]");

		ActionMessage am = new ActionMessage();
		if (Strings.isBlank(ids)) {
			am.setType(ActionMessage.fail);
			am.setMessage("您未选中下架任何商品~_~");
			return am;
		}
		shopService.setGiftNotSale(shop, ids);
		am.setType(ActionMessage.success);
		am.setMessage("商品下架成功^_^");
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object myGiftList(String orderBy, @Param("::page.") Pager page,
			HttpSession session, HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ shop.getCode() + "]  [时间:" + DateUtils.now() + "]  [操作:"
				+ "商家获取商品列表~]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());
		QueryResult result = shopService.getMyGiftList(shop, page, orderBy);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());
		return ams;
	}
}
