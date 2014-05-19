<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户之家</title>
<link rel="stylesheet" href="${base}/rs/css/slick.grid.css" type="text/css"/>
<link rel="stylesheet" href="${base}/rs/css/smoothness/jquery-ui-1.8.16.custom.css" type="text/css"/>
<jsp:include page="/rs.jsp"></jsp:include>
<script src="${base}/rs/js/jquery.event.drag-2.2.js" type="text/javascript"></script>
<script src="${base}/rs/js/slick.core.js"></script>
<script src="${base}/rs/js/slick.grid.js"></script>
<script type="text/javascript">
var grid;
var columns = [
               //{id :"owner", name: "", field: ""},
               //{id :"disable", name: "", field: ""},
               {id :"openid", name: "openid", field: "openid"},
               //{id :"alias", name: "", field: ""},
               //{id :"nickname", name: "昵称", field: ""},
               {id :"token", name: "token", field: "token"},
               {id :"appid", name: "appid", field: "appid"},
               {id :"appsecret", name: "appsecret", field: "appsecret"},
               {id :"access_token", name: "access_token", field: "access_token"},
               //{id :"access_token_expires", name: "access_token_expires", field: ""}
];

var options = {
		editable: true,
  		enableCellNavigation: true,
  		enableColumnReorder: false
};

$(function() {
	$.getJSON("${base}/curd/query/wxmpinfo").success(function(data) {
		console.log(data);
		grid = new Slick.Grid("#myGrid", data.list, columns, options);
	});
});
</script>
</head>
<body>
<div style="position:relative">
	<b>您帐号下的公众帐号列表</b>
	<div id="usr_mp_list" style="width:600px;">
		<div id="myGrid" style="width:600px;height:500px;"></div>
	</div>
</div>
</body>
</html>