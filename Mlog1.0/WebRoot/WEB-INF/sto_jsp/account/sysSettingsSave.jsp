<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<script type="text/javascript">
$("#iscommon").combobox({
	valueFiled:'value',
	textFiled:'text',
	data:[{value:'1',text:'是'},{value:'0',text:'否'}],
	panelHeight:'auto',
	onSelect : function(r){
		if(r.value=='1'){
			$("#isvisible_tr").css('display', 'none');
			$("#isvisible+.combo").hide();
		}else {
			$("#isvisible_tr").css('display', 'block');
			$("#isvisible+.combo").show();
			$("#isvisible").combobox('setValue','1');
		}
	}
});
$("#isvisible").combobox({
	valueFiled:'value',
	textFiled:'text',
	data:[{value:'1',text:'是'},{value:'0',text:'否'}],
	panelHeight:'auto'
});
$("#iscommon").combobox('setValue','1');
$("#isvisible_tr").css('display', 'none');
$("#isvisible+.combo").hide();
</script>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<table class="tableForm">
			<tr>
				<td class="label-required">
					设置项
				</td>
				<td>
					<input id="name" name="name" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox"
						data-options="required:true,missingMessage:'请填写名称'" />
				</td>
			</tr>
			<tr>
				<td class="label-required">
					唯一标识
				</td>
				<td>
					<input id="skey" name="skey" style="width: 250px;" autocomplete='off'></input>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					设置值
				</td>
				<td>
					<input id="value" name="value" style="width: 250px;" autocomplete='off'></input>
				</td>
			</tr>
			<tr>
				<td class="label-required">
					是否为系统配置
				</td>
				<td>
					<input id="iscommon" name="iscommon" style="width: 255px;">
				</td>
			</tr>
			<tr id="isvisible_tr">
				<td class="label-required">
					单位配置是否可见
				</td>
				<td>
					<input id="isvisible" name="isvisible" style="width: 255px;">   
				</td>
			</tr>
		</table>
	</form>
</div>
