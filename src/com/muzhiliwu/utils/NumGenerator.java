package com.muzhiliwu.utils;

import java.util.UUID;

public class NumGenerator {
	/**
	 * 生成序列码，当前号码加1，高位补0
	 * 
	 * @param length
	 * @param preNum
	 * @return
	 */
	public static String getNextNum(int length, String preNum) {
		String result = "";
		if (preNum == null || preNum.length() <= 0) {
			for (int i = 0; i < length; i++) {
				result += "0";
			}
			return result;
		}
		int t = Integer.parseInt(preNum);
		t += 1;
		result = String.valueOf(t);
		for (int i = result.length(); i < length; i++) {
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 获取uuid
	 * 
	 * @return
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
