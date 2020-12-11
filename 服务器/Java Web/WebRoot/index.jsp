<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.tomo.entity.Users"%>
<jsp:useBean id="user" class="com.tomo.entity.Users" scope="session"></jsp:useBean>
<%
	//获取项目的路径
	String path = request.getContextPath();
	//协议名称+://+服务名称+端口号+项目路径
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
					
	user=(Users)request.getSession().getAttribute("user"); 
%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<link href="<%=basePath%>css/index.css" rel="stylesheet"
			type="text/css" />

		<title>校园淘</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
		
		<script src="js/modernizr.js" type="text/javascript"></script>
		<script src="js/jquerysession.js" type="text/javascript"></script>
		<script src="js/index.js" type="text/javascript"></script>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.x.x/css/swiper.css">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/js/swiper.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/js/swiper.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/js/swiper.esm.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/js/swiper.esm.bundle.js"></script>
 
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.0.2/css/swiper.min.css">
		<link rel="stylesheet" type="text/css" href="css/reset.css" />
		<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<link rel="stylesheet" type="text/css" href="css/index.css">

	</head>

	<body style="background: url('images/10.jpg') repeat;">

		<div id="title" style="margin-top: -50px ;">
			<br><a href="#javascript" id="person">欢迎登录！</a>
			<a id="school"></a>
			<a href="#javascript" id="exit">退出登录</a>
		</div>
		
		<div class="swiper-container" style=" width: 700px;height: 350px;margin-top: 50px">
   		 	<div class="swiper-wrapper">
        	<div class="swiper-slide"><img src="images/welcome_1.jpg" style="width: 700px"/></div>
        	<div class="swiper-slide"><img src="images/welcome_2.jpg" style="width: 700px"/></div>
        	<div class="swiper-slide"><img src="images/welcome_3.jpg" style="width: 700px"/></div>
        	<div class="swiper-slide"><img src="images/welcome_4.jpg" style="width: 700px"/></div>
   		</div>
   			
    
   				 <!-- 如果需要导航按钮 -->
   				 <div class="swiper-button-prev"></div>
   				 <div class="swiper-button-next"></div>
    
   			
		</div>
		
		<div id="container">
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">体育用品</p> </a>
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">自行车</p> </a>
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">生活用品</p> </a>
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">电子产品</p> </a>
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">图书</p> </a>
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">办公用品</p> </a>
			<a id="dao" style="display: inline; width: 100px;" href="#javascript"><p
					class="daohang">电脑配件</p> </a>
			
		</div>
		<div id="content">
			<P></P>
			<div style="margin-bottom: 20px">
				<p style="display: inline;">
					共搜索到
				</p>
				<p id="num"
					style="display: inline; margin-left: 5px; margin-right: 5px; color: #FF0000; font-style: normal;"></p>
				<p style="display: inline;">
					条数据
				</p>
			</div>
			<div class="accordion">
				<dl>
				</dl>
			</div>
			
		</div>
		<div id="dialog-modal-message" title="留言" style="display: none">
		
		<textarea  id="txetarea" style="height: 180px;width: 450px; margin: 0 auto;"></textarea>
  		<input  type="button" id="messagesub" value="提交" style="background: #2BA659;margin:10px 0 0 200px;padding:5px 10px 5px  10px; border: 0px;"/>
		</div>
		<div id="dialog-modal-login" title="登录" style="display: none">
		
		用户名：<input type="text" name="name" id="name" style="margin: 15px 0 15px 15px" /><br />
  		密&nbsp&nbsp&nbsp码：<input type="password" name= "pwd" id="pwd" style="margin: 15px 0 15px 15px"/><br />
		<input  type="button" id="loginsub" value="登录" style="background: #2BA659;  margin:10px 0 0  100px;padding:5px 10px 5px  10px; border: 0px;"/>
		<input  type="button" id="reseigsub" value="注册" style="background: #2BA659;  margin:10px 0 0  30px;padding:5px 10px 5px  10px; border: 0px;"/>
		</div>
		<div id="dialog-modal-regeiset" title="注册" style="display: none">
		
		邮箱：<input type="text" id="emil" style="margin: 15px 0 15px 15px" /><br />
  		昵称：<input type="text" id="username" style="margin: 15px 0 15px 15px"/><br />
  		密码：<input type="password" id="repwd" style="margin: 15px 0 15px 15px"/><br />
  		学校：<input type="text" id="reschool" style="margin: 15px 0 15px 15px"/><br />
  		学院：<input type="text" id="xueyuan" style="margin: 15px 0 15px 15px"/><br />
  		专业：<input type="text" id="zhuanye" style="margin: 15px 0 15px 15px"/><br />
		<input  type="button" id="reseigtsub" value="注册" style="background: #2BA659;  margin:10px 0 0  120px;padding:5px 10px 5px  10px; border: 0px;"/>
		</div>
		<div style="width: 100%; height: 80px"></div>
		<div style=" background:#FFFFFF;  width: 100% ;height:50px; margin: 0 auto;"><p align="center" style="margin-top: 10px;">BY &nbspteam seven&nbsp&nbsp&nbsp@2020-7</p></div>
		<script>        
  var mySwiper = new Swiper ('.swiper-container', {
    direction: 'vertical',
    loop: true,
    autoplay:true,//等同于以下设置
  	autoplay: {
    delay: 3000,
    stopOnLastSlide: false,
    disableOnInteraction: true,
    },
    effect : 'fade',
    
    // 如果需要前进后退按钮
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
    
   
  })        
  </script>
	</body>
	<script type="text/javascript">
	$(document).ready(function() {	
	if('<%=user.getUserName()%>'!='null'){
		$("#person").text('<%=user.getUserName()%>');
		$("#person").attr('href','<%=basePath%>user.jsp');
		$("#school").text('<%= user.getSchool()%>');
	}
		$('#person').click(function(){
		if('<%=user.getUserName()%>'=='null'){
					$( "#dialog-modal-login" ).dialog({
				      height: 250,
				     width: 380,
				      modal: true
				    });
				  }else{}
		});
	$('#exit').click(function(){
	
	alert("退出成功！ ");
	location.reload();
	
	});
	$('.collect').click(function(){
			if('<%=user.getUserName()%>'!='null'){
			$.ajax({
				url : "/second-hand/collection_add.do",
				type : "post",
				dataType : "txt",
				async : false,
				data : "shopid="+$(this).siblings('.shopid').text()+
				"&username=<%=user.getUserName()%>",
				complete : function(result) {
					alert(result.responseText);
					}
				});
			}else{
				alert("请先登录");
			}			
		});
		//<------------collect_add_end------------->
	$('.message').click(function(){
	var receivename=$(this).siblings(".receivename").text();
	
  	if('<%=user.getUserName()%>'!='null'){
	$( "#dialog-modal-message" ).dialog({
      height: 300,
     width: 500,
      modal: true
    });
    $("#messagesub").button().click(function( event ) {
  
		$.ajax({
			url : "/second-hand/msg_add.do",
			type : "post",
			dataType : "txt",
			async : false,
			data : "content="+$('#txetarea').val()+
			"&username=<%=user.getUserName()%>&receivename="+receivename,
			complete : function(result) {
			alert(result.responseText);
			$( "#dialog-modal" ).dialog("close");
			}
			});
			});
			}else{
			alert("请先登录！");
			}
    });	
	//<------------留言_add_end------------->
    
})</script>

</html>
