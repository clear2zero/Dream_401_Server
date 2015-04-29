<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@ page import="org.apache.shiro.*,sto.common.exception.*,org.apache.shiro.authc.AuthenticationException,java.lang.*"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快办365</title>
<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
<link href="${ctx}/_static/css/front/main.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/_static/css/focus/lrtk.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#item{width:200px;height:4350px;padding-top:10px;background-color:#eeeeee;)}
#item ul li{width:200px;font-size:18px;font-weight:bold;float:left; text-align:center;}
#item ul li a{font-family: "Microsoft YaHei","微软雅黑","SimSun","宋体";display:block; text-decoration:none; width:200px;height:30px;color:#3577b3;}
#item ul li a:hover{color:#FFFFFF;background-color:#dd0000;}
</style>
<link rel="shortcut icon" href="${ctx}/_static/images/front/favicon.ico" type="image/x-icon" />
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<script type="text/javascript" src="${ctx}/_static/js/focus/lrtk.js"></script>
<script type="text/javascript" src="${ctx}/js/login1.js?version=20130427&r=<%=Math.random()%>"></script>
<script type="text/javascript">
function help(flag){
	if(flag == 1){
		$("#pc").css('display','block');
		$("#mobile").css('display','none');
	}
	if(flag == 2){
		$("#pc").css('display','none');
		$("#mobile").css('display','block');
	}
	
}
</script>
</head>
<body>
<div id="index_nav">
<div id="item">
	<ul>
		<li><a href="javascript:void(0);" onclick="javascript:help(1);">PC端用户指南</a></li>
		<li><a href="javascript:void(0);" onclick="javascript:help(2);">移动端用户指南</a></li>
	</ul>
</div>	
</div>

<div style="float:right;width:80%;overflow:hidden;">
	<div id="pc" style="float:left;display:block">
		<img src="${ctx}/_static/images/help1.jpg" alt="快办365" />
	</div>
	<div id="mobile" style="float:left;display:none">
		<img src="${ctx}/_static/images/help2.jpg" alt="快办365" />
	</div>
</div>
</body>
</html>
