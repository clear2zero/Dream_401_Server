<%@ page contentType="text/html; charset=UTF-8" session="false" %>
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
<link rel="shortcut icon" href="${ctx}/_static/images/front/favicon.ico" type="image/x-icon" />
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<%
String msg = String.valueOf(request.getAttribute("loginInvalid"));
if(msg !=null && !msg.equals("") && !msg.equals("null")){
	out.println("<script type='text/javascript'>alert('"+msg+"')</script>");
}
%>
<script type="text/javascript">
window.parent.location.href="${ctx}/front/main.action";
</script>
</head>
<body>
</body>
</html>
