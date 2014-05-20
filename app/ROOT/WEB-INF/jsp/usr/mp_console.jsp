<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信控制台(lua)</title>
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
$(function() {
	$("#run_button").click(function() {
		$.ajax({
			data : $("#console_body").val(),
			method : "POST"
		}).success(function (j) {
			$("#exec_result").html(j);
		});
	});
});
</script>
</head>
<body>
<div>
<form action="#" id="console_form">
	<textarea rows="50" cols="100" id="console_body"></textarea>
	<button id="run_button">Run!</button>
</form>
</div>
<div>
	<div id="exec_result"></div>
</div>
</body>
</html>