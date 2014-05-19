<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>忘记密码了?</title>
<jsp:include page="/rs.jsp"></jsp:include>
</head>
<body>
<div>
<form action="${base}/usr/pwd_reset" method="post">
	<b>用户名</b><input name="name" type="text"><p/>
	<b>邮箱</b><input name="email" type="text"><p/>
	<b>验证码</b><input name="captcha" type="text"><p/>
	<img alt="" src="${base}/toolkit/captcha/update">
	<input type="submit"><p/>
</form>
</div>
</body>
</html>