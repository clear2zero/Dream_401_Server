<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<table class="tableForm">
			<tr>
				<td class="label">
					部门编码
				</td>
				<td>
					<input id="deptid" name="deptid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox">
				</td>
			</tr>
			<tr>
				<td class="label">
					部门名称
				</td>
				<td>
					<input id="deptname" name="deptname" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox">
				</td>
			</tr>			
			<tr>
				<td class="label">
					上级部门
				</td>
				<td>
					<input id="parent.id" name="parent.id" style="width: 250px;">
				</td>
			</tr>
			<tr>
				<td class="label">
					顺序
				</td>
				<td>
					<input id="orderid" name="orderid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox">
				</td>
			</tr>
		</table>
	</form>
</div>
