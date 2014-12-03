package com.muzhiliwu.service.gift;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.TableName;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.mvc.upload.TempFile;

import com.muzhiliwu.model.UnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.Shop;
import com.muzhiliwu.model.gift.ShopUnreadReply;
import com.muzhiliwu.model.gift.ShopVisit;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.FileFilter;
import com.muzhiliwu.utils.MD5;

@IocBean
public class ShopService {
	@Inject
	private Dao dao;

	/**
	 * 检查商户登录
	 * 
	 * @param code
	 *            商户名
	 * @param pass
	 *            商户密码
	 * @param convert
	 *            是否加密
	 * @return
	 */
	public Shop checkShop(String code, String pass, boolean convert) {
		MD5 md5 = new MD5();
		Shop shop = dao.fetch(
				Shop.class,
				Cnd.where("code", "=", code).and("pass", "=",
						convert ? md5.getMD5ofStr(pass) : pass));
		return shop;
	}

	/**
	 * 获取用户详细的信息
	 * 
	 * @param shop
	 * @return
	 */
	public Shop getMyDetail(Shop shop) {
		shop = dao.fetch(Shop.class, shop.getId());
		return shop;
	}

	/**
	 * 获取用户左侧信息
	 * 
	 * @param shop
	 * @return
	 */
	public Shop me(Shop shop) {
		shop = dao.fetch(Shop.class, shop.getId());
		shop.setUnreadReplyNum(countUnreadReplyNum(shop.getId()));// 获取未读信息数
		shop.setMonthVisitRank(countShopMonthVisitRank(shop.getId()));
		shop.setMonthVisitNum(countShopMonthVisitNum(shop.getId()));
		return shop;
	}

	public void setGiftNotSale(Shop shop, String ids) {
		for (String id : ids.split(",")) {
			Gift gift = dao.fetch(Gift.class,
					Cnd.where("id", "=", id).and("shopId", "=", shop.getId()));
			if (gift != null) {
				gift.setSaleState(Gift.NotSale);
				dao.update(gift);
			}
		}
	}

	public void deleteMyGifts(Shop shop, String ids) {
		for (String id : ids.split(",")) {
			Gift gift = dao.fetch(Gift.class,
					Cnd.where("id", "=", id).and("shopId", "=", shop.getId()));
			if (gift != null) {
				gift.setDelete(true);
				dao.update(gift);
			}
		}
	}

	public QueryResult getMyGiftList(Shop shop, Pager page, String orderBy) {
		String AscClickNum = "AscClickNum";// 点击量升序
		String DescClickNum = "DescClickNum";// 点击量降序
		String AscSalePrice = "AscSalePrice";// 销售价升序
		String DescSalePrice = "DescSalePrice";// 销售价降序
		String OnSale = "OnSale";// 在售
		String NotSale = "NotSale";// 已下架

		Criteria cri = Cnd.cri();
		cri.where().and("shopId", "=", shop.getId());
		cri.where().and("isDelete", "=", false);// 没被删除的
		if (AscClickNum.equals(orderBy)) {// 点击量升序
			cri.getOrderBy().asc("clickNum");
		} else if (DescClickNum.equals(orderBy)) {// 点降
			cri.getOrderBy().desc("clickNum");
		} else if (AscSalePrice.equals(orderBy)) {// 销售价升序
			cri.getOrderBy().asc("price");
		} else if (DescSalePrice.equals(orderBy)) {// 价降
			cri.getOrderBy().desc("price");
		} else if (OnSale.equals(orderBy)) {// 在售
			cri.where().and("saleState", "=", Gift.OnSale);
		} else if (NotSale.equals(orderBy)) {// 已下架
			cri.where().and("saleState", "=", Gift.NotSale);
		}
		List<Gift> gifts = dao.query(Gift.class, cri, page);
		if (page == null)
			page = new Pager();
		page.setRecordCount(dao.count(Gift.class, cri));
		return new QueryResult(gifts, page);
	}

