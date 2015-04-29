<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>

<script type="text/javascript" charset="utf-8">
var iconData = [ {
	value : '',
	text : '默认'
}, {
	value : '../_static/images/icons/atd.png',
	text : 'menu_atd'
}, {
	value : '../_static/images/icons/auth.png',
	text : 'menu_auth'
}, {
	value : '../_static/images/icons/folder.png',
	text : 'menu_folder'
}, {
	value : '../_static/images/icons/jou_q.png',
	text : 'menu_jou_q'
}, {
	value : '../_static/images/icons/jou.png',
	text : 'menu_jou'
}, {
	value : '../_static/images/icons/log.png',
	text : 'menu_log'
}, {
	value : '../_static/images/icons/menu.png',
	text : 'menu_menu'
}, {
	value : '../_static/images/icons/op.png',
	text : 'menu_op'
}, {
	value : '../_static/images/icons/pub.png',
	text : 'menu_pub'
}, {
	value : '../_static/images/icons/role.png',
	text : 'menu_role'
}, {
	value : '../_static/images/icons/sys.png',
	text : 'menu_sys'
}, {
	value : '../_static/images/icons/unit.png',
	text : 'menu_unit'
}, {
	value : '../_static/images/icons/user.png',
	text : 'menu_user'
} ];
	var treegrid;
	$(function() {
		treegrid = $('#treegrid').treegrid({
			url : '${pageContext.request.contextPath}/module/listJson.action',
			pagination: true,
			pageSize: 2,
			pageList: [10,20,30,40,50],
			pagePosition : 'bottom',
			toolbar : [ {
				text : '展开',
				iconCls : 'icon-redo',
				handler : function() {
					var node = treegrid.treegrid('getSelected');
					if (node) {
						treegrid.treegrid('expand', node.id);
					} else {
						treegrid.treegrid('expandAll');
					}
				}
			}, '-', {
				text : '折叠',
				iconCls : 'icon-undo',
				handler : function() {
					var node = treegrid.treegrid('getSelected');
					if (node) {
						// 选中子菜单时折叠其父菜单
						var node_children = treegrid.treegrid('getChildren', node.id);
						var node_parent = treegrid.treegrid('getParent', node.id);
						if (node_children == false && node_parent != null) {
							treegrid.treegrid('collapseAll', node_parent.id);
						}
						treegrid.treegrid('collapseAll', node.id);
					} else {
						treegrid.treegrid('collapseAll');
					}
				}
			}, '-', {
				text : '刷新',
				iconCls : 'icon-reload',
				handler : function() {
					treegrid.treegrid('reload');
				}
			}<shiro:hasRole  name="sysadmin">, '-', {
				text : '增加',
				iconCls : 'icon-add',
				handler : function() {
					save();
				}
			}</shiro:hasRole>
			<shiro:hasRole  name="sysadmin">, '-', {
				text : '编辑',
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
			}</shiro:hasRole>
			<shiro:hasRole  name="sysadmin">, '-', {
				text : '分配操作',
				iconCls : 'icon-remove',
				handler : function() {
					menuButton();
				}
			}</shiro:hasRole>,'-'],
			idField : 'id',
			treeField : 'name',
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
				field : 'name',
				title : '名称',
				align:'left',
				width : 150,
				formatter : function(value) {
					if (value) {
						return sy.fs('<span title="{0}">{1}</span>', value, value);
					}
				}
			},{
				field : 'curl',
				title : '地址',
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
			} ] ],
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onExpand : function(row) {
				treegrid.treegrid('unselectAll');
			},
			onCollapse : function(row) {
				treegrid.treegrid('unselectAll');
			},
			onLoadSuccess : function(data){
				var iframeobj = $(window.parent.document).find("#cdgl");
				iframeobj.height = 400;
			}
		});
		function pagerFilter(data){
			if ($.isArray(data)){    // is array  
				data = {  
					total: data.length,  
					rows: data  
				}  
			}
			var dg = $(this);  
			var state = dg.data('treegrid');
			var opts = dg.treegrid('options');  
			var pager = dg.treegrid('getPager');  
			pager.pagination({
				onSelectPage:function(pageNum, pageSize){  
					opts.pageNumber = pageNum;  
					opts.pageSize = pageSize;  
					pager.pagination('refresh',{  
						pageNumber:pageNum,  
						pageSize:pageSize  
					});  
					dg.treegrid('loadData',state.allRows);  
				}  
			});  
			opts.pageNumber = pager.pagination('options').pageNumber || 1;
			if (!state.allRows){
				state.allRows = data.rows;
			}
			var topRows = [];
			var childRows = [];
			$.map(state.allRows, function(row){
				row._parentId ? childRows.push(row) : topRows.push(row);
			});
			data.total = topRows.length;
			var start = (opts.pageNumber-1)*parseInt(opts.pageSize);  
			var end = start + parseInt(opts.pageSize);  
			data.rows = $.extend(true,[],topRows.slice(start, end).concat(childRows));
			return data;
		}
		
		var appendMethod = $.fn.treegrid.methods.append;
		var removeMethod = $.fn.treegrid.methods.remove;
		var loadDataMethod = $.fn.treegrid.methods.loadData;
		$.extend($.fn.treegrid.methods, {
			clientPaging: function(jq){
				return jq.each(function(){
					var state = $(this).data('treegrid');
					var opts = state.options;
					opts.loadFilter = pagerFilter;
					var onBeforeLoad = opts.onBeforeLoad;
					opts.onBeforeLoad = function(row,param){
						state.allRows = null;
						return onBeforeLoad.call(this, row, param);
					}
					$(this).treegrid('loadData', state.data);
					if (opts.url){
						$(this).treegrid('reload');
					}
				});
			},
			loadData: function(jq, data){
				jq.each(function(){
					$(this).data('treegrid').allRows = null;
				});
				return loadDataMethod.call($.fn.treegrid.methods, jq, data);
			},
			append: function(jq, param){
				return jq.each(function(){
					var state = $(this).data('treegrid');
					if (state.options.loadFilter == pagerFilter){
						$.map(param.data, function(row){
							row._parentId = row._parentId || param.parent;
							state.allRows.push(row);
						});
						$(this).treegrid('loadData', state.allRows);
					} else {
						appendMethod.call($.fn.treegrid.methods, $(this), param);
					}
				})
			},
			remove: function(jq, id){
				return jq.each(function(){
					if ($(this).treegrid('find', id)){
						removeMethod.call($.fn.treegrid.methods, $(this), id);
					}
					var state = $(this).data('treegrid');
					if (state.options.loadFilter == pagerFilter){
						for(var i=0; i<state.allRows.length; i++){
							if (state.allRows[i][state.options.idField] == id){
								state.allRows.splice(i,1);
								break;
							}
						}
						$(this).treegrid('loadData', state.allRows);
					}
				})
			},
			getAllRows: function(jq){
				return jq.data('treegrid').allRows;
			}
		});
		treegrid.treegrid('clientPaging');

	});
	function save() {
		var p = sy.dialog({
			title : '新增',
			href : '${pageContext.request.contextPath}/module/save.action',
			width : 370,
			height : 270,
			buttons : [ {
				text : '保存',
				handler : function() {
					var des = p.find('#name').val();
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
				      url : "${pageContext.request.contextPath}/module/saveDo.action",
				      data : p.find('form').serialize(),
				      success : function(json){
						if (json.success) {
							treegrid.treegrid('reload');
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
				var f_parent = f.find('input[name=superid]');
				f_parent.combotree({
				    url:'${pageContext.request.contextPath}/module/listJsonForMenuTree.action',
				  //value:qhcode,
				    onChange: function(n,o){
					},
					onLoadSuccess: function() {
					}			
				});
				
				var iconCls = f.find('input[name=icon]');
				var iconCombo = iconCls.combobox({
					data : iconData,
					formatter : function(v) {
						return sy.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.text, v.text);
					}
				});
				
			}
		});
	}
	
	function update() {
		var node = treegrid.treegrid('getSelected');
		if (node) {
			var p = sy.dialog({
				title : '修改',
				href : '${pageContext.request.contextPath}/module/update.action?id='+node.id,
				width : 540,
				height : 280,
				buttons : [ {
					text : '保存',
					handler : function() {
						var des = p.find('#name').val();
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
					      url : "${pageContext.request.contextPath}/module/updateDo.action",
					      data : p.find('form').serialize(),
					      success : function(json){
							if (json.success) {
								treegrid.treegrid('reload');
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
					var iconCls = f.find('input[name=icon]');
					var iconCombo = iconCls.combobox({
						data : iconData,
						formatter : function(v) {
							return sy.fs('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.text, v.text);
						}
					});
					
			}
				
			});
		} else {
			sy.messagerAlert('提示', '请选中要编辑的记录！', 'error');
		}
	}
	
	function remove() {
		var node = treegrid.treegrid('getSelected');
		var del_msg = '';
		if (node) {
			if (!treegrid.treegrid('isLeaf', node.id)) {
				del_msg = '<strong>该菜单有子菜单</strong>，您确定要删除【' + node.districtname + '】?';
			} else {
				del_msg = '您确定要删除【' + node.name + '】？';
			}
			sy.messagerConfirm('提醒', del_msg, function(b) {
				if (b) {
					$.ajax({
						url : '${pageContext.request.contextPath}/module/delete.action',
						data : {
							ids : node.id
						},
						cache : false,
						dataType : 'JSON',
						success : function(r) {
							if (r.success) {
								treegrid.treegrid('reload'); //刷新列表
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
	
	function menuButton() {
		var node = treegrid.treegrid('getSelected');
		if (node) {
			var p = sy.dialog({
				title : '分配菜单操作',
				href : '${pageContext.request.contextPath}/module/menuButton.action?id='+node.id,
				width : 640,
				height : 480,
				buttons : [ {
					text : '分配',
					handler : function() {
						var dg = p.find('div[id=datagrid_buttonlist]');
						var rows = dg.datagrid('getSelections');
						if(rows.length<=0){
							sy.messagerShow({
								msg : "请选择要分配的操作",
								title : '提示'
							});
							return false;
						}
						var idsarr = new Array();
						for(var i=0;i<rows.length;i++){
							idsarr[i] = rows[i].id;
						}
						var ids = idsarr.join(",");						
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/module/menuButtonDo.action",
					      data : {mid:node.id,bids:ids},
					      success : function(json){
							if (json.success) {
								treegrid.treegrid('reload');
								p.dialog('close');
							}
							sy.messagerShow({
								msg : json.msg,
								title : '提示'
							});
					      }
						 });
					},
					onLoad : function(){
						
					}
				} ]
			});
		} else {
			sy.messagerAlert('提示', '请选中要编辑的记录！', 'error');
		}
	}
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<table id="treegrid" style="overflow: auto;"></table>
	</div>
</body>
</html>