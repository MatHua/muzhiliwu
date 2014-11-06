package com.muzhiliwu.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.mvc.upload.TempFile;

import com.muzhiliwu.model.MessUnreadReply;
import com.muzhiliwu.model.ShareUnreadReply;
import com.muzhiliwu.model.User;
import com.muzhiliwu.utils.DateUtils;
import com.muzhiliwu.utils.FileFilter;
import com.muzhiliwu.utils.Integral;
import com.muzhiliwu.utils.MD5;
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

		// 获取未读的回复
		if (user != null)
			getReply(user);
		return user;
	}

	/**
	 * 注册或者更新用户信息
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	public boolean registOrEditUser(User user, String pass) {
		if (Strings.isBlank(user.getId())) {// 注册
			// 账号已被注册
			if (!checkRepeat(user.getCode())) {
				return false;
			}
			// 一些系统自修改的信息
			user.setCode(user.getCode().trim());
			user.setId(NumGenerator.getUuid());// 生成id
			user.setPass(MD5.toMD5(pass.trim()));// 密码加密
			user.setDate(DateUtils.now());// 注册时间
			user.setIntegral(Integral.Integral_For_New_User);// 积分初始化
			dao.insert(user);
		} else {
			User u = dao.fetch(User.class, Cnd.where("id", "=", user.getId()));
			// 避免修改一些不能修改的信息
			user.setCode(u.getCode());
			user.setPass(u.getPass());
			user.setPhone(u.getPhone() == null ? null : u.getPhone());
			user.setDate(DateUtils.now());
			dao.update(user);
		}
		return true;
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
			template += "/WEB-INF/userphoto/";

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
	public boolean okIntegral(User user, long increment, HttpSession session) {
		user = dao.fetch(User.class, user.getId());
		if (user.getIntegral() + increment >= 0) {// 积分足够
			user.setIntegral(user.getIntegral() + increment);
			dao.update(user);
			session.setAttribute("t_user", user);
			return true;
		}
		return false;
	}

	// 获取用户未读回复
	private void getReply(User user) {

		// 获取未读的留言墙回复
		dao.fetchLinks(user, "myMessUnreadReplies", Cnd.orderBy().desc("date"));
		MessUnreadReply messReply;
		for (int i = 0; i < user.getMyMessUnreadReplies().size(); i++) {
			// 获取回复者信息
			dao.fetchLinks(user.getMyMessUnreadReplies().get(i), "replier");
		}
		// 获取未读的分享墙回复
		dao.fetchLinks(user, "myShareUnreadReplies");
		ShareUnreadReply shareReply;
		for (int i = 0; i < user.getMyShareUnreadReplies().size(); i++) {
			// 获取回复者信息
			dao.fetchLinks(user.getMyShareUnreadReplies().get(i), "replier");
			// shareReply = user.getMyShareUnreadReplies().get(i);
			// shareReply.setReplier(getUserById(shareReply.getReplierId()));
		}

		// dao.fetchLinks(user, "myMessages");
		// dao.fetchLinks(user, "myMessComments", Cnd.orderBy().desc("date"));//
		// 评论按时间降序

		// dao.fetchLinks(user, "myWishes");
		// dao.fetchLinks(user, "myWishCollectes");

		// dao.fetchLinks(user, "myShares");
		// dao.fetchLinks(user, "myShareCollectes");
		// dao.fetchLinks(user, "myShareComments",
		// Cnd.orderBy().desc("date"));// 评论按时间降序
	}

	// 检查密码
	private boolean checkPass(User user, String oldPass) {
		MD5 md5 = new MD5();
		if (user != null) {
			if (md5.getMD5ofStr(oldPass).equals(user.getPass())) {
				return true;
			}
		}
		return false;
	}

	private User getUserByCode(String code) {
		User user = dao.fetch(User.class, Cnd.where("code", "=", code));
		return user;
	}
}
