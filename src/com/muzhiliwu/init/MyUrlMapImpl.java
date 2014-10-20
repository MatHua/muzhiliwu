package com.muzhiliwu.init;

import java.lang.reflect.Method;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.mvc.ActionChainMaker;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.UrlMappingImpl;
import org.nutz.resource.Scans;

public class MyUrlMapImpl extends UrlMappingImpl {

	private Dao dao;

	/**
	 * 添加URL时初始化权限
	 */
	@Override
	public void add(ActionChainMaker maker, ActionInfo ai, NutConfig config) {

		super.add(maker, ai, config);

	}
}
