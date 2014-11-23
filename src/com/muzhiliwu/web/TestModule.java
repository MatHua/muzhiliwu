package com.muzhiliwu.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.view.JspView;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.muzhiliwu.listener.CheckLoginFilter;
import com.muzhiliwu.model.TestDemo;
import com.muzhiliwu.model.Wish;
import com.muzhiliwu.model.gift.Gift;
import com.muzhiliwu.model.gift.GiftClick;
import com.muzhiliwu.model.gift.ReceiveContactWay;
import com.muzhiliwu.service.TestDemoService;
import com.muzhiliwu.service.WishService;
import com.muzhiliwu.utils.ActionMessage;
import com.muzhiliwu.utils.DateUtils;

//测试
@IocBean
@At("test")
public class TestModule {

	@Inject
	private Dao dao;
	private static Log log = LogFactory.getLog(TestDemo.class);
	@Inject
	private TestDemoService testDemoService;
	@Inject
	private WishService wishService;

	@At
	@Ok("json")
	public Object createTable(String year, String month) {
		Map mp = new HashMap<String, String>();
		mp.put("year", year);
		mp.put("month", month);

		TableName.run(mp, new Runnable() {
			@Override
			public void run() {
				dao.create(GiftClick.class, false);
			}
		});
		return "xxx";
	}

	@At
	@Ok("json")
	public Object testNull(String name, Double number) {
		String tmp = "";
		if (name == null)
			tmp += "name is null & ";
		else
			tmp += name;
		if (number == null)
			tmp += "number is null";
		else
			tmp += number.toString();
		return tmp;
	}

	@At
	@Ok("json")
	// @POST
	public Object addAddress() {
		ReceiveContactWay address = new ReceiveContactWay();
		address.setAddressDetail("xx街");
		address.setAreaName("番禺区");
		address.setCityName("广州市");
		address.setCreatorId("1");
		address.setDate("2");
		address.setDefaultAddress(true);
		address.setFullName("马云");
		address.setId("1");
		address.setMobile("123482627");
		address.setPostCode("529500");
		address.setProvinceName("广东省");
		address.setRemarks("周末送");
		dao.insert(address);
		return "xx";
	}

