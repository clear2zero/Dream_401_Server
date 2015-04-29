<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<script type="text/javascript">

</script>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<table class="tableForm">
			<tr>
				<td class="label-required">
					项目ID
				</td>
				<td>
					<input id="projectid" name="projectid" style="width: 250px;" 
						readonly value="${o.projectid}" >
				</td>
			</tr>
			<tr>
				<td class="label-required">
					单位ID
				</td>
				<td>
					<input id="divid" name="divid" style="width: 250px;" readonly  value="${o.divid}" >
				</td>
			</tr>
			<tr>
				<td class="label-required">
					单位名称
				</td>
				<td>
					<input id="divname" name="divname" style="width: 250px;" readonly value="${o.divname}" >
				</td>
			</tr>
			<tr>
				<td class="label">
					单位地址
				</td>
				<td>
					<input id="addr" name="addr" style="width: 250px;" readonly  value="${o.addr}"/>
				</td>
			</tr>
			<tr>
				<td class="label">
					单位电话
				</td>
				<td>
					<input id="tel" name="tel" style="width: 250px;" readonly  value="${o.tel}">
				</td>
			</tr>
			<tr>
				<td class="label">
					法人
				</td>
				<td>
					<input id="corporation" name="corporation" style="width: 250px;" readonly value="${o.corporation}">
				</td>
			</tr>
			<tr>
				<td class="label">
					联系人
				</td>
				<td>
					<input id="linkman" name="linkman" style="width: 250px;" readonly  value="${o.linkman}">
				</td>
			</tr>
			<tr>
				<td class="label">
					单位管理员证书CN
				</td>
				<td>
					<input id="adminuser" name="adminuser" style="width: 250px;" readonly  value="${o.adminuser}">
				</td>
			</tr>
			<%-- <tr>
				<td class="label">
					证书许可数量
				</td>
				<td>
					<input id="licence" name="licence" style="width: 250px;" readonly value="${o.licence}">
				</td>
			</tr> --%>
		</table>
	</form>
</div>
