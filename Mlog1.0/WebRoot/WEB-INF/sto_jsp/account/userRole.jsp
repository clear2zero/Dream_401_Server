<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>

<div  style="padding: 5px; overflow: hidden;">
	<form method="post" >
		<input type="hidden" name="username" value="${o[0]}" />
		<table class="tableForm">
			<tr>
				<td style="font-size: 12px;">
					账号:
				</td>
				<td>
					${o[0]}
				</td>
			</tr>
			<tr>
				<td style="font-size: 12px;">
					角色:
				</td>
				<td>
					<select name="role_id" style="width: 200px;">
						 <c:forEach items="${roleList}" var="r"> 
							<option value="${r.id}"  <c:if test="${r.id==o[1]}">selected="selected"</c:if>  >${r.name}</option>
						</c:forEach>
					</select>

				</td>
			</tr>
		</table>
	</form>
</div>
