function ifr(ifrid,url,w,h){

	document.write('<iframe id="'+ifrid+'" name="'+ifrid+'" width="'+w+'" height="'+h+'"   style="border:1px solid #abb4b9;" frameborder="0" src="'+url+'" ></iframe>');
}
	
function bindTable2(tableId,tableJson,isDelRow){
	var tableData=tableJson.data;
	var trInfo=$("#"+tableId+" tbody tr:first").html();
	if (tableId.indexOf("table_edit")!=-1) isDelRow=true;//约定:可增删行的表格id以table_edit开头
	if (isDelRow) {$("#"+tableId+" tbody tr:not(:first)").remove();}
	var keepRow=$("#"+tableId+" tbody tr").length-1;//页面中保留行数
	var i;

	for (i=0;i<tableData.length;i=i+1){
		var rowJson=tableData[i];
		var newTr=trInfo;
		for(var j in rowJson){
			var jValue=eval("rowJson."+j);
			jValue=filterTimestamp(jValue);
			var re=eval("/{"+j+"}/ig");
			newTr=newTr.replace(re,jValue);
		}
		newTr=newTr.replace(/{.*?}/ig,"");//清空没有数据的单元格
		$("#"+tableId+" tbody tr:eq("+i+")").after("<tr>"+newTr+"</tr>");
		
		if (i<keepRow)	{$("#"+tableId+" tbody tr:last").remove();}
		//支持select的绑定,select的option值通过jjd.setSelect传入
		$("#"+tableId+" tbody tr:eq("+(i+1)+") select").each(
			function(i){$(this).val(eval("rowJson."+this.name));}
		);
		
		$("#"+tableId+" tbody tr:eq("+(i+1)+")").css("cursor","hand");  //加上小手样式
	}
	//$("#"+tableId+" tbody tr:even").css("background","#EDEDEB");
	//翻页时清空余下的行
	for (1==1;i<keepRow;i=i+1){
		$("#"+tableId+" tbody tr:eq("+(i+1)+") td").empty();
		//翻页时出现第一页的小手样式
		$("#"+tableId+" tbody tr:eq("+(i+1)+")").removeAttr("style");
	}
	
	if (typeof(tableJson.page)!="undefined"){//如果存在导航条
		bindTableListNav2(tableJson.page,tableId);
	}
	
	
	sys_getCurPageGuids();					//数据绑定Table后加载数据
	bind_floatDiv2(tableId);  //绑定页面显示模式
	//绑定hover样式。
	$('#'+tableId).tableHover();
}
function bindTableListNav2(pageInfo,tableId){
	var prefix="";//选择器前缀
	
	if(tableId!=null&&tableId!=''&&tableId!='null'&&tableId!='undefined'){
		prefix="#nav_"+tableId+" ";
		tableId="_"+tableId;
	}
	$(prefix+"span[navid=page_cur]").text(pageInfo.page_cur==0?1:pageInfo.page_cur);
	$(prefix+"span[navid=page_allPage]").text(pageInfo.page_allPage==0?1:pageInfo.page_allPage);
	$(prefix+"span[navid=page_allCount]").text(pageInfo.page_allCount);
	$(prefix+"span[navid=page_size]").text(pageInfo.page_size);
	
	var pageNum=parseInt(pageInfo.page_cur);
	var page_pre=pageNum-1;
	var page_next=pageNum+1;
	if(pageNum>1&&(parseInt(pageInfo.page_allPage)>1)){
		$(prefix+"td[navid=first_page_td]").addClass('').removeClass('');
		$(prefix+"a[navid=first_page]").unbind();
		$(prefix+"a[navid=first_page]").click(function(){
			if (page_pre===0){window.alert("已到达第一页");}		
			else{eval("callback_getPageData"+tableId+"(1,$(this).parent())");}
		});
	}else{
		$(prefix+"a[navid=first_page]").unbind();
		$(prefix+"td[navid=first_page_td]").addClass('').removeClass('font12blue');
	}
	if(parseInt(pageInfo.page_allPage)<=1||pageNum==parseInt(pageInfo.page_allPage)){
		$(prefix+"td[navid=last_page_td]").addClass('').removeClass('font12blue');
		$(prefix+"a[navid=last_page]").unbind();

	}else{
		$(prefix+"td[navid=last_page_td]").addClass('').removeClass('');
		$(prefix+"a[navid=last_page]").unbind();
		$(prefix+"a[navid=last_page]").click(function(){
			eval("callback_getPageData"+tableId+"("+pageInfo.page_allPage+",$(this).parent())");
		});
	}
	$(prefix+"a[navid=page_pre]").unbind();
	$(prefix+"a[navid=page_pre]").click(function(){
		if(pageNum==1){
			window.alert("已是第一页");return false;
		}
		eval("callback_getPageData"+tableId+"("+page_pre+",$(this).parent())");
	});
	$(prefix+"a[navid=page_next]").unbind();
	$(prefix+"a[navid=page_next]").click(function(){
		if (page_next>parseInt(pageInfo.page_allPage)) {window.alert("已是最后一页");return false;}
		eval("callback_getPageData"+tableId+"("+page_next+",$(this).parent())");return false;
	});
	$(prefix+"a[navid=page_enter]").unbind();
	$(prefix+"a[navid=page_enter]").click(function(){
		var _goto = $(prefix+"input[navid=page_goto]").val();
		if (isNaN(_goto)){window.alert("请输入有效的页码");return false;}
		if ((_goto>parseInt(pageInfo.page_allPage))||(_goto<1)) {window.alert("请输入有效的页码");return false;}
		eval("callback_getPageData"+tableId+"("+_goto+",$(this).parent())");
	});
	//if(sys_navDivId_postion.top!="-1000") return false;
	$(prefix+"input[navid=page_goto]").unbind("keyup");
	$(prefix+"input[navid=page_goto]").keyup(function(event){
	  if (event.keyCode=="13"){
		var _goto = $(this).val();
		if (isNaN(_goto)){window.alert("请输入有效的页码");return false;}
		if ((_goto>parseInt(pageInfo.page_allPage))||(_goto<1)) {window.alert("请输入有效的页码");return false;}
		eval("callback_getPageData"+tableId+"("+_goto+",$(this).parent().parent())");
   	  }
   });
}
//点击浮出模式
function bind_floatDiv2(table_id){
	$("#"+table_id+" tbody tr:not(:first)").each(function(index){
		jQuery(this).unbind();
		var text=$("td:first",$(this)).html();
		if(text){
			$(this).click(function(event){
                	var cn = event.target;
                	if( cn && (cn.nodeName.toLowerCase() == "td" || cn.nodeName.toLowerCase() == "span")){
                		//没有callback_trclick_tableid方法时，不执行任何操作
                		try{
                			eval("callback_trclick_"+table_id+"('"+text+"')");
                		}catch(exception){
                		}
                	}
			});
		}
		
	});
}

