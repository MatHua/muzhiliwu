package com.muzhiliwu.search.util;

public class Unicode {
	public static void main(String[] args) {
		String tmp = toUnicode("男生");
		System.out.println(tmp);
		// System.out.println(decodeUnicode(tmp));
		// char[] msg = tmp.toCharArray();
		// for (int i = 0; i < msg.length; i++) {
		// System.out.println(msg[i]);
		// }
	}

	// 字符串转unicode编码
	public static String toUnicode(final String dataStr) {
		char[] utfBytes = dataStr.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	// unicode编码转字符串
	public static String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}
}
