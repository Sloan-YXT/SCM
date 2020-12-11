<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import"%>
<%@page import="com.tomo.entity.Users"%>
<jsp:useBean id="user" class="com.tomo.entity.Users" scope="session"></jsp:useBean>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	user=(Users)request.getSession().getAttribute("user"); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>用户信息</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<script src="js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="js/data.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />

		<script type="text/javascript">
	$(document).ready(function() {
	$('#shopadd').click(function() {
			$('#sub-dialog').dialog( {
				height : 500,
				width : 350,
				modal : true
			});
		});

		$('#tf').submit(function() {
			var options = {
				url : '/second-hand/shop_add.do',
				type : 'POST',

				beforeSubmit : function() {
					alert('bdhcgdsyucvd');
				},
				success : succ
			};
			$(this).ajaxSubmit(options);
			return false; //不会跳转页面 
			});
		function succ(responseText, statusText) {
			alert(responseText);
		};
	 $( "#accordion" ).accordion({
      collapsible: true
    });

		$('#collect').click(function() {
		$('#right_td div').empty();
			$.ajax({
				url : "/second-hand/collect_list.do",
				type : "post",
				dataType : "txt",
				async : false,
				data : "username=<%=user.getUserName()%>",
				complete : function(result) {
					
				var parsedJson = jQuery.parseJSON(result.responseText);
				// 获取结果的条数
				json = parsedJson.data;
				$('#right_td div').prepend("<div id='accordion'></div>");
				for(var i=0;i<json.length;i++){
				$('#accordion').prepend("<h3>商品名称:&nbsp&nbsp"+json[i]['shopname']+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+"发布时间：&nbsp&nbsp"+ChangeDateFormat(json[i]['put_time'])+"</h3><div><p><br />商品名称： "
				+json[i]['shopname']+"<br />价格："
				+json[i]['price']+"<br />类别 ："
				+json[i]['category']+"<br />发布者信息   ："
				+json[i]['userName']+"&nbsp&nbsp&nbsp&nbsp&nbsp"
				+json[i]['school']+"&nbsp&nbsp&nbsp&nbsp&nbsp"
				+json[i]['court']+"<br />发布时间 ："
				+ChangeDateFormat(json[i]['put_time'])+"<br />联系电话  ："
				+json[i]['userPhone']+"<br />详细描述   ："
				+json[i]['description']+"</p></div>");
				}
					}
				});
				//<-----ajax end------>
				$( "#accordion" ).accordion({
      			collapsible: true
    				});
		});
		
		
		$('#shop').click(function() {
			$('#right_td div').empty();
			$.ajax({
				url : "/second-hand/shop_info.do",
							type : "post",
							dataType : "txt",
							async : false,
							data : "condition=username&pageNo=1&school=&value=<%=user.getUserName()%>",
				complete : function(result) {
					
				var parsedJson = jQuery.parseJSON(result.responseText);
				// 获取结果的条数
				json = parsedJson.data;
				$('#right_td div').prepend("<div id='accordion'></div>");
				for(var i=json.length-1;i>=0;i--){
				$('#accordion').prepend("<h3>商品名称:&nbsp&nbsp"+json[i]['shopname']+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+"发布时间：&nbsp&nbsp"+ChangeDateFormat(json[i]['put_time'])+"</h3><div><p><br />商品名称： "
				+json[i]['shopname']+"<br />价格："
				+json[i]['price']+"<br />类别 ："
				+json[i]['category']+"<br />发布者信息   ："
				+json[i]['userName']+"&nbsp&nbsp&nbsp&nbsp&nbsp"
				+json[i]['school']+"&nbsp&nbsp&nbsp&nbsp&nbsp"
				+json[i]['court']+"<br />发布时间 ："
				+ChangeDateFormat(json[i]['put_time'])+"<br />联系电话  ："
				+json[i]['userPhone']+"<br />详细描述   ："
				+json[i]['description']+"</p></div>");
				}
					}
				});
				//<-----ajax end------>
				$( "#accordion" ).accordion({
      			collapsible: true
    				});
			
		});
		$('#message').click(function() {
		$('#right_td div').empty();
			$.ajax({
				url : "/second-hand/msg_list.do",
				type : "post",
				dataType : "txt",
				async : false,
				data : "pageNo=1&receivename=<%=user.getUserName()%>",
				complete : function(result) {
				var parsedJson = jQuery.parseJSON(result.responseText);
				// 获取结果的条数
				json = parsedJson.data;
				$('#right_td div').prepend("<div id='accordion'></div>");
				for(var i=json.length-1;i>=0;i--){
				$('#accordion').prepend("<h3>留言者："+json[i]['username']+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+"留言时间："+ChangeDateFormat(json[i]['leave_time'])+"</h3><div><p>"+json[i]['content']+"</p></div>");
				
				}			
					}
				});
				//<-----ajax end------>
				$( "#accordion" ).accordion({
      			collapsible: true
    				});
		});
	})
</script>
		<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
		<link rel="stylesheet" type="text/css" href="css/user.css" />

	</head>

	<body style="background: url('images/10.jpg') repeat;">
<a href="<%=basePath%>/index.jsp " style="text-decoration: none;
	font-size: 20px;
	font: bold;
	color: #FFFFFF;">返回首页</a>
		<table width="819" height="353" border="0" cellspacing="0"
			cellpadding="0" background="#FFFFFF" style="margin: 0pt auto;">
			<tr>

				<td id="left_td" width="200px"  style="background: #FFFFFF;border-radius: 15px;margin-right: 10px">
					<div calss="menu" id="collect">
						<a href="#javascript">我的收藏</a>
					</div>
					<div calss="menu" id="shopadd">
						<a href="#javascript">发布商品</a>
					</div>
					<div calss="menu" id="shop">
						<a href="#javascript">我的发布</a>
					</div>
					<div calss="menu" id="message">
						<a href="#javascript">我的留言</a>
					</div>
				</td>
				<td id="right_td">
					<div style="width: 100%;height: 100%;margin-top: 200px;margin-left: 20px;">
						
					</div>
				</td>
			</tr>
		</table>

	<div id="sub-dialog" title="添加商品" style="display: none;">
			<form id='tf' action="/second-hand/shop_add.do"
				enctype="multipart/form-data">
				商品名称:
				<input type="text" id="shopname" name="shopname" style="margin: 8px auto;"  />
				发&nbsp;布&nbsp;人：<input type="text" readonly="readonly" id="userName" name="userName" style="margin: 8px auto;" value="<%=user.getUserName()%>" />
				商品介绍:
				<textarea id="description" name="description"
					style="height: 100px; width: 320px; margin: 8px auto;"></textarea>
				商品价格:
				<input type="text" id="price" name="price"  style="margin: 8px auto;" />
				联系电话:
				<input type="text" id="userPhone" name="userPhone"
					 style="margin: 8px auto;" />
				商品类型:
				<select name="category" style="margin: 8px auto;">
					<option value="电子产品" />
						电子产品
					</option>
					<option value="体育用品" />
						体育用品
					</option>
					<option value="办公用品" />
						办公用品
					</option>
					<option value="生活用品" />
						生活用品
					</option>
					<option value="自行车" />
						自行车
					</option>
					<option value="图书" />
						图书
					</option>
					<option value="电脑配件" />
						电脑配件
					</option>
				</select><br />

				<input type="submit"  style="margin: 0 auto" value="提交">
		</form>
		</div>
	</body>
</html>