//此后增加iframe之间互相刷新的代码
var iframe_cur_li_id="";
function refresh_frames(left_url,left_frame_width){
	if(leftframe!=null&&leftframe!='undefined'){
	leftframe.location.href=left_url;
	if(left_frame_width){
		$("#left_td").css("width",left_frame_width);
	}
	default_left_width=left_frame_width;
	}
}
function refresh_frames_and_chg_class(li_id,left_frame_width){
	var cur_page_url=window.location.pathname;
	var urlcons=cur_page_url.split("/");
	var base_page_url="";
	for(var urlindex=1;urlindex<urlcons.length-1;urlindex++){
		base_page_url+="/"+urlcons[urlindex];
	}
	var left_url=base_page_url+"/"+li_id+"left.jsp";
	var right_url=base_page_url+"/"+li_id+"rightbody.jsp";
	if(li_id==iframe_cur_li_id){
		return;
	}else{
		if(iframe_cur_li_id!=""){
			document.getElementById(iframe_cur_li_id).className="";
		}else{
			$("li.curr").addClass('').removeClass('curr');
		}
		document.getElementById(li_id).className="curr";
		iframe_cur_li_id=li_id;
		//刷新到新的页面的时候，所有页面都禁用onbeforeunload事件。
		var frame_loadeds=document.getElementsByName("frame_loaded");
		for(var flindex=0;flindex<frame_loadeds.length;flindex++){
			frame_loadeds[flindex].value="0";
		}
		parent.refresh_frames(left_url,left_frame_width);
		rightmainfr.location.href=right_url;
		//刷新完新页面，当前页面启用onbeforeunload事件。
		$("#"+li_id+"_loaded").val("1");
		$("#"+li_id+"_left_loaded").val("1");
	}
	setTimeout(function (){parent.resize();},800);
}

