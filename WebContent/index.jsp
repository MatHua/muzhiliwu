<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=">
<title>测试</title>
<script src="http://localhost:8080/muzhiliwu/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	    $(document).ready(function(){
			$("#btn").click(function(){
			  var name=$("#name").val();
			  var passwd=$("#pass").val();
			  var repass=$("#repass").val();
			  
			    $.post("http://localhost:8080/muzhiliwu/user/regist",{"user.code":name,"user.pass":passwd,"repass":repass}, function(json){
			    	alert(json);
				}); 
			});
		});
  
    </script>
</head>
<body>
<form action="" method="post">
	<span>用名:</span><input type="text" name="name" id="name"><br>
    <span>密码:</span><input type="password" name="pass" id="pass"><br>
    <span>重复:</span><input type="password" name="repass" id="repass"><br>
    <input type="button" id="btn" style="width:30px;">                    
</form>
</body>
</html>