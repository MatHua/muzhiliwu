package com.muzhiliwu.service.gift;

import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.mvc.upload.TempFile;

import com.muzhiliwu.model.GiftCollect;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.GiftShare;
import com.muzhiliwu.model.gift.GiftTag;
import com.muzhiliwu.model.gift.OrderForm;
import com.muzhiliwu.model.gift.OrderFormForWish;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.model.gift.Tag;
import com.muzhiliwu.service.GiftCollectService;
import com.muzhiliwu.service.UserService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.FileFilter;
import com.muzhiliwu.utils.GiftSearch;
import com.muzhiliwu.utils.MuzhiCoin;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class GiftService {
	@Inject
	private Dao dao;
	@Inject
	private UserService userService;
	@Inject
	private GiftCollectService giftCollectService;

	/**
	 * 上传一件线上商品
	 * 
	 * @param shop
	 * @param gift
	 * @return
	 */
	public String createOnlineGift(Shop shop, Gift gift) {
		shop = dao.fetch(Shop.class, shop.getId());

		gift.setAuditState(Gift.Auditing);
		gift.setDate(DateUtils.now());
		gift.setDelete(false);
		gift.setFromType(Gift.FromOnlineShop);
		gift.setSaleState(Gift.OnSale);

		gift.setShopState(shop.getBusinessState());// 对应商家是否在营业的
		gift.setShopOnBusiness(shop.getOkBusiness());// 对应商家是否可营业

		gift.setShopId(shop.getId());
		dao.insert(gift);
		for (String tag : gift.getGiftTag().split(",")) {
			Tag tmp = dao.fetch(Tag.class, Cnd.where("name", "=", tag));
			if (tmp == null) {
				tmp = new Tag();
				tmp.setCreatorId(shop.getId());
				tmp.setDate(DateUtils.now());
				tmp.setId(NumGenerator.getUuid());
				tmp.setName(tag);
				tmp.setSearchCount(0);
				dao.insert(tmp);
			}
			GiftTag giftTag = new GiftTag();
			giftTag.setDate(DateUtils.now());
			giftTag.setGiftId(gift.getId());
			giftTag.setId(NumGenerator.getUuid());
			giftTag.setTagId(tmp.getId());
			dao.insert(giftTag);
		}
		return ActionMessage.success;
	}

	/**
	 * 上传礼品图片
	 * 
	 * @param gift
	 *            礼品
	 * @param tfs
	 *            图片
	 * @param template
	 *            存储路径
	 * @param flag
	 *            是否是大图
	 * @return
	 */
	public String uploadPic(Gift gift, TempFile tfs, String template,
			boolean flag) {

		// template += "/WEB-INF/giftPic/";
		template += "/giftPic/";

		// 如果对应的文件夹不存在,就创建该文件夹
		Files.createDirIfNoExists(template);

		// 获取文件后缀名
		int beginIndex = tfs.getFile().getAbsolutePath().lastIndexOf(".");
		String fileExt = tfs.getFile().getAbsolutePath().substring(beginIndex);

		// 删除原来存在的图
		String regx = gift.getId() + (flag ? "_big" : "_small") + ".*";
		File f2 = new File(template);
		File[] s = f2.listFiles(new FileFilter(regx));
		for (File file : s) {
			Files.deleteFile(file);
		}

		// 上传新的图
		File f = new File(template + gift.getId() + (flag ? "_big" : "_small")
				+ fileExt);
		try {
			Files.move(tfs.getFile(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/giftPic/" + gift.getId() + (flag ? "_big" : "_small")
				+ fileExt;

	}

	/**
	 * 获取一个id
	 * 
	 * @return
	 */
	public String gainId() {
		return NumGenerator.getUuid();
	}

	/**
	 * 获取礼物商品列表
	 * 
	 * @param user
	 *            已登录的用户
	 * @param page
	 *            分页参数
	 * @return
	 */
	public QueryResult getGiftList(User user, Pager page, GiftSearch giftSearch) {
		String all = "皆可";
		Criteria cri = Cnd.cri();
		cri.where().and("auditState", "=", Gift.AuditSuccess)
		// 审核通过
				.and("isDelete", "=", false)// 没被删除
				.and("saleState", "=", Gift.OnSale)// 在售中(还没下架)
				.and("okBusiness", "=", Shop.CanBusiness)// 而且是没被禁止营业
				.and("shopState", "=", Shop.ShopOpen)// 商家店营业中
		;
		if (giftSearch != null) {
			if (giftSearch.getFromType() != null
					&& !all.equals(giftSearch.getFromType())) {// 礼品来源
				cri.where().and("fromType", "=", giftSearch.getFromType());
			}
			if (giftSearch.getType() != null
					&& !all.equals(giftSearch.getType())) {// 礼品类型
				cri.where().and("type", "=", giftSearch.getType());
			}
			if (giftSearch.getSuitSex() != null
					&& !all.equals(giftSearch.getSuitSex())) {// 适合性别
				cri.where().and("suitSex", "=", giftSearch.getSuitSex());
			}
			if (giftSearch.getSuitStar() != null
					&& !all.equals(giftSearch.getSuitStar())) {// 适合星座
				cri.where().and("suitStar", "=", giftSearch.getSuitStar());
			}
			if (giftSearch.getSuitAgeGroup() != null
					&& !all.equals(giftSearch.getSuitAgeGroup())) {// 适合年龄段
				cri.where().and("suitAgeGroup", "=",
						giftSearch.getSuitAgeGroup());
			}
			if (giftSearch.getMinPrice() != null
					&& giftSearch.getMaxPrice() != null) {// 最高价/最低价
				Double min = giftSearch.getMinPrice();
				Double max = giftSearch.getMaxPrice();
				if (Double.compare(giftSearch.getMinPrice(),
						giftSearch.getMaxPrice()) > 0) {
					min = giftSearch.getMaxPrice();
					max = giftSearch.getMinPrice();
				}
				cri.where().and("price", ">=", min).and("price", "<=", max);
			} else if (giftSearch.getMinPrice() != null) {
				cri.where().and("price", ">=", giftSearch.getMinPrice());
			} else if (giftSearch.getMaxPrice() != null) {
				cri.where().and("price", "<=", giftSearch.getMaxPrice());
			}
		}
		cri.getOrderBy().desc("date");

		// 审核通过的,还没下架的,没被删除的才能被显示
		List<Gift> gifts = dao.query(Gift.class, cri, page);

		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(Gift.class, cri));

		dao.fetchLinks(gifts, "tags");// 获取标签
		// dao.fetchLinks(gifts, "pics");// 获取礼品图片

		for (Gift gift : gifts) {
			gift.setPrice(Double.parseDouble(Gift.df.format(gift.getPrice())));// 130.000000->130.0
			gift.setCollectNum(getGiftCollectNum(gift));// 获取收藏数
			gift.setShareNum(getGiftShareNum(gift));// 获取分享数
			if (user != null) {
				if (!okCollect(user, gift))// 可否收藏
					gift.setCollected(true);
				if (hasShared(user, gift))// 是否分享过
					gift.setShared(true);
				if (hasBuyed(user, gift))// 是否购买过,或者已存在在购物车中
					gift.setBuyed(true);
				if (hasWished(user, gift))// 是否用该商品许愿果
					gift.setWished(true);
			}
		}
		return new QueryResult(gifts, page);
	}

	// 获取收藏数
	public int getGiftCollectNum(Gift gift) {
		return dao.count(GiftCollect.class,
				Cnd.where("giftId", "=", gift.getId()));
	}

	// 获取分享数
	public int getGiftShareNum(Gift gift) {
		return dao.count(GiftShare.class,
				Cnd.where("giftId", "=", gift.getId()));
	}

	public String shareGift(Gift gift, User sharer, HttpSession session) {
		// 分享到社交网有一定积分奖励
		if (sharer != null
				&& !userService.okMuzhiCoin(sharer,
						MuzhiCoin.MuzhiCoin_For_Share_Gift, session)) {
			return ActionMessage.Not_MuzhiCoin;
		}
		GiftShare share = new GiftShare();
		share.setDate(DateUtils.now());
		share.setId(NumGenerator.getUuid());
		share.setSharerId(sharer.getId());
		share.setGiftId(gift.getId());
		dao.insert(share);
		return ActionMessage.success;
	}

	// 判断该商品是否已被该用户收藏~
	private boolean okCollect(User collector, Gift gift) {
		GiftCollect collect = dao.fetch(
				GiftCollect.class,
				Cnd.where("collectorId", "=", collector.getId()).and("giftId",
						"=", gift.getId()));
		return collect == null ? true : false;
	}

	// 判断是否分享
	private boolean hasShared(User sharer, Gift gift) {
		int tmp = dao.count(
				GiftShare.class,
				Cnd.where("sharerId", "=", sharer.getId()).and("giftId", "=",
						gift.getId()));
		return (tmp == 0) ? false : true;
	}

	// 判断是否购买过,或者已存在在购物车
	private boolean hasBuyed(User buyer, Gift gift) {
		int tmp = dao.count(
				OrderForm.class,
				Cnd.where("buyerId", "=", buyer.getId()).and("giftId", "=",
						gift.getId()));
		return (tmp == 0) ? false : true;
	}

	private boolean hasWished(User wisher, Gift gift) {
		int tmp = dao.count(
				OrderFormForWish.class,
				Cnd.where("creatorId", "=", wisher.getId()).and("giftId", "=",
						gift.getId()));
		return (tmp == 0) ? false : true;
	}
}