function disable_right_onbeforeunload(li_id){
	$(window.frames["rightframe"].document).find("#"+li_id+"_loaded").val("0");
}

function enable_right_onbeforeunload(li_id){
	$(window.frames["rightframe"].document).find("#"+li_id+"_loaded").val("1");
}

function load_right_frame(li_id,right_frame_url){
	//刷新右边页面的时候，右边的页面禁用onbeforeunload事件。
	parent.disable_right_onbeforeunload(li_id);
	var pageUrl = right_frame_url;
	parent.rightframe.rightmainfr.location.href = pageUrl;
	//刷新右边页面完毕，启用右边页面onbeforeunload事件。
	parent.enable_right_onbeforeunload(li_id);
}
//右面页面的onbeforeunload事件
function refresh_cur_page(li_id){
	var left_frame_url=window.location.pathname.replace("rightbody.jsp","left.jsp");
	var gridshowdisplay_loaded=parent.document.getElementById(li_id+"_loaded").value;
	if(gridshowdisplay_loaded=='1'){
		parent.document.getElementById(li_id+"_left_loaded").value="0";
		parent.parent.refresh_frames(left_frame_url);
		parent.document.getElementById(li_id+"_left_loaded").value="1";
	}
}

function refresh_cur_left_page(li_id){
	var gridshowdisplay_left_loaded=parent.rightframe.document.getElementById(li_id+"_left_loaded").value;
	if(gridshowdisplay_left_loaded=="1"){
		var right_frame_url=window.location.pathname.replace("left.jsp","rightbody.jsp");
		load_right_frame(li_id,right_frame_url);
	}
}