	public String editShop(Shop shop) {
		Shop s = dao.fetch(Shop.class, shop.getId());
		if (true) {
			s.setShopName(shop.getShopName());
			s.setUrl(shop.getUrl());
			s.setAlipayCode(shop.getAlipayCode());
			s.setShopIntroduct(shop.getShopIntroduct());
			s.setPass(MD5.toMD5(shop.getPass()));
			s.setShopBoss(shop.getShopBoss());
			s.setMobile(shop.getMobile());
			s.setEmail(shop.getEmail());
			dao.update(shop);
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	public String uploadPhoto(String code, TempFile tfs, String template) {
		// 更新数据库信息
		Shop shop = getShopByCode(code);

		// template += "/WEB-INF/shopLogo/";
		template += "/shopLogo/";

		// 如果对应的文件夹不存在,就创建该文件夹
		Files.createDirIfNoExists(template);

		// 获取文件后缀名
		int beginIndex = tfs.getFile().getAbsolutePath().lastIndexOf(".");
		String fileExt = tfs.getFile().getAbsolutePath().substring(beginIndex);

		// 删除原来存在的头像
		String regx = code + "_pic" + ".*";
		File f2 = new File(template);
		File[] s = f2.listFiles(new FileFilter(regx));
		for (File file : s) {
			Files.deleteFile(file);
		}

		// 上传新的头像文件
		File f = new File(template + code + "_pic" + fileExt);
		try {
			Files.move(tfs.getFile(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		shop.setLogo("/shopLogo/" + code + "_pic" + fileExt);
		dao.update(s);
		return "/shopLogo/" + code + "_pic" + fileExt;
	}

	public String cutLogo(String code, String picPath, int top, int left,
			int width, int height, String template) {
		// 更新数据库信息
		Shop s = getShopByCode(code);

		File pic = new File(template + picPath);

		// 如果对应的文件夹不存在,就创建该文件夹
		Files.createDirIfNoExists(template);

		// 获取文件后缀名
		int beginIndex = pic.getAbsolutePath().lastIndexOf(".");
		String fileExt = pic.getAbsolutePath().substring(beginIndex);

		// 删除原来存在的头像
		String regx = code + fileExt;
		File f2 = new File(template + "/shopLogo/" + code + fileExt);
		Files.deleteFile(f2);

		// 上传新的头像文件
		File f = new File(template + "/shopLogo/" + code + fileExt);
		try {
			BufferedImage img = (BufferedImage) ImageIO.read(pic);
			height = Math.min(height, img.getHeight());
			width = Math.min(width, img.getWidth());
			if (height <= 0)
				height = img.getHeight();
			if (width <= 0)
				width = img.getWidth();
			top = Math.max(Math.max(0, top), img.getHeight() - height);
			left = Math.min(Math.max(0, left), img.getWidth() - width);
			BufferedImage cut = img.getSubimage(left, top, width, height);
			ImageIO.write(cut, ".png".equals(fileExt) ? "png" : "jpeg", f);
			// Files.move(tfs.getFile(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		f2 = new File(template + picPath);
		Files.deleteFile(f2);

		s.setLogo(/* template */"/shopLogo/" + code + fileExt);
		dao.update(s);
		return "/shopLogo/" + code + fileExt;
	}

	public Shop getShopByCode(String code) {
		return dao.fetch(Shop.class, Cnd.where("code", "=", code));
	}

	// 获取月访问量
	public int countShopMonthVisitNum(String shopId) {
		// 获取年月
		int year = DateUtils.getYear();
		int month = DateUtils.getMonth();

		Map mp = new HashMap<String, Integer>();
		mp.put("year", year);
		mp.put("month", month);

		TableName.set(mp);
		dao.create(ShopVisit.class, false);
		ShopVisit visit = dao.fetch(ShopVisit.class,
				Cnd.where("shopId", "=", shopId));
		TableName.clear();
		return visit.getVisitNum();
	}

	// 获取月访问排行
	public int countShopMonthVisitRank(String shopId) {
		// 获取年月
		int year = DateUtils.getYear();
		int month = DateUtils.getMonth();

		Map mp = new HashMap<String, Integer>();
		mp.put("year", year);
		mp.put("month", month);

		TableName.set(mp);
		dao.create(ShopVisit.class, false);
		int count = dao.count(
				ShopVisit.class,
				Cnd.wrap("visitNum >= (select visitNum from " + "t_shop_visit_"
						+ year + "_" + month + " where shopId='" + shopId
						+ "')"));
		TableName.clear();
		return count;
	}

	// 获取商家未读信息数
	public int countUnreadReplyNum(String receiverId) {
		return dao.count(
				ShopUnreadReply.class,
				Cnd.where("shopId", "=", receiverId).and("state", "=",
						UnreadReply.Unread));
	}
}
