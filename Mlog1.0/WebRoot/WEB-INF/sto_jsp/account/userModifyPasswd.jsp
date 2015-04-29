<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>

<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<input type="hidden" name="id" value="${o.id}" />
		<table class="tableForm">
			<tr>
				<td class="label-required">
					新密码
				</td>
				<td>
					<input id="username" name="username" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox" readonly
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					重复密码
				</td>
				<td>
					<input id="name" name="name" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
		</table>
	</form>
</div>
