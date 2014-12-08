//首页功能函数
$(document).ready(function(e) {
	//遍历获取表单数据并且返回map值
	function getFromInputValMap(formIdObj){
		//创建map对象
		var map={};
		//遍历表格下name=form_input的input并且将其储存进map
		$(formIdObj).find("input[name=form_input]").each(function(index, element) {
			//当值不为空时有效
			if($(this).val()!='')
				map[$(this).attr("fr")]=$(this).val();
		});
		//返回map对象
		return map;
	}
	
	//检查首页登录状态
	function checkLogin(){
		$.ajax({
			url:"user/me",
			type:"POST",
			success: function(data){
				var obj=eval("("+data+")");
				if(obj.type!=6)
				{
					$("#head_not_login_info").hide();
					$("#head_login_info").show();
					if(obj.object.nickName==null)
						$("#head_username").html(obj.object.code)
					else
						$("#head_username").html(obj.object.nickName)
				}
				else {
					$("#head_login_info").hide();
					$("#head_not_login_info").show();
				}
			}
		})
	}
	
	//测试遍历获取表单值
	function alertFromInputVal(formIdObj){
		$(formIdObj).find("input[name=form_input]").each(function(index, element) {
			alert($(this).attr("fr")+":"+$(this).val());
		});
	}
	
	//打开页面时验证是否已经登录
	checkLogin();
	
	//注册
	$("#head_sign").click(function(){
		var map=getFromInputValMap($("#head_sign_form"));
		if(map["user.code"]==null||map["user.pass"]==null)
			$.UIkit.notify("用户名或密码不能为空",{status:'warning'});
		else if(map["user.pass"]!=map["repass"])
			$.UIkit.notify("两次输入的密码不一样，请检查并重试",{status:'warning'});
		else
		{
			$.ajax({
				url:"user/regist",
				type:"POST",
				data:map,
				success: function(data)
				{
					var obj=eval("("+data+")");
					if(obj.type==1)
					{
						$.UIkit.notify("<i class='uk-icon-check'></i>"+obj.message+",3秒之后将自动刷新页面",{status:'success'});
						$.ajax({
							url:"user/login",
							type:"POST",
							data:{"user.code":map["user.code"],"user.pass":map["user.pass"]},
							success: function(data)
							{
								setTimeout("location.reload()",3000);
							}
						})
					}
					else if(obj.type==0)
					{
						$.UIkit.notify(obj.message,{status:'warning'});
					}
					else
					{
						$.UIkit.notify("连接服务器失败，请稍后重试",{status:'danger'});
					}
				}
			})
		}
	})
	
	//已有帐号
	$("#already_sign").click(function(){
		var modal = $.UIkit.modal("#sign_dialog");
		modal.hide();
		var modal = $.UIkit.modal("#login_dialog");
		modal.show();
	})
	
	//登录
	$("#head_login").click(function(){
		var map=getFromInputValMap($("#head_login_form"));
		if(map["user.code"]==null||map["user.pass"]==null)
			$.UIkit.notify("用户名或密码不能为空",{status:'warning'});
		else
		{
			$.ajax({
				url:"user/login",
				type:"POST",
				data:map,
				success: function(data)
				{
					var obj=eval("("+data+")");
					if(obj.type==5)
					{
						$.UIkit.notify(obj.message,{status:'danger'});
					}
					else if(obj.type==1)
					{
						$.UIkit.notify("<i class='uk-icon-check'></i>"+obj.message+",3秒之后将自动刷新页面",{status:'success'});
						setTimeout("location.reload()",3000)
					}
					else if(obj.type==0)
					{
						$.UIkit.notify(obj.message,{status:'warning'});
					}
					else
					{
						$.UIkit.notify("连接服务器失败，请稍后重试",{status:'danger'});
					}
				}
			})
		}
	})
	
	//还未注册
	$("#not_sign").click(function(){
		var modal = $.UIkit.modal("#login_dialog");
		modal.hide();
		var modal = $.UIkit.modal("#sign_dialog");
		modal.show();
	})
	
	//退出登录
	$("#head_logout").click(function(){
		$.ajax({
			url:"user/logout",
			type:"POST",
			success: function(data){
				obj=eval("("+data+")")
				if(obj.type==1)
				{
					$.UIkit.notify("退出成功,2秒之后将自动刷新页面",{status:'warning'});
					setTimeout("location.reload()",2000)
				}
			}
		})
	})
	
	//获取商品
	
	
//功能函数结束
});