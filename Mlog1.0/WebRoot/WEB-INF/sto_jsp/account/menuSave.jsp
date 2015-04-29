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
						 />
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					地址
				</td>
				<td>
					<input id="curl" name="curl" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						 />
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					英文名称
				</td>
				<td>
					<input id="enname" name="enname" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						 />
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
					顺序
				</td>
				<td>
					<input id="orderid" name="orderid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						 />
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					上级菜单
				</td>
				<td>
					<input id="superid" name="superid" style="width: 250px;" />
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					菜单图标
				</td>
				<td>
					<input id="icon" name="icon" style="width: 250px;" />
				</td>
			</tr>
		</table>
	</form>
</div>
