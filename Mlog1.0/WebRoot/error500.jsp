<%@ page language="java" pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title>Error</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <H1>错误：</H1><%=exception%>
   <%
	         exception.printStackTrace(response.getWriter());
 %>
  	<div style="width:460px;margin:80px auto 0 auto;border:1px solid #aaa;font-size:12px;">
  		<div style="padding:6px 10px 8px 7px;color:#ffffff;font-weight: bold;background-color: #35AEE3;">系统提示</div>
  		<div style="padding:30px 34px 0 110px;">
  			<p style="font-size:16px;font-weight: bold;font-family: 'Microsoft YaHei';'">系统出现错误，请联系管理员！</p>
  		</div>
  		<div style="padding:10px 8px 25px 8px;text-align: center;">
  			<input type="button" style="cursor: pointer;width:80px;height: 30px;padding:3px 5px 0 0;margin:0;" value="返&nbsp;&nbsp;回" onclick="window.history.go(-1);"/>
  		</div>
  	</div>
  </body>
</html:html>
