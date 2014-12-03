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

import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.listener.CheckShopLoginFilter;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.service.GiftCollectService;
import com.muzhiliwu.service.gift.GiftService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.ActionMessages;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.GiftSearch;
import com.muzhiliwu.utils.IpUtils;
import com.muzhiliwu.utils.MuzhiCoin;

@IocBean
@At("gift")
public class GiftModule {
	@Inject
	private GiftService giftService;
	@Inject
	private GiftCollectService giftCollectService;
	private static Log log = LogFactory.getLog(GiftModule.class);

	// 上传在线商品
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object createOnlineGift(@Param("::gift.") Gift gift,
			HttpSession session, HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (shop != null ? shop.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "尝试上传在线商品]");

		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("参数gift.id不能为空~");
			return am;
		} else if (Strings.isBlank(gift.getBigPic())
				|| Strings.isBlank(gift.getSamllPic())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请上传商品的大图和小图~");
			return am;
		} else if (Strings.isBlank(gift.getName())
				|| Strings.isBlank(gift.getGiftTag())
				|| Strings.isBlank(gift.getUrl())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请完善: 商品名称/商品标签/商品完整链接");
			return am;
		} else if (gift.getPrice() == null || Strings.isBlank(gift.getType())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请完善:商品价格/商品分类");
			return am;
		} else if (Strings.isBlank(gift.getSuitAgeGroup())
				|| Strings.isBlank(gift.getSuitSex())
				|| Strings.isBlank(gift.getSuitStar())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请完善:商品适合年龄段/商品适合性别/商品适合星座");
			return am;
		} else if (Strings.isBlank(gift.getDescript())) {
			am.setType(ActionMessage.fail);
			am.setMessage("请完善:商品描述");
			return am;
		}
		giftService.createOnlineGift(shop, gift);
		am.setType(ActionMessage.success);
		am.setMessage("商品上传成功^_^");
		return null;
	}

	// 生成一个礼品id
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	public Object gainGiftId(HttpSession session, HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (shop != null ? shop.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "尝试生成一个礼品id~]");

		ActionMessage am = new ActionMessage();
		am.setType(ActionMessage.success);
		am.setObject(giftService.gainId());
		return am;
	}

	// 上传商品大图
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object uploadGiftPic(@Param("pic") TempFile tfs,
			@Param("::gift.") Gift gift, ServletContext context,
			HttpSession session, HttpServletRequest request) {

		Shop shop = (Shop) session.getAttribute("s_shop");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (shop != null ? shop.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "商品礼品大图]");

		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		String result = giftService.uploadPic(gift, tfs,
				context.getRealPath("/"));

		am.setMessage("商品图片上传成功^_^");
		am.setType(ActionMessage.success);
		am.setObject(result);// 返回图片存储路径
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckShopLoginFilter.class, args = { "ioc:checkShopLoginFilter" }))
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object cutGiftPic(@Param("::gift.") Gift gift, String picPath,
			int top, int left, int width, int height, int smallWidth,
			int smallHeight, ServletContext context, HttpSession session,
			HttpServletRequest request) {
		Shop shop = (Shop) session.getAttribute("s_shop");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (shop != null ? shop.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "商品礼品大图]");

		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		if (Strings.isBlank(picPath)) {
			am.setType(ActionMessage.fail);
			am.setMessage("您还没选择商品图片~");
			return am;
		}

		String result = giftService.cutGiftPic(gift, picPath, top, left, width,
				height, smallWidth, smallHeight, context.getRealPath("/"));

		am.setMessage("商品图片上传成功^_^");
		am.setType(ActionMessage.success);
		am.setObject(result);
		return am;
	}

	// ***************************************上面属于商家*********下面属于普通商户*************************

	// **********************************************************************************************
	// 首页获取礼物商品列表
	@At
	@Ok("json")
	@POST
	public Object list(@Param("::page.") Pager page,
			@Param("::giftSearch.") GiftSearch giftSearch, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");

		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取礼物商品列表]");
		if (page != null)
			page.setPageNumber(page.getPageNumber() <= 0 ? 1 : page
					.getPageNumber());

		QueryResult result = giftService.getGiftList(user, page, giftSearch);

		ActionMessages ams = new ActionMessages();
		ams.setMessCount(result.getPager().getRecordCount());
		ams.setPageNum(result.getPager().getPageNumber());
		ams.setPageSize(result.getPager().getPageSize());
		ams.setPageCount((int) Math.ceil((double) result.getPager()
				.getRecordCount() / (double) result.getPager().getPageSize()));
		ams.setObject(result.getList());
		return ams;
	}

	// 获取某条礼品商品的被收藏数
	@At
	@Ok("json")
	@POST
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

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object cancelCollect(@Param("::gift.") Gift gift,
			HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "取消收藏某商品~]");

		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		giftCollectService.cancelCollect(user, gift);
		am.setType(ActionMessage.success);
		am.setMessage("收藏取消成功~");
		return am;
	}

	// 收藏礼品
	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object collect(@Param("::gift.") Gift gift, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "被收藏商品~]");
		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		String tmp = giftCollectService.collectGift(user, gift, session);
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

	// 获取某礼品商品分享数
	@At
	@Ok("json")
	@POST
	public Object getShareNum(@Param("::gift.") Gift gift, HttpSession session,
			HttpServletRequest request) {
		User user = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (user != null ? user.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "获取分享数~]");
		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		am.setType(ActionMessage.success);
		am.setMessage(gift.getId());
		am.setObject(giftService.getGiftShareNum(gift));
		return am;
	}

	@At
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object share(@Param("::gift.") Gift gift, HttpSession session,
			HttpServletRequest request) {
		User sharer = (User) session.getAttribute("t_user");
		log.info("[ip:" + IpUtils.getIpAddr(request) + "]  [用户:"
				+ (sharer != null ? sharer.getCode() : "游客") + "]  [时间:"
				+ DateUtils.now() + "]  [操作:" + "将愿望分享到社交网站~]");

		ActionMessage am = new ActionMessage();
		if (gift == null || Strings.isBlank(gift.getId())) {
			am.setType(ActionMessage.fail);
			am.setMessage("gift.id不能为空~");
			return am;
		}
		String result = giftService.shareGift(gift, sharer, session);
		if (ActionMessage.success.equals(result)) {
			am.setMessage("礼品分享成功~");
			am.setAddMuZhiCoin(MuzhiCoin.MuzhiCoin_for_Share_Wish);
			am.setType(ActionMessage.success);
		} else if (ActionMessage.Not_MuzhiCoin.equals(result)) {
			am.setMessage("分享失败,拇指币不够用~");
			am.setType(ActionMessage.Not_MuzhiCoin);
		}
		return am;
	}
}
