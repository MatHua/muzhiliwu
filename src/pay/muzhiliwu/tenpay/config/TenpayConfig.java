package pay.muzhiliwu.tenpay.config;

public class TenpayConfig {
	// 收款方
	public static String spname = "财付通双接口测试";
	// 商户号
	public static String partner = "";
	// 密钥
	public static String key = "";
	// 交易完成后跳转的URL
	public static String return_url = "http://*/tenpay_api_b2c/payReturnUrl.jsp";
	// 接收财付通通知地址的URL
	public static String notify_url = "http://*/tenpay_api_b2c/payNotifyUrl.jsp";
}
