<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	 <style type="text/css">
		#date_left{
			float:left;
		}
		#date_right{
			float:left;
		}
		.week{
			font-size:14px;
			color:white;
			font-family: 'Microsoft YaHei','微软雅黑','SimSun','宋体';
			padding-left:5px;
			padding-top:5px;
		}
		.day{
			font-size:36px;
			color:white;
			font-family: 'Microsoft YaHei','微软雅黑','SimSun','宋体';
			padding-left:5px;
		}
		.month{
			font-size:14px;
			color:white;
			font-family: 'Microsoft YaHei','微软雅黑','SimSun','宋体';
			padding-left:5px;
		}
		.apm{
			font-size:14px;
			color:#0080ff;
			font-family: 'Microsoft YaHei','微软雅黑','SimSun','宋体';
			font-weight:solid;
			padding-left:90px;
		}
		.time{
			font-size:32px;
			color:#0080ff;
			font-family: 'Microsoft YaHei','微软雅黑','SimSun','宋体';
			font-weight:solid;
			padding-left:31px;
			padding-bottom:25px;
		}
		table td{
			height:30px;
			border-bottom:1px solid #D7D7D7;
			border-left:1px solid #D7D7D7;
			text-align:center;
		}
		table td:first-child{
			background:none repeat scroll 0 0 #F1F1F3;
		}
		table th{
			height:30px;
			border-bottom:1px solid #D7D7D7;
			border-left:1px solid #D7D7D7;
			background:none repeat scroll 0 0 #F1F1F3;
			text-align:center;
		}
		table td a{
			width:80px;
			text-align:center;
			font-size:20px;
			font-family: 'Microsoft YaHei','微软雅黑','SimSun','宋体';
		}
    </style>
    <%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
    <script src="${pageContext.request.contextPath}/js/ServerClock.js"></script>
<script type="text/javascript">
function attend(type){
	$.post("${ctx}/attend/checkAttendStatus.action",{},function(json){
		if(type == 1){
			if(json.status == 0){
				sy.messagerShow({
					msg : '已打上班卡，请先签退',
					title : '提示'
				});
				$("#on").css("display","none");
				$("#off").css("display","block");
				return false;
			}	
		}
		if(type == 2){
			if(json.status == 1 || json.status == -1){
				sy.messagerShow({
					msg : '未打上班卡,请先签到',
					title : '提示'
				});
				$("#on").css("display","block");
				$("#off").css("display","none");
				return false;
			}	
		}
		
		$.ajax({
		      type : "post",
		      url : "${pageContext.request.contextPath}/attend/attendDo.action",
		      data : {type:type},
		      success : function(json){
				if (json.success) {
					if(type == 1){
						$("#on").css("display","none");
						$("#off").css("display","block");
						sy.messagerShow({
							msg : '签到成功！',
							title : '提示'
						});
					}else{
						$("#on").css("display","block");
						$("#off").css("display","none");
						sy.messagerShow({
							msg : '签退成功！',
							title : '提示'
						});
					}
					checkAttendStatus();
				}
		      }
		});
	});
	
}

