<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<div align="center" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<input type="hidden" name="id" value="${o.id}" />
		<table class="tableForm">
			<tr>
				<td class="label-required">
					用户名
				</td>
				<td>
					<input id="username" name="username" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox" readonly
						data-options="required:true,missingMessage:'请填写用户名'" value="${o.username}" >
				</td>
			</tr>
			<tr>
				<td class="label-required">
					用户中文名
				</td>
				<td>
					<input id="name" name="name" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off' value="${o.name}" >
				</td>
			</tr>
			<%-- <tr>
				<td class="label">
					密码
				</td>
				<td>
					<input id="password" name="password" type="password" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off' value="${o.password}">
				</td>
			</tr> --%>
			<tr>
				<td class="label-required">
					身份证号码
				</td>
				<td>
					<input id="identitycard" name="identitycard" style="width: 250px;"
						autocomplete='off' class="easyui-validatebox" readonly
						data-options="required:true,missingMessage:'请填写身份证号'" value="${o.identitycard}" >
				</td>
			</tr>
			<tr>
				<td class="label-required">
					手机号
				</td>
				<td>
					<input id="mobilephone" name="mobilephone" style="width: 250px;" class="easyui-validatebox"
						data-options="required:true" autocomplete='off' value="${o.mobilephone}">
				</td>
			</tr>
			<tr>
				<td class="label">
					座机号码
				</td>
				<td>
					<input id="telephone" name="telephone" style="width: 250px;" autocomplete='off' value="${o.telephone}">
				</td>
			</tr>
			<tr>
				<td class="label">
					分机号码
				</td>
				<td>
					<input id="extension" name="extension" style="width: 250px;" autocomplete='off' value="${o.extension}">
				</td>
			</tr>
			<tr>
				<td class="label-required">
					所属角色
				</td>
				<td>
					<input id="roleid" name="roleid" style="width: 250px;" value="${o.role.id}">
				</td>
			</tr>
			<tr id="unit_tr">
				<td class="label-required">
					所属单位
				</td>
				<td>
					<input id="divid1" name="divid1" style="width: 250px;" autocomplete='off' value="${o.unit.divid}">
				</td>
			</tr>
			<tr id="dept_tr">
				<td class="label-required">
					所属部门
				</td>
				<td>
					<input id="deptid1" name="deptid1" style="width: 250px;" autocomplete='off' value="${o.dept.id}">
				</td>
			</tr>
			<!-- <tr>
				<td class="label-required">
					客户端角色
				</td>
				<td>
					<input id="clientrole" name="clientrole" style="width: 250px;" autocomplete='off'>
				</td>
			</tr> -->
			<tr>
				<td class="label">
					服务端证书
				</td>
				<td>
					<input id="hcertcn" name="hcertcn" style="width: 250px;" autocomplete='off' readonly value="${o.hcertcn}">
				</td>
			</tr>
			<tr>
				<td class="label">
					移动端证书
				</td>
				<td>
					<input id="scertcn" name="scertcn" style="width: 250px;" autocomplete='off' readonly value="${o.scertcn}">
				</td>
			</tr>
			<tr>
				<td class="label">
					状态
				</td>
				<td>
					<select name="isenable"  style="width: 250px;">
							<option value="1" <c:if test='${o.isenable==1}'>selected</c:if> >正常</option>
							<option value="0" <c:if test='${o.isenable==0}'>selected</c:if> >已冻结</option>
					</select>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
/* Array.prototype.contains = function(b){
	boolean iscontain = false;
	for (var i = 0; i < this.length; i++){
		if(this[i] == b){
			iscontain = true;
			break;
		}
    }
	return iscontain;
}; */
$(function(){
	$("#divid1").combobox({
	    url:'${pageContext.request.contextPath}/unit/listJsonNoPage.action',
	    valueField:'divid',
	    textField: 'divname',
	    panelHeight:'250',
	    onSelect : function(r){
	    	$("#deptid1").combotree('clear');
	    	$("#deptid1").combotree('reload','${pageContext.request.contextPath}/dept/listJson.action?divid='+r.divid);  
	    }
	});
	$("#deptid1").combotree({
	    url: '${pageContext.request.contextPath}/dept/listJson.action',   
	    required: true,
	    onBeforeSelect : function(node){
	    	/* var parent = $("#deptid").combotree('tree').tree('getParent',node.target);
	    	if(parent == null){
	    		alert("请选择具体部门");
	    		return false;
	    	} */
	    	return true;
	    },
	    onBeforeLoad : function(param){
	    	/* if(String($("#divid").combobox('getValue')).replace(/\s/,"") == ""){
	    		return false;
	    	}
	    	return true; */
	    }
	});

	var deptval = $("#deptid1").combotree('getValue');
	$("#deptid1").combotree('clear');
	$("#deptid1").combotree('reload','${pageContext.request.contextPath}/dept/listJson.action?divid='+$("#divid1").combobox('getValue'));  
	$("#deptid1").combotree('setValue',deptval);
	
	$("#roleid").combobox({
	    url:'${pageContext.request.contextPath}/role/listJsonNoPage.action',
	    valueField:'id',
	    textField: 'name',
	    panelHeight:'auto',
	    onSelect : function(r){
	    	$.post("${pageContext.request.contextPath}/role/isUnitgroup.action",{id:r.id},function(json){
	    		if(json.success){
		    		$("#unit_tr").css('display', 'block');
		    		$("#dept_tr").css('display', 'block');
		    		$("#divid1+.combo").show();
		    		$("#deptid1+.combo").show();
		    		$('#divid1').combobox({ required: true });
		    		$('#deptid1').combotree({ required: true });
		    	}else {
		    		$("#unit_tr").css('display', 'none');
		    		$("#dept_tr").css('display', 'none');
		    		$("#divid1+.combo").hide();
		    		$("#deptid1+.combo").hide();
		    		$('#divid1').combobox({ required: false });
		    		$('#deptid1').combotree({ required: false });
		    		$('#divid1').combobox('setValue','');
		    		$('#deptid1').combotree('setValue','');
		    	}
	    	});
	    	
	    },
	    onLoadSuccess:function(data){
	    	var id = $('#roleid').combobox('getValue');
	    	$.post("${pageContext.request.contextPath}/role/isUnitgroup.action",{id:id},function(json){
	    		if(json.success){
		    		$("#unit_tr").css('display', 'block');
		    		$("#dept_tr").css('display', 'block');
		    		$("#divid1+.combo").show();
		    		$("#deptid1+.combo").show();
		    		$('#divid1').combobox({ required: true });
		    		$('#deptid1').combotree({ required: true });
		    	}else {
		    		$("#unit_tr").css('display', 'none');
		    		$("#dept_tr").css('display', 'none');
		    		$("#divid1+.combo").hide();
		    		$("#deptid1+.combo").hide();
		    		$('#divid1').combobox({ required: false });
		    		$('#deptid1').combotree({ required: false });
		    		$('#divid1').combobox('setValue','');
		    		$('#deptid1').combotree('setValue','');
		    	}
	    	});
	    }
	});
});
</script>