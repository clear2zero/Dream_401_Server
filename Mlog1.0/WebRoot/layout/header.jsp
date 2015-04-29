<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %> 
<%@ taglib prefix="fnc" uri="/WEB-INF/tld/fnc.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=7" >
	<title>快办365</title>
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/_static/images/front/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/_static/css/themes/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/_static/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/_static/css/index.css">
	<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/_static/css/popWindows.css"> --%>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/_static/css/general.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/_static/css/themes/demo.css">
	
	<link rel="stylesheet" type="text/css" href="${ctx}/_static/js/ztree/zTreeStyle/zTreeStyleExt.css">
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/_static/js/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/_static/common/json2.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/_static/js/easyui1.3.3/jquery.easyui1.3.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/_static/js/easyui1.3.3/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/_static/js/easyui1.3.3/syUtil.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/base.js"></script>
	<script src="${pageContext.request.contextPath}/js/client/HebcaClient.js"></script>
	<script type="text/javascript" src="${ctx}/_static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
	<script type="text/javascript">
		var ctx = '${pageContext.request.contextPath}';
		var cache='${applicationScope.ResourcesWebSite}';
	</script>
</head>