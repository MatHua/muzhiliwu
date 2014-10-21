package com.muzhiliwu.service;

import java.io.File;
import java.io.IOException;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.mvc.upload.TempFile;

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

		// dao.fetchLinks(user, "myMessages");
		dao.fetchLinks(user, "myMessComments", Cnd.orderBy().desc("date"));// 评论按时间降序

		// dao.fetchLinks(user, "myWishes");
		// dao.fetchLinks(user, "myWishCollectes");

		// dao.fetchLinks(user, "myShares");
		// dao.fetchLinks(user, "myShareCollectes");
		dao.fetchLinks(user, "myShareComments", Cnd.orderBy().desc("date"));// 评论按时间降序
		return user;
	}

	/**
	 * 注册或者更新用户信息
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	public boolean registOrUpdateUser(User user, String pass) {
		if (Strings.isBlank(user.getId())) {// 注册
			// 账号已被注册
			if (!checkRepeat(user.getCode())) {
				return false;
			}
			user.setId(NumGenerator.getUuid());// 生成id
			user.setPass(MD5.toMD5(pass));// 密码加密
			user.setDate(DateUtils.now());// 注册时间
			user.setPoints(Integral.INTEGRAL_FOR_NEW_USER);// 积分初始化
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
	public boolean updatePass(User user, String newPass) {
		User u = dao.fetch(User.class, user.getId());
		u.setPass(MD5.toMD5(newPass));
		dao.update(u);
		return true;
	}

	/**
	 * 检查密码
	 * 
	 * @param user
	 * @param olePass
	 * @return
	 */
	public boolean checkPass(User user, String olePass) {
		MD5 md5 = new MD5();
		User u = dao.fetch(User.class, user.getId());
		if (user != null) {
			if (md5.getMD5ofStr(olePass).equals(u.getPass())) {
				return true;
			}
		}
		return true;
	}

	/**
	 * 上传头像
	 * 
	 * @param tfs
	 * @param template
	 * @param code
	 */
	public void uploadPhoto(TempFile tfs, String template, String code) {
		template += "/WEB-INF/userphoto/";

		// 如果对应的文件夹不存在,就创建该文件夹
		Files.createDirIfNoExists(template);
		
		// 获取文件后缀名
		int beginIndex = tfs.getFile().getAbsolutePath().lastIndexOf(".");
		String fileExt = tfs.getFile().getAbsolutePath().substring(beginIndex);

		// 删除原来存在的头像
		String regx = code + ".*";
		File f2 = new File(template);
		File[] s = f2.listFiles(new FileFilter(regx));
		for (File file : s) {
			Files.deleteFile(file);
		}

		// 上传头像文件
		File f = new File(template + code + fileExt);
		try {
			Files.move(tfs.getFile(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 更新数据库信息
		User u = getUserByCode(code);
		u.setPhoto(code + fileExt);
		dao.update(u);
	}

	private User getUserByCode(String code) {
		User user = dao.fetch(User.class, Cnd.where("code", "=", code));
		return user;
	}
}
