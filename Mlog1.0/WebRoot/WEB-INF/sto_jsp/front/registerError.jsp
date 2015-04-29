<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/includes/taglibs.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<link rel="shortcut icon" href="${ctx}/_static/images/front/favicon.ico" type="image/x-icon" />
<link href="${ctx}/_static/css/front/main.css" rel="stylesheet" type="text/css" />
<style type="text/css">
</style>
</head>
<body>
<div id="sitop">
<div id="sitopcenter">
<div id="toplogo"><img src="${ctx}/_static/images/front/indexlogo_05.png" width="220" height="80" /></div>
<div id="toptexta">注册失败</div>
<div id="toptextb"><a href="${ctx}/front/main.action">返回首页</a> | <a href="javascript:void(0)">帮助中心</a></div>
</div>
</div><!-----sitop------>
<div id="regwrap">
<div id="signcenter">
<div id="regt"></div>
<div id="regc">

<div id="regok">
<div id="regoka"><img src="${ctx}/_static/images/front/errico_03.png" width="68" height="67" /></div>
<div id="regokb">
  <h2><font color='red'>注册失败</font></h2>
  <p><font color='red'>原因：${exception}</font></p>
  <p></p>
  <p>请联系系统管理员，或点此重新注册<a href="${ctx}/unit/frontRegister.action">【重新注册】</a></a></p>
</div>
<div class="clearfloat"></div>
</div><!-----regok------>
</div>
<div id="regb"></div>
</div><!-----signcenter------>
<div class="clearfloat"></div>
<div class="signbottom">版权所有：2013-2014河北腾翔软件科技有限公司  冀ICP备13016849号-2  电话：0311-69025838</div>
</div><!-----signwrap------>


</body>
</html>
