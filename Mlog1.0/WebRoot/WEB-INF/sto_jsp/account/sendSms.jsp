<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
if($("input[name=sf]:checked").val()){
	if($("input[name=sf]:checked").val() == "1"){
		$("#template_tr").css("display","block");
		$("#self_tr").css("display","none");
		$("#isusetemplate").val("1");
	}else {
		$("#template_tr").css("display","none");
		$("#isusetemplate").val("0");
		$("#self_tr").css("display","block");
	}
}

$("input[name=sf]").click(function(){
	if($("input[name=sf]:checked").val() == "1"){
		$("#template_tr").css("display","block");
		$("#isusetemplate").val("1");
		$("#self_tr").css("display","none");
	}else {
		$("#template_tr").css("display","none");
		$("#isusetemplate").val("0");
		$("#self_tr").css("display","block");
	}
});
</script>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<input type="hidden" name="ids" id="ids" />
		<input type="hidden" name="divid" id="divid" />
		<input type="hidden" name="isusetemplate" id="isusetemplate" value="1" />
		<table class="tableForm">
			<shiro:hasRole name="sysadmin">
			<tr>
				<td class="label">使用模板：</td>
				<td style="width:200px;"><input type="radio" name="sf" value="1" checked>是<input type="radio" name="sf" value="0">否</td>
			</tr>
			</shiro:hasRole>
			<tr id="template_tr">
				<td class="label">短信模板：</td>
				<td style="width:200px;"><span><p style="text-indent:2em;line-height:20px;">${content}</p></span></td>
			</tr>
			<shiro:hasRole name="sysadmin">
			<tr id="self_tr">
				<td class="label">自定义短信：</td>
				<td style="width:200px;"><textarea rows="8" cols="40" id="content" name="content"></textarea></td>
			</tr>
			</shiro:hasRole>
		</table>
	</form>
</div>