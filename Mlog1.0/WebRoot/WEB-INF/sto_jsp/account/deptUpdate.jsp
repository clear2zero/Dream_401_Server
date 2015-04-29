<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post" >
		<input type="hidden" name="id" value="${o.id}" />
		<input id="superidTemp" name="superidTemp" type="hidden"  value="${o.parent.id}"/>
		<table class="tableForm">
			<tr>
				<td class="label">
					部门编码
				</td>
				<td>
					<input id="deptid" name="deptid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox" readonly value="${o.deptid}">
				</td>
			</tr>
			<tr>
				<td class="label">
					部门名称
				</td>
				<td>
					<input id="deptname" name="deptname" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox" value="${o.deptname}">
				</td>
			</tr>			
			<tr>
				<td class="label">
					上级部门
				</td>
				<td>
					<input id="parent.id" name="parent.id" style="width: 250px;" value="${o.parent.id}">
				</td>
			</tr>
			<tr>
				<td class="label">
					顺序
				</td>
				<td>
					<input id="orderid" name="orderid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox" value="${o.orderid}">
				</td>
			</tr>
		</table>
	</form>
</div>
