<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>

<script type="text/javascript" charset="utf-8">
	var datagrid;
	$(function() {

		datagrid = $('#treegrid').treegrid({
			url : '${pageContext.request.contextPath}/district/listJson.action',
			height:$(document.body).height()-100,
			toolbar : [ {
				text : '展开',
				iconCls : 'icon-redo',
				handler : function() {
					var node = datagrid.treegrid('getSelected');
					if (node) {
						datagrid.treegrid('expandAll', node.id);
					} else {
						datagrid.treegrid('expandAll');
					}
				}
			}, '-', {
				text : '折叠',
				iconCls : 'icon-undo',
				handler : function() {
					var node = datagrid.treegrid('getSelected');
					if (node) {
						// 选中子菜单时折叠其父菜单
						var node_children = datagrid.treegrid('getChildren', node.id);
						var node_parent = datagrid.treegrid('getParent', node.id);
						if (node_children == false && node_parent != null) {
							datagrid.treegrid('collapseAll', node_parent.id);
						}
						datagrid.treegrid('collapseAll', node.id);
					} else {
						datagrid.treegrid('collapseAll');
					}
				}
			}, '-', {
				text : '刷新',
				iconCls : 'icon-reload',
				handler : function() {
					datagrid.treegrid('reload');
				}
			}, '-', {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					save();
				}
			},'-', {
				text : '编辑',
				iconCls : 'icon-edit',
				handler : function() {
					update();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			} ],
			idField : 'id',
			treeField : 'districtname',
			iconCls : 'icon-save',
			fit : true,
			fitColumns : true,
			nowrap : false,
			animate : true,
			border : false,
			columns : [ [  {
				title : 'id',
				field : 'id',
				width : 150,
				hidden : true
			}, {
				field : 'districtname',
				title : '名称',
				align:'left',
				width : 150,
				formatter : function(value) {
					if (value) {
						return sy.fs('<span title="{0}">{1}</span>', value, value);
					}
				}
			},{
				field : 'districtid',
				title : '区划编码',
				align:'center',
				width : 150,
				formatter : function(value) {
					if (value) {
						return sy.fs('<span title="{0}">{1}</span>', value, value);
					}
				}
			}, {
				title : '状态',
				field : 'status',
				align:'center',
				sortable : true,
				width : 150,
				formatter : function(value) {
					if (value==1) {
						return "正常";
					}
					return "停用";
				}
			}, {
				title : '顺序',
				field : 'orderid',
				align:'center',
				sortable : true,
				width : 150
			}, {
				title : '级次',
				field : 'levelid',
				align:'center',
				sortable : true,
				width : 150
			} ] ],
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#district').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onExpand : function(row) {
				datagrid.treegrid('unselectAll');
			},
			onCollapse : function(row) {
				datagrid.treegrid('unselectAll');
			}
		});

	});

	function save() {
		var p = sy.dialog({
			title : '新增',
			href : '${pageContext.request.contextPath}/district/save.action',
			width : 370,
			height : 270,
			buttons : [ {
				text : '保存',
				handler : function() {
					var des = p.find('#districtname').val();
					if(des==""){
						sy.messagerAlert('提示', '名称不能空', 'error');
						return;
					}
					var porder = p.find('#orderid').val();
					if(porder==""){
						sy.messagerAlert('提示', '顺序不能空', 'error');
						return;
					}
					
					$.ajax({
				      type : "post",
				      url : "${pageContext.request.contextPath}/district/saveDo.action",
				      data : p.find('form').serialize(),
				      success : function(json){
						if (json.success) {
							datagrid.treegrid('reload');
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
				var f_parent = f.find("input[name='parent.id']");
				f_parent.combotree({
				    url:'${pageContext.request.contextPath}/district/listJson.action',
				  //value:qhcode,
				    onChange: function(n,o){//alert(n+"-"+o);
						/*$.ajax({
						   type: "post",
						   url : "/online/baseinfo/district/regionbm.action",
						   dataType:'json',
						   data:'bm='+n,
						   success: function(data) {
							   f.find("#qhcode").val(data);
							   f.find("#parent").val(f_parent.combotree("getValue"));
						   },
						   error : function() {     
						         alert("异常！");    
						    }
						});*/
					},
					onLoadSuccess: function() {//alert(2);
						/*p.find(".combo input:first").val(qhname);
						p.find("#parent").val(qhcode);
						$.ajax({
						   type: "post",
						   url : "/online/baseinfo/district/regionbm.action",
						   dataType:'json',
						   data:'bm='+qhcode,
						   success: function(data) {
							   f.find("#qhcode").val(data);
						   },
						   error : function() {     
						         alert("异常！");    
						    }
						});*/
					}			
				});
				
			}
		});
	}
	
	function update() {
		var node = datagrid.treegrid('getSelected');
		if (node) {
			var p = sy.dialog({
				title : '修改',
				href : '${pageContext.request.contextPath}/district/update.action?id='+node.id,
				width : 540,
				height : 280,
				buttons : [ {
					text : '保存',
					handler : function() {
						var des = p.find('#districtname').val();
						if(des==""){
							sy.messagerAlert('提示', '名称不能空', 'error');
							return;
						}
						var porder = p.find('#orderid').val();
						if(porder==""){
							sy.messagerAlert('提示', '顺序不能空', 'error');
							return;
						}
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/district/updateDo.action",
					      data : p.find('form').serialize(),
					      success : function(json){
							if (json.success) {
								datagrid.treegrid('reload');
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
					var f_parent = f.find("input[name='parent.id']");
					var mycombotree = f_parent.combotree({
					    url:'${pageContext.request.contextPath}/district/listJson.action',
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
	
	function remove() {
		var node = datagrid.treegrid('getSelected');
		var del_msg = '';
		if (node) {
			if (!datagrid.treegrid('isLeaf', node.id)) {
				del_msg = '<strong>该区划有下级区划</strong>，您确定要删除【' + node.districtname + '】?';
			} else {
				del_msg = '您确定要删除【' + node.districtname + '】？';
			}
			sy.messagerConfirm('提醒', del_msg, function(b) {
				if (b) {
					$.ajax({
						url : '${pageContext.request.contextPath}/district/delete.action',
						data : {
							ids : node.id
						},
						cache : false,
						dataType : 'JSON',
						success : function(r) {
							if (r.success) {
								datagrid.treegrid('reload'); //刷新列表
							//	ctrlTree.tree('reload');/*刷新左侧菜单树*/
							}
							sy.messagerShow({
								msg : r.msg,
								title : '提示'
							});
						}
					});
				}
			});
		}else {
			sy.messagerAlert('提示', '请选择记录！', 'error');
		}
	}
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<table id="treegrid"></table>
	</div>
	
	<div id="district" class="easyui-menu" style="width:120px;display: none;">
		<div onclick="save();" data-options="iconCls:'icon-add'">新增</div>
		<div onclick="update();" data-options="iconCls:'icon-edit'">编辑</div>
		<div onclick="remove();" data-options="iconCls:'icon-remove'">删除</div>
	</div>
	
</body>
</html>