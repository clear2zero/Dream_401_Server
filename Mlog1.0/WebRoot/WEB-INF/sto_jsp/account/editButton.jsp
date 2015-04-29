<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>

<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<input type="hidden" name="moduleid" value="${o.moduleid}" />
		<table class="tableForm">
			<tr>
				<td style="font-size: 12px;">
					分配操作
				</td>
				<td>
					<select name="buttonids" id="buttonids"  style="width: 250px;">
					</select>
				</td>
			</tr>
		</table>
	</form>
</div>
