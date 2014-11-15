package com.muzhiliwu.utils;

import java.io.File;
import java.io.FilenameFilter;

public class LogFileFilter implements FilenameFilter {

	private String index;

	public LogFileFilter(String index) {
		this.index = index;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(index);
	}

}
