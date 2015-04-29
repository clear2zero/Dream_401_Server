<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<script type="text/javascript">

</script>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<table class="tableForm">
			<!-- <tr>
				<td class="label-required">
					项目ID
				</td>
				<td>
					<input id="projectid" name="projectid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						data-options="required:true,missingMessage:'请填写用户名'" />
				</td>
			</tr> -->
			<tr>
				<td class="label-required">
					单位ID
				</td>
				<td>
					<input id="divid" name="divid" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					单位名称
				</td>
				<td>
					<input id="divname" name="divname" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					单位地址
				</td>
				<td>
					<input id="addr" name="addr" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						data-options="required:true,missingMessage:'请填写身份证号'" />
				</td>
			</tr>
			<tr>
				<td class="label-required">
					单位电话
				</td>
				<td>
					<input id="tel" name="tel" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label">
					法人
				</td>
				<td>
					<input id="corporation" name="corporation" style="width: 250px;" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label">
					联系人
				</td>
				<td>
					<input id="linkman" name="linkman" style="width: 250px;" autocomplete='off'>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					单位管理员证书CN
				</td>
				<td>
					<input id="adminuser" name="adminuser" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off'>
				</td>
			</tr>
			<!-- <tr>
				<td class="label">
					证书许可数量
				</td>
				<td>
					<input id="licence" name="licence" style="width: 250px;" autocomplete='off'>
				</td>
			</tr> -->
		</table>
	</form>
</div>
