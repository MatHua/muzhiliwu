package com.muzhiliwu.init;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

import com.muzhiliwu.model.gift.Admin;
import com.muzhiliwu.utils.AdminUtils;
import com.muzhiliwu.utils.MD5;

public class SetupAndInitParam implements Setup {

	private Dao dao;

	@Override
	public void destroy(NutConfig arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(NutConfig config) {
		// TODO Auto-generated method stub
		// 搜索所有的 权限，添加到数据库,同时加入到超级管理员的角色下
		dao = config.getIoc().get(Dao.class);
		// 自动建表
		for (Class<?> klass : Scans.me().scanPackage("com.muzhiliwu.model")) {
			if (null != klass.getAnnotation(Table.class)) {
				dao.create(klass, false);
			}
		}
		for (Class<?> klass : Scans.me()
				.scanPackage("com.muzhiliwu.model.gift")) {
			if (null != klass.getAnnotation(Table.class)) {
				dao.create(klass, false);
			}
		}
		Admin superAdmin = dao.fetch(Admin.class, AdminUtils.superAdminId);
		if (superAdmin == null) {
			superAdmin = new Admin();
			superAdmin.setId(AdminUtils.superAdminId);
			superAdmin.setCode(AdminUtils.superAdminCode);
			superAdmin.setPass(MD5.toMD5(AdminUtils.superAdminPass));
			dao.insert(superAdmin);
		}
	}

}
