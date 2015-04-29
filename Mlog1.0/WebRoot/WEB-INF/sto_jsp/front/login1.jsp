<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/client.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户登录</title>
<link href="${ctx}/_static/css/front/main.css" rel="stylesheet" type="text/css" />
<style type="text/css">
body {	background-color: #eee9de;}
</style>
<script type="text/javascript" src="${ctx}/js/blockUI.js"></script>
<script type="text/javascript" src="${ctx}/js/login.js?version=20130427&r=<%=Math.random()%>"></script>
<script type="text/javascript">
var reqrandom = "";      //请求随即数,防止js缓冲和重复提交
var u;
var install=false;
</script>
</head>
<body onload="MM_preloadImages('${ctx}/_static/images/front/wel5butb_13.png')">
<div id="sitop">
	<div id="sitopcenter">
		<div id="toplogo"><img src="${ctx}/_static/images/front/logo_05.png" width="200" height="80" /></div>
		<div id="toptexta">用户登录</div>
		<div id="toptextb"><a href="${ctx}/front/main.action">返回首页</a> | <a href="javascript:void(0)">帮助中心</a></div>
	</div>
</div><!-----sitop------>
<div id="signwrap">
	<div id="signcenter">
		<div id="signc1"><img src="${ctx}/_static/images/front/wel5_06.png" width="672" height="561" /> </div>
		<div id="signc2">
			<form action="${ctx}/account/login.action" method="post" id="loginForm" style="margin: 0px;" >
				<div id="signt1">请插入USBkey登录</div>
				<div id="signt2"><a href="javascript:validate('hebca');"><img src="${ctx}/_static/images/front/wel5but_09.png" width="203" height="39" border="0" /></a></div>
				<div id="signt3"><a href="javascript:changepin();">点击修改USBkey密码 </a></div>
				<div id="signt4">新用户请注册</div>
				<div id="signt5"><a href="javascript:checkOutKey(1);" class="button orange">注册 </a> </div>
				<input type="hidden" name="useCertLogin" />  
				<input type="hidden" name="authMode"  />   
				<input type="hidden" name="caMode"  value="hebca"/>  
				<input type="hidden" name="signCert" /> 
				<input type="hidden" name="cryptCert" /> 
				<input type="hidden" name="signature" />   
				<input type="hidden" name="seal" /> 
				<input type="hidden" name="keyType" value="usbkey证书"/> 
				<input type="hidden" name="username" />
				<input type="hidden" name="loginRandom" value="<c:out value="${requestScope.loginRandom}" />" /> 
			</form>
		</div><!-----signc2------>
	</div><!-----signcenter------>
	<div class="clearfloat"></div>
	<div class="signbottom">版权所有：2013-2014河北腾翔软件科技有限公司  冀ICP备13016849号-2  电话：0311-69025838</div>
</div><!-----signwrap------>
</body>
</html>
