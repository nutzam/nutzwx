<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请先登录</title>
<!-- 最新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="container" id="login_div">
<form action="${base}/usr/login" role="form">
	<div class="form-group">
    	<label for="login_name">Username</label>
    	<input name="name" type="text" class="form-control" id="login_name" placeholder="Enter Username">
  	</div>
	<div class="form-group">
    	<label for="login_pwd">Password</label>
    	<input name="passwd" type="password" class="form-control" id="login_pwd" placeholder="Enter Password">
  	</div>
	<div class="form-group">
    	<label for="login_captcha">Captcha</label>
    	<input name="captcha" type="text" class="form-control" id="login_captcha" placeholder="Enter Captcha">
  	</div>
  	<div>
  		<img alt="" src="${base}/toolkit/captcha/update" class="img-responsive">
  	</div>
	<button type="submit" class="btn btn-default">Submit</button>
</form>
</div>
<div>
<a href="${base}/pwd_reset.jsp">忘记密码?</a>
</div>
</body>
</html>