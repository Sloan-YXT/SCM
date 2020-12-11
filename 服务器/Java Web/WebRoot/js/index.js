$(document)
		.ready(
				function() {
					
					
					
				    $("#reseigsub").button().click(function( event ) {
				    	
				    $( "#dialog-modal-regeiset" ).dialog({
				      height: 500,
				     width: 350,
				      modal: true
				    });
				    $("#reseigtsub").button().click(function( event ) {
				    	var emil=$('#emil').val();
				    	var username=$('#username').val();
				    	var pwd=$('#repwd').val();
				    	var school=$('#reschool').val();
				    	var xueyuan=$('#xueyuan').val();
				    	var zhuanye=$('#zhuanye').val();
				    	$.ajax({
						      url: "/second-hand/add_user.do",
						      type: "post",
						      dataType: "txt",
						      data: "email="+emil+"&username="+username+"&pwd="+pwd+"&school="+school+"&court="+xueyuan+"&professional="+zhuanye,
						      complete: function(result){
						   alert(result.responseText);
				    		$( "#dialog-modal-regeiset" ).dialog("close");
						      }
						    });
				    });
				    
				    });
				   
				    $("#loginsub").button().click(function( event ) {
					    var name=$('#name').val();
					    var pwd=$('#pwd').val();
					    $.ajax({
						      url: "/second-hand/login.do",
						      type: "post",
						      dataType: "txt",
						      data: "username="+name+"&pwd="+pwd,
						      complete: function(result){
						   if(result.responseText=="error1"){
							   alert("用户不存在！");
						   }else if(result.responseText=="error2"){
							   alert("密码错误！");
						   }else{
							   alert("登录成功！");
							   location.reload();
							  　
						   }
						      }
						    });
					    $( "#dialog-modal-login" ).dialog("close");
					    });
				   
			
						
						//<------------login_end------------->
					
					var num = 0;
					var json;
					var value = "办公用品";
					initdata();
					accord();
					$('.shopid').hide();
					$('.receivename').hide();
					$('.daohang').click(function() {
						if($(this).text()!="个人中心"){
						
						value = $(this).text();
						$('dl').empty();
						
						initdata();
						accord();}
					});
					
					
					
					function initdata() {
						
						
						$.ajax({
							url : "/second-hand/shop_info.do",
							type : "post",
							dataType : "txt",
							async : false,
							data : "condition=category&pageNo=1&school=&value="
									+ value,
							complete : function(result) {
								var parsedJson = jQuery
										.parseJSON(result.responseText);
								// 获取结果的条数
								json = parsedJson.data;
								$("#num").text(json.length);
								num = json.length;
							}
						});

						for ( var i = 0; i < num; i++) {
							/*
							 * 将服务器返回来的数据解析到页面上 category court description
							 * 商品的详细描述 picture put_time school 商家的学校 shopId 商品id
							 * shopname 商品名称 userName 商家名称 userPhone 商家电话
							 * 
							 */

							$("dl")
									.prepend(
											"<dt><a href='#accordion3' aria-expanded='false' aria-controls='accordion3' class='accordion-title accordionTitle js-accordionTrigger'>"
													+ "<table><tr><td class='name'>"
													+ json[i]["shopname"]
													+ "</td>"
													+ "<td class='price'>"
													+ json[i]["price"]
													+ "</td>"
													+ "<td class='school'>"
													+ json[i]["school"]
													+ "</td></tr></table></a></dt>"
													+ "<dd class='accordion-content accordionItem is-collapsed' id='accordion3' aria-hidden='true'>"
													+ "<div class='jieshao'>商家名称：&nbsp&nbsp&nbsp&nbsp&nbsp<b>"
													+ json[i]["userName"]
													+ "</b><br />商家电话:&nbsp&nbsp&nbsp&nbsp&nbsp<b>"
													+ json[i]["userPhone"]
													+ "</b><br />商家专业信息:&nbsp&nbsp&nbsp&nbsp&nbsp<b>"
													+ json[i]["school"]
													+ "&nbsp&nbsp&nbsp&nbsp&nbsp"
													+ json[i]["court"]
													+ "</b><br />详细介绍:&nbsp&nbsp&nbsp&nbsp&nbsp<b>"
													+ json[i]["description"]
													+ "</b></div>" 
													+ "<span class='receivename' style='display:none;'>"+json[i]["userName"]+"</span>"
													+ "<span class='shopid' style='display:none;' >"+json[i]["shopId"]+"</span>"
													+"<a href='#javascript' class='collect image' >收藏</a>"
													+"<a href='#javascript' class='message image' >留言</a>"
													+"</dd>");
						}

					};
					// <------End---data.init--->

					// uses classList, setAttribute, and querySelectorAll
					// if you want this to work in IE8/9 youll need to polyfill
					// these
					function accord() {
						var d = document, accordionToggles = d
								.querySelectorAll('.js-accordionTrigger'), setAria, setAccordionAria, switchAccordion, touchSupported = ('ontouchstart' in window), pointerSupported = ('pointerdown' in window);

						skipClickDelay = function(e) {
							e.preventDefault();
							e.target.click();
						}

						setAriaAttr = function(el, ariaType, newProperty) {
							el.setAttribute(ariaType, newProperty);
						};
						setAccordionAria = function(el1, el2, expanded) {
							switch (expanded) {
							case "true":
								setAriaAttr(el1, 'aria-expanded', 'true');
								setAriaAttr(el2, 'aria-hidden', 'false');
								break;
							case "false":
								setAriaAttr(el1, 'aria-expanded', 'false');
								setAriaAttr(el2, 'aria-hidden', 'true');
								break;
							default:
								break;
							}
						};
						// function
						switchAccordion = function(e) {
							console.log("triggered");
							e.preventDefault();
							var thisAnswer = e.target.parentNode.nextElementSibling;
							var thisQuestion = e.target;
							if (thisAnswer.classList.contains('is-collapsed')) {
								setAccordionAria(thisQuestion, thisAnswer,
										'true');
							} else {
								setAccordionAria(thisQuestion, thisAnswer,
										'false');
							}
							thisQuestion.classList.toggle('is-collapsed');
							thisQuestion.classList.toggle('is-expanded');
							thisAnswer.classList.toggle('is-collapsed');
							thisAnswer.classList.toggle('is-expanded');

							thisAnswer.classList.toggle('animateIn');
						};
						for ( var i = 0, len = accordionToggles.length; i < len; i++) {
							if (touchSupported) {
								accordionToggles[i].addEventListener(
										'touchstart', skipClickDelay, false);
							}
							if (pointerSupported) {
								accordionToggles[i].addEventListener(
										'pointerdown', skipClickDelay, false);
							}
							accordionToggles[i].addEventListener('click',
									switchAccordion, false);
						}
					}
				});