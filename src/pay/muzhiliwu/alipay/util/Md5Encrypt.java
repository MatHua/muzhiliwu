package pay.muzhiliwu.alipay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pay.muzhiliwu.alipay.config.AlipayConfig;

/**
 * 名称：MD5加密类
 * 功能：将支付宝提交的相关参数按照传入编码进行MD5加密
 * 接口名称：标准即时到账接口
 * 版本：2.0
 * 日期：2008-12-25
 * 作者：支付宝公司销售部技术支持团队
 * 联系：0571-26888888
 * 版权：支付宝公司
 * */
public class Md5Encrypt {
	// Used building output as Hex~用于16进制
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * @return 密文
	 */
	public static String md5(String text) {
		/**
		 * MessageDigest为程序提供信息摘要算法的功能(如MD5算法)
		 * 信息摘要是安全的单向哈希函数,它接收任意大小的数据,并输出固定长度的哈希值
		 */
		MessageDigest msgDigest = null;
		try {
			// 指定MD5为摘要算法
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}
		try {
			// 使用指定的byte数组更新摘要
			msgDigest.update(text.getBytes(AlipayConfig.CharSet));// 按utf-8编码形式加密
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");
		}
		byte[] bytes = msgDigest.digest();
		String mdSStr = new String(encodeHex(bytes));
		return mdSStr;
	}

	public static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}
}
