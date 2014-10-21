package com.muzhiliwu.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

//通过正则表达式过滤文件
public class FileFilter implements FilenameFilter {
	private Pattern p;

	public FileFilter(String regex) {
		p = Pattern.compile(regex);
	}

	public boolean accept(File dir, String name) {
		return p.matcher(name).matches();
	}

}