	@At
	@Ok("json")
	@POST
	public ActionMessage saveDemo(String name, String email) {
		testDemoService.saveDemo(name, email);
		ActionMessage am = new ActionMessage();
		am.setType("success");
		am.setMessage("save message ok!");

		am.setObject(name);
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object check() {
		TestDemo test = dao.fetch(TestDemo.class, "1");
		dao.fetchLinks(test, "myDemos");
		return test;
	}

	@At
	@POST
	public void gift() {
		Gift gift = new Gift();
		gift.setAuditMess("1");
		gift.setAuditState(Gift.AuditSuccess);
		gift.setDate(DateUtils.now());
		gift.setDescript("1");
		gift.setFromType("1");
		gift.setId("3");
		gift.setName("1");
		gift.setPackagePostal("全国包邮");
		gift.setPrice(20.0001);
		gift.setSaleState(Gift.OnSale);
		gift.setShopId("1");
		gift.setStock(1);
		gift.setType(Gift.FromEntityShop);
		gift.setUrl("1");
		gift.setDelete(false);
		dao.insert(gift);
	}

	@At
	@Ok("json")
	@POST
	public Object getNewly(String wisherId) {
		// name LIKE 'J%' AND age>20
		// ELECT * FROM t_person WHERE || name LIKE 'J%' AND age>20;
		// TestDemo test = dao.fetch(TestDemo.class,
		// Cnd.wrap("id in (select max(id) from t_test_demo)"));
		Wish wish = dao
				.fetch(Wish.class,
						Cnd.wrap("wisherId='"
								+ wisherId
								+ "' and date in (select max(date) from t_wish where wisherId='"
								+ wisherId + "')"));
		return wish;
	}

	@At("/?/name")
	@Ok("json")
	@POST
	@Filters(@By(type = CheckLoginFilter.class, args = { "ioc:checkLoginFilter" }))
	public Object myName(String id, HttpServletRequest request) {
		ActionMessage am = new ActionMessage();
		am.setMessage(id);
		am.setType(request.getRemoteAddr());
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object myTest(String name) {
		ActionMessage am = new ActionMessage();
		List<TestDemo> tests = dao.query(TestDemo.class,
				Cnd.where("name", "=", name));
		dao.fetchLinks(tests, "myTests");
		am.setObject(tests);
		am.setMessage("雅妹蝶~");
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object me(String name) {
		ActionMessage am = new ActionMessage();
		List<TestDemo> tests = dao.query(TestDemo.class,
				Cnd.where("name", "=", name));
		dao.fetchLinks(tests, "myTests");
		for (TestDemo test : tests) {
			// dao.fetchLinks(test.getMyTests(), "msgs");
		}
		am.setObject(tests);
		am.setMessage("雅妹蝶~");
		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object delete(String id) {
		TestDemo xx = dao.fetch(TestDemo.class, id);
		dao.fetchLinks(xx, "myTests");
		dao.deleteWith(xx, "myTests");

		ActionMessage am = new ActionMessage();
		List<TestDemo> tests = dao.query(TestDemo.class,
				Cnd.where("name", "=", "name"));
		dao.fetchLinks(tests, "myTests");

		am.setObject(tests);
		am.setMessage("雅妹蝶~");

		return am;
	}

	@At
	@Ok("json")
	@POST
	public Object insert() {
		TestDemo test = new TestDemo();
		test.setId("xx_xx");
		test.setDate("xx_xx");
		test.setEmail("xx_xx");
		test.setName("xx_xx");
		dao.insert(test);

		TestDemo demo = new TestDemo();
		demo.setId("xxxxx");
		demo.setDate("xxxxx");
		demo.setEmail("xxxxx");
		demo.setName("xxxxx");
		dao.insert(demo);
		return "插入成功~";
	}

	@At
	@Ok("json")
	@POST
	public Object getById1() {
		TestDemo test = dao.fetch(TestDemo.class, "xx_xx");
		return test;
	}

	@At
	@Ok("json")
	@POST
	public Object getById2() {
		log.warn("查找一条信息");
		log.info("有新操作~");
		log.error("错误了~");
		// log.debug("测试~");
		// TestDemo test = dao.fetch(TestDemo.class, "xxxxx");
		return "xx";
	}

	@At
	@POST
	public JspView getHtml(HttpServletResponse response) throws IOException {
		// 支付类型
		String payment_type = "1";
		// 必填，不能修改
		// 服务器异步通知页面路径
		String notify_url = "http://xx/create_partner_trade_by_buyer-JAVA-UTF-8/notify_url.jsp";
		// 需http://格式的完整路径，不能加?id=123这类自定义参数

		// 页面跳转同步通知页面路径
		String return_url = "http://xxx/create_partner_trade_by_buyer-JAVA-UTF-8/return_url.jsp";
		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		// 卖家支付宝帐户
		String seller_email = "xxx";
		// 必填

		// 商户订单号
		String out_trade_no = "xxssaqwq";
		// 商户网站订单系统中唯一订单号，必填

		// 订单名称
		String subject = "xxx";
		// 必填

		// 付款金额
		String price = "111";
		// 必填

		// 商品数量
		String quantity = "1";
		// 必填，建议默认为1，不改变值，把一次交易看成是一次下订单而非购买一件商品
		// 物流费用
		String logistics_fee = "0.00";
		// 必填，即运费
		// 物流类型
		String logistics_type = "EXPRESS";
		// 必填，三个值可选：EXPRESS（快递）、POST（平邮）、EMS（EMS）
		// 物流支付方式
		String logistics_payment = "SELLER_PAY";
		// 必填，两个值可选：SELLER_PAY（卖家承担运费）、BUYER_PAY（买家承担运费）
		// 订单描述

		// 商品展示地址
		String show_url = "http://www.xxx.com/myorder.html";

		// 收货人姓名
		String receive_name = "xx";

		// 收货人地址
		String receive_address = "XXXXXXXXXXXXXX";

		// 收货人邮编
		String receive_zip = "123456";

		// 收货人电话号码
		String receive_phone = "0571-88158090";

		// 收货人手机号码
		String receive_mobile = "13312341234";

		// ////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_partner_trade_by_buyer");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("price", price);
		sParaTemp.put("quantity", quantity);
		sParaTemp.put("logistics_fee", logistics_fee);
		sParaTemp.put("logistics_type", logistics_type);
		sParaTemp.put("logistics_payment", logistics_payment);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("receive_name", receive_name);
		sParaTemp.put("receive_address", receive_address);
		sParaTemp.put("receive_zip", receive_zip);
		sParaTemp.put("receive_phone", receive_phone);
		sParaTemp.put("receive_mobile", receive_mobile);

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "xx");

		ServletOutputStream out = response.getOutputStream();
		out.println(sHtmlText);
		return new JspView("");
	}
}
