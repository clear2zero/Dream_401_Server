<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>

<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<table class="tableForm">
			<tr>
				<td style="font-size: 12px;">
					名称
				</td>
				<td>
					<input id="name" name="name" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						data-options="required:true,missingMessage:'请填写角色名称'" />
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					英文名称
				</td>
				<td>
					<input id="enname" name="enname" style="width: 250px;" autocomplete='off'></input>
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					序号
				</td>
				<td>
					<input id="orderid" name="orderid" 
						autocomplete='off' class="easyui-validatebox"
						data-options="required:true,missingMessage:'请填写序号'" />
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					状态
				</td>
				<td>
					<select name="status"  style="width: 250px;">
							<option value="1">正常</option>
							<option value="0">停用</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					备注
				</td>
				<td>
					<textarea id="remark" name="remark"
						style="width: 250px; height: 70px;" autocomplete='off'
						class="easyui-validatebox"
						data-options="required:true,missingMessage:'请填写备注'"></textarea>

				</td>
			</tr>
		</table>
	</form>
</div>
