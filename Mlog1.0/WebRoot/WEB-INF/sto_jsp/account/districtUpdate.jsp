<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post" >
		<input type="hidden" name="id" value="${o.id}" />
		<input id="superidTemp" name="superidTemp" type="hidden"  value="${o.parent}"/>
		<table class="tableForm">
			<tr>
				<td style="font-size: 12px;">
					名称
				</td>
				<td>
					<input id="districtname" name="districtname" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						  value="${o.districtname}"/>
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					区划编码
				</td>
				<td>
					<input id="districtid" name="districtid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						 value="${o.districtid}"/>
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					邮政编码
				</td>
				<td>
					<input id="postcode" name="postcode" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						 value="${o.postcode}"/>
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
						 value="${o.orderid}"/>
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					级次
				</td>
				<td>
					<input id="levelid" name="levelid" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						 value="${o.levelid}"/>
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					上级菜单
				</td>
				<td>
					<input id="parent.id" name="parent.id" style="width: 250px;"  value="${o.parent.id}"/>
				</td>
			</tr>
		</table>
	</form>
</div>
