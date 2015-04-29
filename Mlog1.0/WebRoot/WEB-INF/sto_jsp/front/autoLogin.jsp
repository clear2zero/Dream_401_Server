<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快办365</title>
<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<link rel="shortcut icon" href="${ctx}/_static/images/front/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="${ctx}/js/login1.js?version=20130427&r=<%=Math.random()%>"></script>
<script type="text/javascript">
var reqrandom = "";      //请求随即数,防止js缓冲和重复提交
var _client = new HebcaClient();
$(document).ready(function(){
	validate('auto');
});	
</script>
</head>
<body  style="margin:0px;background-color: white">
<span>正在登陆，请稍后......</span>
<span>若无法跳转请点击此处<a href="${ctx}/front/main.action">手动登陆</a></span>
<form action="${ctx}/account/login.action" method="post" id="loginForm" style="margin: 0px;" >
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
<input type="hidden" name="certcn" value="${certcn}"/> 
</form>
</body>
</html>