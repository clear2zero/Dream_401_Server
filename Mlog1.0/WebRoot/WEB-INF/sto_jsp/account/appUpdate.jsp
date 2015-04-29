<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post" >
		<input type="hidden" name="id" value="${o.id}" />
		<table class="tableForm">
			<tr>
					<td class="label-required">版本号：</td>
					<td><input id="apkversion" name="apkversion" disabled value="${o.apkversion}" style="width: 350px;"></td>
				</tr>
				<tr>
					<td class="label-required">是否强制升级：</td>
					<td><select id="isforceupdate" name="isforceupdate" class="easyui-combobox" data-options="required:true,editable:false" panelHeight="auto" style="width: 355px;">
						<option value="0" <c:if test="${o.isforceupdate==0}">selected</c:if>>否</option>
						<option value="1" <c:if test="${o.isforceupdate==1}">selected</c:if>>是</option>
					</select></td>
				</tr>
				<tr>
					<td class="label-required">发布状态：</td>
					<td><select id="status" name="status" class="easyui-combobox" data-options="required:true,editable:false" panelHeight="auto" style="width: 355px;" >
						<option value="0" <c:if test="${o.status==0}">selected</c:if> >正常</option>
						<option value="1" <c:if test="${o.status==1}">selected</c:if>>停用</option>
						<option value="2" <c:if test="${o.status==2}">selected</c:if>>回滚</option>
					</select></td>
				</tr>
				<tr>
					<td class="label">版本描述：</td>
					<td><textarea rows="12" cols="43" id="description" name="description">${o.description}</textarea></td>
				</tr>
		</table>
	</form>
</div>
