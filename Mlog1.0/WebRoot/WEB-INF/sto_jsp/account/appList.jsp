<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'north',border:false,title:'查询条件'"
		style="height: 60px; overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar"
				style="width：100%;height: 100%;">
				<tr>
					<td style="font-size: 12px;">版本号:</td>
					<td><input id="apkversion" name="apkversion" /></td>
					<td><a href="javascript:void(0)" class="easyui-linkbutton" 
						data-options="iconCls:'icon-search'" onclick="javascript:_search();">查询</a></td>
				</tr>
			</table>
		</form>
	</div>
<div data-options="region:'center',border:false" style="overflow: hidden;">
	<table id="datagrid" style="overflow: auto;height:450px;"></table>
</div>
	   
<script type="text/javascript" >
var datagrid;
$(function() {
	datagrid = $('#datagrid').datagrid({
		url : '${pageContext.request.contextPath}/app/getAppList.action',
		height:$(document.body).height()-100,
		pagination : true,
		pageSize : 10,
		pageList : [5,10,20,30,50],
		pageNumber:1,
		nowrap : true,
		border : false,
		idField : 'id',
		sortName : 'id',
		sortOrder : 'desc',
		checkOnSelect : true,
		singleSelect : true,
		columns : [ [ {
			title : '编号',
			field : 'id',
			hidden : true
		},{
			field : 'userid',
			hidden : true
		}, {
			title : '序号',
			field : 'orderid',
			width : 30,
			align:'center',
			formatter:function(value, row, index){
				return index+1;
			}
		}, {
			title : '发布版本',
			field : 'apkversion',
			width : 120,
			align:'left'
		},{
			title : '版本状态',
			field : 'status',
			width : 70,
			align:'center',
			formatter:function(v,row,i){
				if(v=='0'){
					return "正常";
				}
				if(v=='1'){
					return "停用";
				}
				if(v=='2'){
					return "回滚";
				}
				return v;
			}
		},{
			title : '是否强制升级',
			field : 'isforceupdate',
			width : 100,
			align:'center',
			formatter:function(v,row,i){
				if(v=='0'){
					return "否";
				}
				if(v=='1'){
					return "是";
				}
				return v;
			}
		},{
			title : '发布地址',
			field : 'downloadurl',
			width : 200,
			align:'left'
		},{
			title : '发布描述',
			field : 'description',
			width : 350,
			align:'left'
		},{
			title : '发布日期',
			field : 'publishtime',
			width : 120,
			align:'center'
		},{
			title : '发布人',
			field : 'name',
			width : 70,
			align:'left'
		}] ],
		toolbar : [{
			text : '编辑',
			iconCls : 'icon-edit',
			handler : function(){
				update();
			}
		}],
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll');
			$(this).datagrid('selectRow', rowIndex);
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});
});

function update() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length == 1 ) {
		var p = sy.dialog({
			title : '修改',
			href : '${pageContext.request.contextPath}/app/update.action?id='+rows[0].id,
			width : 540,
			height : 380,
			buttons : [ {
				text : '保存',
				handler : function() {
					if(!p.find('form').validate()){
						return false;
					}
					$.ajax({
				      type : "post",
				      url : "${pageContext.request.contextPath}/app/updateDo.action",
				      data : p.find('form').serialize(),
				      success : function(json){
						if (json.success) {
							datagrid.datagrid('reload');
							p.dialog('close');
						}
						sy.messagerShow({
							msg : json.msg,
							title : '提示'
						});
				      }
					 });
				}
			} ],
			onLoad : function() {
				var f = p.find('form');
				f.form('load', {
					status:node.status
				});
				var f_parent = f.find('input[name=superid]');
				var mycombotree = f_parent.combotree({
				    url:'${pageContext.request.contextPath}/module/listJsonForMenuTree.action',
				    lines : true,
				    onChange: function(n,o){
					},
					onLoadSuccess: function() {
					
					//	alert(f.find('input[name=parentIdTemp]').val());
						//f.find('input[name=parentId]').combotree('setValue', f.find('input[name=parentIdTemp]').val());
					}			
				});
				
				
		}
			
		});
	} else {
		sy.messagerAlert('提示', '请选中要编辑的记录！', 'error');
	}
}

function _search() {
	datagrid.datagrid('load', sy.serializeObject($('#searchForm')));
}
</script>
</body>
</html>
