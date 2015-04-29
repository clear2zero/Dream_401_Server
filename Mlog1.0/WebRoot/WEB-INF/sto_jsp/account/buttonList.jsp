<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
		<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<div id="datagrid_buttonlist" style="overflow: auto;"></div>
	</div>
	<script type="text/javascript" >
	var datagrid;
	$(function() {
		datagrid = $('#datagrid_buttonlist').datagrid({
			url : '${pageContext.request.contextPath}/button/listJson.action',
			iconCls : 'icon-save',
			height:$(document.body).height()-100,
			pagination : true,
			pageSize : 10,
			pageList : [5,10,20,30,50],
			pageNumber:1,
			//width:750,
			//height:450,
			fitColumns : true,
			nowrap : true,
			border : false,
			idField : 'id',
			sortName : 'orderid',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			columns : [ [ {
				title : '编号',
				field : 'id',
				width : 150,
				sortable : true,
				checkbox : true
			}, {
				title : '序号',
				field : 'orderid',
				width : 30,
				align:'center',
				formatter:function(value, row, index){
					return index+1;
				}
			}, {
				title : '名称',
				field : 'name',
				width : 150,
				align:'center'
			}, {
				title : '英文名称',
				field : 'enname',
				width : 150,
				align:'center'
			},{
				title : '方法名',
				field : 'handler',
				width : 150,
				align:'center'
			}, {
				title : '状态',
				field : 'status',
				width : 30,
				align:'center',
				formatter : function(value) {
					if (value==1) {
						return "正常";
					}
					return "停用";
				}
			}, {
				title : '备注',
				field : 'remark',
				width : 150,
				align:'center'
			} ] ],
			toolbar : [ 
			<shiro:hasRole  name="sysadmin">{
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					save();
				}
			}</shiro:hasRole>
			<shiro:hasRole  name="sysadmin">, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					update();
				}
			}</shiro:hasRole>
			<shiro:hasRole  name="sysadmin">, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}</shiro:hasRole>],
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

function save() {
		var p = sy.dialog({
			title : '新增',
			href : '${pageContext.request.contextPath}/button/save.action',
			width : 370,
			height : 320,
			buttons : [ {
				text : '保存',
				handler : function() {
				
					var name = p.find('#name').val();
					if(name==""){
						sy.messagerAlert('提示', '名称不能空', 'error');
						return;
					}
					
					
					$.ajax({
				      type : "post",
				      url : "${pageContext.request.contextPath}/button/saveDo.action",
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
			} ]
		});
	}
	
	function update() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '修改',
				href : '${pageContext.request.contextPath}/button/update.action?id='+rows[0].id,
				width : 370,
				height : 320,
				buttons : [ {
					text : '保存',
					handler : function() {
						var name = p.find('#name').val();
						if(name==""){
							sy.messagerAlert('提示', '名称不能空', 'error');
							return;
						}
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/button/updateDo.action",
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
						status:rows[0].status
					});
			}
				
			});
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请勾选要编辑的记录！', 'error');
		}
	}
	
	function remove() {
		var rows = datagrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			sy.messagerConfirm('请确认', '您要删除当前所选记录吗？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/button/delete.action',
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(d) {
							datagrid.datagrid('load');
							datagrid.datagrid('unselectAll');
							sy.messagerShow({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		} else {
			sy.messagerAlert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
</script>
</body>

</html>
