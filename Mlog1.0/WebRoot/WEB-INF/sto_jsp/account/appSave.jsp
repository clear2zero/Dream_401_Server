<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<script type="text/javascript">
function onUploadApkChange(sender){
	if(sender.value){
		 if( !sender.value.match( /.apk/i ) ){
		        alert('需上传apk格式！');
		        sender.select();
			  	document.execCommand("delete");
		        return false;
		  }
	}
   
    /*alert(document.selection.createRange().text); */
    return true;
}
function checkData(form) {
	if ($("#apkversion").val().replace('\s','') == '') {
		alert('请填写版本号');
		return false;
	}
	if ($("#apk").val().replace('\s','') == '') {
		alert('请选择上传文件');
		return false;
	}
	if ($("#isforceupdate").combobox('getValue').replace('\s','') == '') {
		alert('请选择是否强制升级');
		return false;
	}
	if ($("#status").combobox('getValue').replace('\s','') == '') {
		alert('请选择发布状态');
		return false;
	}
	return true;
}

function save(){
	$("#form").form('submit',{
		onSubmit: function(param) {
			return checkData();
		},
    	success:function(json){
    		var jsonObj = eval('(' + json + ')');
			if (jsonObj.success) {
				form.reset();
			};
			sy.messagerShow({
				msg : jsonObj.msg,
				title : '提示'
			});
			
	      }
	}); 
}

</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<form id="form" method="post" enctype="multipart/form-data" onsubmit="return checkData(this);" action="${pageContext.request.contextPath}/app/saveDo.action">
<div data-options="region:'center',border:false" style="overflow: hidden;">
	<div align="left" style="padding: 5px; overflow: hidden;">
			<table border="1" cellspacing="0" cellpadding="0" >
				<tr>
					<td align="right">版本号：</td>
					<td><input id="apkversion" name="apkversion" class="easyui-validatebox"></td>
				</tr>
				<tr>
					<td align="right" >apk文件：</td>
					<td >
				     	<input size="50" id="apk"  type="file" name="apk" class="easyui-validatebox" accept="apk/*" onchange="onUploadApkChange(this);" />
				    </td>
				</tr>
				<tr>
					<td align="right">是否强制升级：</td>
					<td><select id="isforceupdate" name="isforceupdate" class="easyui-combobox" data-options="required:true,editable:false" style="width:100px;">
						<option value="0" selected>否</option>
						<option value="1">是</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">发布状态：</td>
					<td><select id="status" name="status" class="easyui-combobox" data-options="required:true,editable:false" style="width:100px;">
						<option value="0" selected>正常</option>
						<option value="1">停用</option>
						<option value="2">回滚</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">版本描述：</td>
					<td><textarea rows="12" cols="50" id="description" name="description" ></textarea></td>
				</tr>
				<tr>
				    <td height="20" align="right" colspan="4"><input class="btn4" type="button" value="提交" onclick="save()"/></td>
				</tr>
			</table>
	</div>
</div>
</form>
</body>