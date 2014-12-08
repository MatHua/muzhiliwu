//index javascript function by rolea.liu
$(document).ready(function(e) {
	
	//当移动到菜单上时淡出右侧主要内容
	$(".main_menu_ul").hover(function(){
		$(".manage_right").animate({
			opacity:0.3
		},"fast");
	},
	function(){
		$(".manage_right").animate({
			opacity:1
		},"fast");
	});
	
	//退出登录
	$("#head_logout").click(function(){
		$.ajax({
			url:"user/logout",
			type:"POST",
			success: function(data){
				obj=eval("("+data+")")
				if(obj.type==1)
				{
					window.location.href="index.html";
				}
			}
		})
	})
	
	$("#sidebar_logout").click(function(){
		$.ajax({
			url:"user/logout",
			type:"POST",
			success: function(data){
				obj=eval("("+data+")")
				if(obj.type==1)
				{
					window.location.href="index.html";
				}
			}
		})
	})
	
	//检查首页登录状态
	function checkLogin(){
		$.ajax({
			url:"user/me",
			type:"POST",
			success: function(data){
				var obj=eval("("+data+")");
				if(obj.type!=6)
				{
					$("#sidebar_user_pic").attr("src",obj.object.photo);
					$("#sidebar_user_name").html(obj.object.code);
					$("#sidebar_user_pp").html("人气值:"+obj.object.popularityValue);
					$("#sidebar_user_coin").html("拇指币:"+obj.object.muzhiCoin);
				}
				else {
					alert("抱歉，您尚未登录，请重新登陆~");
					window.location.href="index.html";
				}
			}
		})
	}
	
	//打开页面时验证是否已经登录
	checkLogin();
	
});