function checkAttendStatus(){
	$.post("${ctx}/attend/checkAttendStatus.action",{},function(json){
		if(json.status == 0){
			$("#on").css("display","none");
			$("#off").css("display","block");
			document.getElementById("preontime").innerHTML=json.preontime;
			document.getElementById("preofftime").innerHTML=json.preofftime;
			document.getElementById("ontime").innerHTML=json.ontime;
			document.getElementById("offtime").innerHTML=json.offtime;
		}else {
			$("#on").css("display","block");
			$("#off").css("display","none");
			document.getElementById("preontime").innerHTML=json.preontime;
			document.getElementById("preofftime").innerHTML=json.preofftime;
			document.getElementById("ontime").innerHTML=json.ontime;
			document.getElementById("offtime").innerHTML=json.offtime;
		}
	});
}
$(function(){
	$("#jou_bg").css("width",$(document.body).width()-50);
	$("#jou_bg").css("height",$(document.body).height()-40);
	
	$("#main").css("height",$(document.body).height()-110);
	$.post("${ctx}/common/getServerDateStr.action",{},
		function(jsonarr){
			var srvClock = new ServerClock(jsonarr[0],jsonarr[1],jsonarr[2],jsonarr[3],jsonarr[4],jsonarr[5]);
			/* 时间格式化字符串 */
			var fmtStr = "服务器的时间是：<br/>yyyy年MM月dd日 HH:mm:ss E<br/>是该年的第D天<br/>是该年的第w周<br/>";
			var fmtStr1 = "MM-dd-EEE-HH:mm-a";
			/* 
			由于网络延时无法估计的原因，会有一定的误差。用户可以通过 set_delay() 方法来减少误差。
			 默认为1000，表示 1 秒。 
			*/
			srvClock.set_delay(3000); /* 时钟向后延时 3 秒 */
			
			/* 0.5秒刷新一次时间 */
			window.setInterval(function(){
					var date = srvClock.get_ServerTime(fmtStr1).split("-");
					var dateHtml="<div id='date_left'><span class='week'>"+date[2]+"</span><br/><span class='day'>"+date[1]+"</span><span class='month'>"+date[0]+"</span></div>"
						+"<div id='date_right'><span class='apm'></span><br/><span class='time'>"+date[3]+"</span></div>";
			     	document.getElementById("date").innerHTML = dateHtml;
			 },500);	
		}
	);
	checkAttendStatus();
});
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<form id="form" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/journal/saveDo.action">
<input id="userid" name="userid" type="hidden" value="${user.id}" >
<input id="name" name="name" type="hidden" value="${user.name}" >
<input id="createtime" name="createtime" type="hidden" value="${createtime}">
<div data-options="region:'center',border:false" style="overflow: hidden;">
	<div align="top" style="position:fixed;z-index:2;width:100%;height:64px;left:30px;top:10px;overflow: hidden;">
			<div id="date" style="float:left;width:236px;height:64px;background:url('${ctx}/_static/images/jou_date.png');background-repeat:no-repeat;">
			</div>
	</div>
	<div style="position:fixed;z-index:1;width:100%;height:100%;left:10px;top:20px;">
		<img id="jou_bg" src="${ctx}/_static/images/jou_bg.png">
	</div>
	<%-- <div style="position:fixed;z-index:2;width:100%;height:100%;left:20px;top:80px;">
		<img id="jou_main" src="${ctx}/_static/images/jou_main.png">
	</div> --%>
	<div id="main" style="position:fixed;z-index:3;width:100%;left:30px;top:100px;overflow:hidden">
		<table id="attend" cellspacing="0" cellpadding="0" style="border:1px solid #D7D7D7;">
			<tr>
				<th>上/下班</th>
				<th>上次打卡时间</th>
				<th>当前打卡时间</th>
				<th>操作</th>
			</tr>
			<tr>
				<td style="width:100px;"><div>上班</div></td>
				<td style="width:200px;color:green;"><div id="preontime" style="width:195px;padding-left:5px;"></div></td>
				<td style="width:200px;color:green;"><div id="ontime" style="width:195px;padding-left:5px;"></div></td>
				<td style="width:100px;"><div id="on" style="width:100px;"><a  href="javascript:attend(1);" class="easyui-linkbutton">签到</a></div></td>
			</tr>
			<tr>
				<td style="width:100px;"><div>下班</div></td>
				<td style="width:200px;color:green;"><div id="preofftime" style="width:195px;padding-left:5px;"></div></td>
				<td style="width:200px;color:green;"><div id="offtime" style="width:195px;padding-left:5px;"></div></td>
				<td style="width:100px;"><div id="off" style="width:100px;"><a href="javascript:attend(2);" class="easyui-linkbutton">签退</a></div></td>
			</tr>
		</table>
	</div>
</div>
</form>
</body>