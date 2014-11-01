package pay.muzhiliwu.alipay.config;

/**
 * 名称：支付宝关键值配置类 功能：将支付时需要的合作者ID和安全校验码文件中，在其他文设置成常变量。
 */
public class AlipayConfig {
	// partner和key提取方法:登录签约支付宝账户-->点击商家服务可以看到
	public static String partnerID = "";
	public static String key = "";
	// 卖家支付宝账号,例如:gw125@126.com~~这个就要对入驻的商家的支付宝账号存入数据库进行管理
	public static String sellerEmail = "";
	// 页面编码
	public static String CharSet = "utf-8";
}