//中间分隔条
var is_left_hide=false;
var is_right_hide=false;
var frame_height=screen.height-266;
var body_frame_height=screen.height-303;
var default_left_width;
function hide_left(){
	if(!is_left_hide){
		$("#left_td").hide();
		$("#middle_bar_img2").hide();
		document.getElementById("middle_bar_img").src=sys_ctx+"/images/zhedie1.gif";
		rightframe.rightmainfr.expandMapDiv();
	}else{
		$("#left_td").show();
		$("#middle_bar_img2").show();
		document.getElementById("middle_bar_img").src=sys_ctx+"/images/zhedie2.gif";
		rightframe.rightmainfr.exposeMapDiv();
	}
	is_left_hide=!is_left_hide;
}
function hide_right(){
	if(!is_right_hide){
		$("#right_td").hide();
		$("#middle_bar_img").hide();
		var main_width = screen.width * 1 - 10;
		$("#left_td").css('width',main_width);
		document.getElementById("middle_bar_img2").src=sys_ctx+"/images/zhedie2.gif";
		if(leftframe!=null&&leftframe!='undefined'){
		leftframe.expandMapDiv();
		}
	}else{
		$("#right_td").show();
		$("#middle_bar_img").show();
		$("#left_td").css('width',default_left_width);
		document.getElementById("middle_bar_img2").src=sys_ctx+"/images/zhedie1.gif";
		if(default_left_width=="200px"){
			leftframe.exposeMapDiv(default_left_width);
		}else{
			var left_frame_map_width=screen.width/4-3;
			leftframe.exposeMapDiv(left_frame_map_width);
		}
	}
	is_right_hide=!is_right_hide;
}
function bindSelectOption(){
//绑定select框onchange事件
if($("#cx_jd").length>0){
	$("#cx_jd").change(function(event){
		var querystr="orgLevel=2";
		querystr+="&jd_id="+$("#cx_jd").val();		
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}else{

}

if($("#cx_pq").length>0){
	$("#cx_pq").change(function(event){
		var querystr="orgLevel=3";
		querystr+="&pq_id="+$("#cx_pq").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_sq").length>0){
	$("#cx_sq").change(function(event){
		var querystr="orgLevel=4";
		querystr+="&sq_id="+$("#cx_sq").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_wg").length>0){
	$("#cx_wg").change(function(event){
		var querystr="orgLevel=5";
		querystr+="&wg_id="+$("#cx_wg").val();
		if($("#cx_xq").length>0){
			querystr+="&xq_id="+$("#cx_xq").val();
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_xq").length>0){
	$("#cx_xq").change(function(event){
		var querystr="orgLevel=5";
		querystr+="&xq_id="+$("#cx_xq").val();
		if($("#cx_wg").length>0){
			querystr+="&wg_id="+$("#cx_wg").val();
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}
if($("#cx_ld").length>0){
	$("#cx_ld").change(function(event){
		var querystr="orgLevel=6";
		querystr+="&ld_id="+$("#cx_ld").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
			var fwlx = json.formData.fwlx;
			$("#fw_span").text('');
			if(fwlx=='1')
			{				
				$("#cx_dy").hide();
				$("#cx_lc").hide();
				if($("#cx_fw")){
					$("#cx_fw").hide();
					if($("#fw_span")){
						$("#fw_span").text(''+json.formData.ld_mc);
					};
				};
				if($("#cx_fw_label")){
					$("#cx_fw_label").hide();
				};
				if($("#cx_dy_lable"))
				{
					$("#cx_dy_lable").hide();
				}
				if($("#cx_lc_lable"))
				{
					$("#cx_lc_lable").hide();
				}
				if($("#cx_dy_td"))
				{
					$("#cx_dy_td").hide();
				}
				if($("#cx_lc_td"))
				{
					$("#cx_lc_td").hide();
				}
			}else
			{
				$("#cx_dy").show();
				$("#cx_lc").show();
				if($("#cx_dy_lable"))
				{
					$("#cx_dy_lable").show();
				}
				if($("#cx_lc_lable"))
				{
					$("#cx_lc_lable").show();
				}
				if($("#cx_fw")){
					$("#cx_fw").show();
				};
				if($("#cx_fw_label")){
					$("#cx_fw_label").show();
				};
				if($("#cx_dy_td"))
				{
					$("#cx_dy_td").show();
				}
				if($("#cx_lc_td"))
				{
					$("#cx_lc_td").show();
				}
			}
			
		});
	});
}

if($("#cx_dy").length>0){
	$("#cx_dy").change(function(event){
		var querystr="orgLevel=7";
		querystr+="&dy_id="+$("#cx_dy").val();
		if($("#cx_lc").length>0){
			querystr+="&lc_id="+$("#cx_lc").val();
		}
		if($("#cx_ld").length>0){
			querystr+="&ld_id="+$("#cx_ld").val();
		}else{
			alert("请选择楼栋！");
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_lc").length>0){
	$("#cx_lc").change(function(event){
		var querystr="orgLevel=7";
		querystr+="&lc_id="+$("#cx_lc").val();
		if($("#cx_dy").length>0){
			querystr+="&dy_id="+$("#cx_dy").val();
		}
		if($("#cx_ld").length>0){
			querystr+="&ld_id="+$("#cx_ld").val();
		}else{
			alert("请选择楼栋！");
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange", querystr, function (json) {
			bind(json);
		});
	});
}
}

function bindSelectOption2(){
	//绑定select框onchange事件
if($("#cx_jd2").length>0){
	$("#cx_jd2").change(function(event){
		var querystr="orgLevel=2";
		querystr+="&jd_id2="+$("#cx_jd2").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}else{

}

if($("#cx_pq2").length>0){
	$("#cx_pq2").change(function(event){
		var querystr="orgLevel=3";
		querystr+="&pq_id2="+$("#cx_pq2").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_sq2").length>0){
	$("#cx_sq2").change(function(event){
		var querystr="orgLevel=4";
		querystr+="&sq_id2="+$("#cx_sq2").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_wg2").length>0){
	$("#cx_wg2").change(function(event){
		var querystr="orgLevel=5";
		querystr+="&wg_id2="+$("#cx_wg2").val();
		if($("#cx_xq2").length>0){
			querystr+="&xq_id2="+$("#cx_xq2").val();
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_xq2").length>0){
	$("#cx_xq2").change(function(event){
		var querystr="orgLevel=5";
		querystr+="&xq_id2="+$("#cx_xq2").val();
		if($("#cx_wg2").length>0){
			querystr+="&wg_id2="+$("#cx_wg2").val();
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}
if($("#cx_ld2").length>0){
	$("#cx_ld2").change(function(event){
		var querystr="orgLevel=6";
		querystr+="&ld_id2="+$("#cx_ld2").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_dy2").length>0){
	$("#cx_dy2").change(function(event){
		var querystr="orgLevel=7";
		querystr+="&dy_id2="+$("#cx_dy2").val();
		if($("#cx_lc2").length>0){
			querystr+="&lc_id2="+$("#cx_lc2").val();
		}
		if($("#cx_ld2").length>0){
			querystr+="&ld_id2="+$("#cx_ld2").val();
		}else{
			alert("请选择楼栋！");
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#cx_lc2").length>0){
	$("#cx_lc2").change(function(event){
		var querystr="orgLevel=7";
		querystr+="&lc_id2="+$("#cx_lc2").val();
		if($("#cx_dy2").length>0){
			querystr+="&dy_id2="+$("#cx_dy2").val();
		}
		if($("#cx_ld2").length>0){
			querystr+="&ld_id2="+$("#cx_ld2").val();
		}else{
			alert("请选择楼栋！");
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange2", querystr, function (json) {
			bind(json);
		});
	});
}
}

function bindSelectOption0(){
	//绑定select框onchange事件
if($("#ssjd").length>0){
	$("#ssjd").change(function(event){
		var querystr="orgLevel=2";
		querystr+="&ssjd="+$("#ssjd").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}else{

}

if($("#sspq").length>0){
	$("#sspq").change(function(event){
		var querystr="orgLevel=3";
		querystr+="&sspq="+$("#sspq").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#sssq").length>0){
	$("#sssq").change(function(event){
		var querystr="orgLevel=4";
		querystr+="&sssq="+$("#sssq").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#sswg").length>0){
	$("#sswg").change(function(event){
		var querystr="orgLevel=5";
		querystr+="&sswg="+$("#sswg").val();
		if($("#ssxq").length>0){
			querystr+="&ssxq="+$("#ssxq").val();
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#ssxq").length>0){
	$("#ssxq").change(function(event){
		var querystr="orgLevel=5";
		querystr+="&ssxq="+$("#ssxq").val();
		if($("#sswg").length>0){
			querystr+="&sswg="+$("#sswg").val();
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}
if($("#ssld").length>0){
	$("#ssld").change(function(event){
		var querystr="orgLevel=6";
		querystr+="&ssld="+$("#ssld").val();
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#ssdy").length>0){
	$("#ssdy").change(function(event){
		var querystr="orgLevel=7";
		querystr+="&ssdy="+$("#ssdy").val();
		if($("#sslc").length>0){
			querystr+="&sslc="+$("#sslc").val();
		}
		if($("#ssld").length>0){
			querystr+="&ssld="+$("#ssld").val();
		}else{
			alert("请选择楼栋！");
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}

if($("#sslc").length>0){
	$("#sslc").change(function(event){
		var querystr="orgLevel=7";
		querystr+="&sslc="+$("#sslc").val();
		if($("#ssdy").length>0){
			querystr+="&ssdy="+$("#ssdy").val();
		}
		if($("#ssld").length>0){
			querystr+="&ssld="+$("#ssld").val();
		}else{
			alert("请选择楼栋！");
		}
		sys_ajaxPost("/select/default.do?method=selectOnChange0", querystr, function (json) {
			bind(json);
		});
	});
}
}

/*
 * 绑定受理主体下拉列表框（矛调）		Li Shenjie / 2014.1.24
 * @param {Object} select_group	一组 select 下拉选择框
 * @param {Object} user_level	登录用户级别
 */
function bindSelectOptionAccept(select_group, user_level) {
	//绑定select框onchange事件
	if (select_group[0].length > 0) {
		$("#" + select_group[0].id).change(
				function(event) {
					var querystr = "selectNumber=1&userLevel=" + user_level;
					querystr += "&s_initiative_id="
							+ $("#" + select_group[0].id).val()
							+ "&s_regain_id=" + select_group[1].id
							+ "&s_regain_id2=" + select_group[2].id;
					sys_ajaxPost(
							"/select/default.do?method=selectOnChangeAccept",
							querystr, function(json) {
								bind(json);
							});
				});
	}
	if (select_group[1].length > 0) {
		$("#" + select_group[1].id).change(
				function(event) {
					var querystr = "selectNumber=2&userLevel=" + user_level;
					querystr += "&s_initiative_id="
							+ $("#" + select_group[1].id).val()
							+ "&s_regain_id2=" + select_group[2].id;
					sys_ajaxPost(
							"/select/default.do?method=selectOnChangeAccept",
							querystr, function(json) {
								bind(json);
							});
				});
	}
}

/*
 * 绑定矛盾所属地下拉列表框（矛调）		Li Shenjie / 2014.2.12
 * @param {Object} s_initiative_id	产生级联的下拉选择框 ID
 * @param {Object} s_regain_id		被级联的下拉选择框 ID
 */
function bindSelectOptionTerritorial(s_initiative_id, s_regain_id) {
	//绑定select框onchange事件
	if ($("#" + s_initiative_id).length > 0) {
		$("#" + s_initiative_id)
				.change(
						function(event) {
							var querystr = "s_initiative_id="
									+ $("#" + s_initiative_id).val()
									+ "&s_regain_id=" + s_regain_id;
							sys_ajaxPost(
									"/select/default.do?method=selectOnChangeTerritorial",
									querystr, function(json) {
										bind(json);
									});
						});
	}
}

//切换到影像地图
function switch_to_image_mode() {
	document.getElementById("img_img_mode").src = sys_ctx + "/skin/zhsq/images/map11.gif";
	document.getElementById("img_vector_mode").src = sys_ctx + "/skin/zhsq/images/map22.gif";
	if(map_net_mode=='innernet'){
		dMap.setMapType(D_SATE_MAP);
	}else{
		dMap.setMapType(TDT_SATE_MAP);
		D_OVERLAY_VECTOR_LAYER.display(false);
		D_OVERLAY_TDT_VECTOR_LAYER.display(false);
		D_OVERLAY_RASTER_LAYER.display(true);
		D_OVERLAY_TDT_RASTER_LAYER.display(true);
	}
}
//切换到矢量地图
function switch_to_vector_mode() {
	document.getElementById("img_img_mode").src = sys_ctx + "/skin/zhsq/images/map12.gif";
	document.getElementById("img_vector_mode").src = sys_ctx + "/skin/zhsq/images/map21.gif";
	if(map_net_mode=='innernet'){
		dMap.setMapType(D_VECTOR_MAP);
	}else{
		dMap.setMapType(TDT_VECTOR_MAP);
		D_OVERLAY_VECTOR_LAYER.display(true);
		D_OVERLAY_TDT_VECTOR_LAYER.display(true);
		D_OVERLAY_RASTER_LAYER.display(false);
		D_OVERLAY_TDT_RASTER_LAYER.display(false);
	}
}

function loadDMap(mapLoaded){
	if(map_net_mode!='innernet'){
		mapLoaded.addLayer(D_OVERLAY_TDT_VECTOR_LAYER);
		mapLoaded.addLayer(D_OVERLAY_VECTOR_LAYER);
		D_OVERLAY_VECTOR_LAYER.display(true);
		D_OVERLAY_TDT_VECTOR_LAYER.display(true);
		mapLoaded.addLayer(D_OVERLAY_RASTER_LAYER);
		mapLoaded.addLayer(D_OVERLAY_TDT_RASTER_LAYER);
		D_OVERLAY_RASTER_LAYER.display(false);
		D_OVERLAY_TDT_RASTER_LAYER.display(false);
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	