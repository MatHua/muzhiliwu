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
	
	
	
//功能函数结束
});