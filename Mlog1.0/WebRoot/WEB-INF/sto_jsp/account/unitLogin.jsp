<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<title></title>			
<script type="text/javascript">
function checkForm(){
	return true;
}
function applyCert(){
	$("#form").submit();
}
</script>
<script type="text/javascript" src="${ctx}/js/blockUI.js"></script>
<script type="text/javascript" src="${ctx}/js/login.js?version=20130427&r=<%=Math.random()%>"></script>
</head>
<body  style="margin-top:30px;">
<div align="center" style="height:485px;overflow: hidden;background:url(${ctx}/_static/images/reg.jpg) repeat-x;">
	<em class="error">
		<c:if test="${not empty exception }">
	  	申请失败,<c:out value="${exception}"/>
		</c:if>
		&nbsp;
	</em>
	<form id="form" method="post" action="${ctx}/unit/frontUnitLoginDo.action" onSubmit="return checkForm()">
		<input id="divname" name="divname" type="hidden" value="${o.divname}">
		<table class="tableForm" style="line-height:40px;">
			<tr>
				<td class="label-required">
					登录名：
				</td>
				<td>
					<input id="username" name="username" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					口令：
				</td>
				<td>
					<input id="password" name="password" type="password" style="width: 250px;"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td colspan=2 align="center">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="applyCert()">申请证书</a>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>