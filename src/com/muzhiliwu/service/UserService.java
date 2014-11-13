package com.muzhiliwu.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.mvc.upload.TempFile;

import com.muzhiliwu.model.UnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.FileFilter;
import com.muzhiliwu.utils.MD5;
import com.muzhiliwu.utils.MuzhiCoin;
import com.muzhiliwu.utils.NumGenerator;

@IocBean
public class UserService {
	@Inject
	private Dao dao;

	/**
	 * 检查登录用户名的正确性
	 * 
	 * @param code
	 * @param pass
	 * @return
	 */
	public User checkUser(String code, String pass, boolean convert) {
		MD5 md5 = new MD5();
		User user = dao.fetch(
				User.class,
				Cnd.where("code", "=", code).and("pass", "=",
						convert ? md5.getMD5ofStr(pass) : pass));
		return user;
	}

	/**
	 * 注册或者更新用户信息
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	public String registUser(User user) {

		// 账号已被注册
		if (!checkRepeat(user.getCode())) {
			return ActionMessage.fail;
		}
		// 一些系统自修改的信息
		user.setCode(user.getCode().trim());
		user.setId(NumGenerator.getUuid());// 生成id
		user.setPass(MD5.toMD5(user.getPass().trim()));// 密码加密
		user.setDate(DateUtils.now());// 注册时间
		user.setMuzhiCoin(MuzhiCoin.MuzhiCoin_For_New_User);// 积分初始化
		user.setPopularityRank(1);
		user.setPopularityValue(0);
		user.setSendGiftRank(1);
		user.setSendGiftValue(0);
		dao.insert(user);
		return ActionMessage.success;
	}

	/**
	 * 用户的信息修改
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	public String editUser(User user, String pass) {
		User u = dao.fetch(User.class, Cnd.where("id", "=", user.getId()));
		if (u.getPass().equals(MD5.toMD5(pass))) {
			// 避免修改一些不能修改的信息,例如：拇指币数,送礼物数,送礼物等级,人气值,人气等级等..都是不能让用户自行修改
			user.setCode(u.getCode());
			user.setPass(u.getPass());
			user.setPhone(u.getPhone() == null ? null : u.getPhone());
			user.setDate(DateUtils.now());
			user.setMuzhiCoin(u.getMuzhiCoin());
			user.setPopularityRank(u.getPopularityRank());
			user.setPopularityValue(u.getPopularityValue());
			user.setSendGiftRank(u.getSendGiftRank());
			user.setSendGiftValue(u.getSendGiftValue());
			dao.update(user);
			return ActionMessage.success;
		}
		return ActionMessage.fail;
	}

	public boolean checkRepeat(String code) {
		User user = dao.fetch(User.class, Cnd.where("code", "=", code));
		return user == null ? true : false;
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @param newPass
	 * @return
	 */
	public boolean changePass(User user, String oldPass, String newPass) {
		user = dao.fetch(User.class, user.getId());
		if (checkPass(user, oldPass)) {
			user.setPass(MD5.toMD5(newPass));
			dao.update(user);
			return true;
		}
		return false;
	}

	/**
	 * 上传头像
	 * 
	 * @param tfs
	 * @param template
	 * @param code
	 */
	public boolean uploadPhoto(String code, TempFile tfs, String template) {
		// 更新数据库信息
		User u = getUserByCode(code);
		if (u != null) {
			// template += "/WEB-INF/userphoto/";
			template += "/userphoto/";

			// 如果对应的文件夹不存在,就创建该文件夹
			Files.createDirIfNoExists(template);

			// 获取文件后缀名
			int beginIndex = tfs.getFile().getAbsolutePath().lastIndexOf(".");
			String fileExt = tfs.getFile().getAbsolutePath()
					.substring(beginIndex);

			// 删除原来存在的头像
			String regx = code + ".*";
			File f2 = new File(template);
			File[] s = f2.listFiles(new FileFilter(regx));
			for (File file : s) {
				Files.deleteFile(file);
			}

			// 上传新的头像文件
			File f = new File(template + code + fileExt);
			try {
				Files.move(tfs.getFile(), f);
			} catch (IOException e) {
				e.printStackTrace();
			}

			u.setPhoto(code + fileExt);
			dao.update(u);
			return true;
		}
		return false;

	}

	public User getUserById(String id) {
		User user = dao.fetch(User.class, id);
		user.setUnreadReplyNum(countUnreadReplyNum(id));// 获取未读信息数
		user.setNewlyWish(getNewlyWish(id));// 获取最近的许愿
		return user;
	}

	/**
	 * 积分处理方法
	 * 
	 * @param user
	 *            处理的用户
	 * @param increment
	 *            积分增量
	 * @return
	 */
	public boolean okMuzhiCoin(User user, int muzhiCoin, HttpSession session) {
		user = dao.fetch(User.class, user.getId());
		if (user.getMuzhiCoin() + muzhiCoin >= 0) {// 积分足够
			user.setMuzhiCoin(user.getMuzhiCoin() + muzhiCoin);
			dao.update(user);
			session.setAttribute("t_user", user);
			return true;
		}
		return false;
	}

	// 获取未读信息数
	private int countUnreadReplyNum(String receiverId) {
		return dao.count(
				UnreadReply.class,
				Cnd.where("receiverId", "=", receiverId).and("state", "=",
						UnreadReply.Unread));
	}

	// 获取最近的那条许愿
	private Wish getNewlyWish(String wisherId) {
		Wish wish = dao
				.fetch(Wish.class,
						Cnd.wrap("wisherId='"
								+ wisherId
								+ "' and date in (select max(date) from t_wish where wisherId='"
								+ wisherId + "')"));
		return wish;
	}

	// 检查密码
	private boolean checkPass(User user, String oldPass) {
		MD5 md5 = new MD5();
		if (user != null && md5.getMD5ofStr(oldPass).equals(user.getPass())) {
			return true;
		}
		return false;
	}

	private User getUserByCode(String code) {
		User user = dao.fetch(User.class, Cnd.where("code", "=", code));
		return user;
	}
}
