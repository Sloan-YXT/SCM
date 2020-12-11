$(document).ready(function(){
	$("#sub").click(function(){
   var a=myform.name.value;  
   var b=myform.pwd.value;  
   alert(a+b);
   $.ajax({
	      url: "/second-hand/login.do",
	      type: "post",
	      dataType: "txt",
	      data: "username="+a+"&pwd="+b,
	      complete: function(result){
	   if(result.responseText=="error1"){
		   alert("用户不存在！");
	   }else if(result.responseText=="error2"){
		   alert("密码错误！");
	   }else{
		   alert("登录成功！");
		  　
	   }
	      }
	    });
   
  }) 
// <------End---sub.click--->
  
});
