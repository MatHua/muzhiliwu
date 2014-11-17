package com.alipay.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;

@IocBean
@At("alipay")
public class AlipayModule {
	public static final String Base_Url_1 = "http://www.muzhiliwu.com/";
	public static final String Base_Url_2 = "http://www.yaomailiwu.com/";
	public static final String Base_Url_Test = "http://localhost:8080/";
	public static final String Bank_Pay_Notify_Url = "alipay/bank_pay/notify_url";

	// 异步通知URL,用于接收支付宝发送过来的处理结果
	@At("/bank_pay/notify_url")
	public String notifyUrlForBankPay(HttpServletRequest request)
			throws UnsupportedEncodingException {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no")
				.getBytes("ISO-8859-1"), "UTF-8");

		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes(
				"ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status")
				.getBytes("ISO-8859-1"), "UTF-8");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if (AlipayNotify.verify(params)) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("WAIT_BUYER_PAY")) {
				// 该判断表示买家已在支付宝交易管理中产生了交易记录，但没有付款

				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// out.println("success");
				return "success";// 请不要修改或删除
			} else if (trade_status.equals("WAIT_SELLER_SEND_GOODS")) {
				// 该判断表示买家已在支付宝交易管理中产生了交易记录且付款成功，但卖家没有发货

				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// out.println("success");
				return "success";// 请不要修改或删除
			} else if (trade_status.equals("WAIT_BUYER_CONFIRM_GOODS")) {
				// 该判断表示卖家已经发了货，但买家还没有做确认收货的操作

				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// out.println("success");
				return "success";// 请不要修改或删除
			} else if (trade_status.equals("TRADE_FINISHED")) {
				// 该判断表示买家已经确认收货，这笔交易完成

				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// out.println("success");
				return "success";// 请不要修改或删除
			} else {
				// out.println("success");
				return "success";// 请不要修改或删除
			}

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

		} else {// 验证失败
			// out.println("fail");
			return "fail";
		}
	}

	// @At("ensureDeliverGoods")
	// public Object ensureDeliverGoods() {
	// //
	// //////////////////////////////////请求参数//////////////////////////////////////
	//
	// // 支付宝交易号
	// String trade_no = new String(request.getParameter("WIDtrade_no")
	// .getBytes("ISO-8859-1"), "UTF-8");
	// // 必填
	//
	// // 物流公司名称
	// String logistics_name = new String(request.getParameter(
	// "WIDlogistics_name").getBytes("ISO-8859-1"), "UTF-8");
	// // 必填
	//
	// // 物流发货单号
	//
	// String invoice_no = new String(request.getParameter("WIDinvoice_no")
	// .getBytes("ISO-8859-1"), "UTF-8");
	// // 物流运输类型
	// String transport_type = new String(request.getParameter(
	// "WIDtransport_type").getBytes("ISO-8859-1"), "UTF-8");
	// // 三个值可选：POST（平邮）、EXPRESS（快递）、EMS（EMS）
	//
	// //
	// ////////////////////////////////////////////////////////////////////////////////
	//
	// // 把请求参数打包成数组
	// Map<String, String> sParaTemp = new HashMap<String, String>();
	// sParaTemp.put("service", "send_goods_confirm_by_platform");
	// sParaTemp.put("partner", AlipayConfig.partner);
	// sParaTemp.put("_input_charset", AlipayConfig.input_charset);
	// sParaTemp.put("trade_no", trade_no);
	// sParaTemp.put("logistics_name", logistics_name);
	// sParaTemp.put("invoice_no", invoice_no);
	// sParaTemp.put("transport_type", transport_type);
	//
	// // 建立请求
	// String sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp);
	// out.println(sHtmlText);
	// }
}
