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
		try{
			$("#divid").combobox({
			    url:'${ctx}/unit/listJsonNoPage.action',
			    valueField:'divid',
			    textField: 'divname',
			    panelHeight:'250',
			    editable : false,
			    onSelect : function(r){
			    	_search(r);  
			    }
			});
		}catch(e){
			e.message;
		}
		datagrid = $('#treegrid').treegrid({
			url : '${pageContext.request.contextPath}/dept/listJson.action',
			pagination: true,
			pageSize: 2,
			pageList: [10,20,30,40,50],
			pagePosition : 'bottom',
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
			treeField : 'deptname',
			iconCls : 'icon-save',
			fit : true,
			nowrap : false,
			animate : true,
			border : false,
			columns : [ [  {
				title : 'id',
				field : 'id',
				width : 150,
				hidden : true
			}, {
				field : 'deptname',
				title : '部门名称',
				halign : 'center',
				align:'left',
				width : 250,
				formatter : function(value) {
					if (value) {
						return sy.fs('<span title="{0}">{1}</span>', value, value);
					}
				}
			},{
				field : 'deptid',
				title : '部门编码',
				halign : 'center',
				align:'left',
				width : 80,
				formatter : function(value) {
					if (value) {
						return sy.fs('<span title="{0}">{1}</span>', value, value);
					}
				}
			}, {
				title : '级次',
				field : 'level',
				halign : 'center',
				align:'right',
				sortable : true,
				width : 50
			}, {
				title : '顺序',
				field : 'orderid',
				halign : 'center',
				align:'right',
				sortable : true,
				width : 50
			},{
				title : '创建时间',
				field : 'createtime',
				halign : 'center',
				align:'left',
				sortable : true,
				width : 200,
				formatter:function(v,r,i){
					return v ==  null ? "" : v.substring(0,19);
				}
			} ] ],
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#dept').menu('show', {
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
		datagrid.treegrid('clientPaging');

	});

	function save() {
		var p = sy.dialog({
			title : '新增',
			href : '${pageContext.request.contextPath}/dept/save.action',
			width : 370,
			height : 250,
			buttons : [ {
				text : '保存',
				handler : function() {
					var des = p.find('#deptname').val();
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
				      url : "${pageContext.request.contextPath}/dept/saveDo.action",
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
				var divid = null;
				try{
					divid = $("#divid").combobox('getValue');
				}catch(e){
					e.message;
				}
				f_parent.combotree({
				    url:'${pageContext.request.contextPath}/dept/listJson.action?divid='+divid,
				  //value:qhcode,
				    onChange: function(n,o){//alert(n+"-"+o);
						/*$.ajax({
						   type: "post",
						   url : "/online/baseinfo/dept/regionbm.action",
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
						   url : "/online/baseinfo/dept/regionbm.action",
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
				href : '${pageContext.request.contextPath}/dept/update.action?id='+node.id,
				width : 540,
				height : 280,
				buttons : [ {
					text : '保存',
					handler : function() {
						var des = p.find('#deptname').val();
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
					      url : "${pageContext.request.contextPath}/dept/updateDo.action",
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
					var divid = null;
					try{
						divid = $("#divid").combobox('getValue');
					}catch(e){
						e.message;
					}
					var mycombotree = f_parent.combotree({
					    url:'${pageContext.request.contextPath}/dept/listJson.action?divid='+divid,
					    lines : true,
					    onChange: function(n,o){
						},
						onBeforeSelect : function(n){
							return fact(f_parent.combotree('tree'),n,node);
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
	
	function fact(tree,n,node){
		if(n.id == node.id){
			return false;
		}else {
			var p = tree.tree('getParent',n.target);
			if(p){
				return fact(tree,p,node);
			}else {
				return true;
			}
		}
	}
	
	function remove() {
		var node = datagrid.treegrid('getSelected');
		var del_msg = '';
		if (node) {
			if (!datagrid.treegrid('isLeaf', node.id)) {
				del_msg = '<strong>该部门有下级部门</strong>，您确定要删除【' + node.deptname + '】?';
			} else {
				del_msg = '您确定要删除【' + node.deptname + '】？';
			}
			sy.messagerConfirm('提醒', del_msg, function(b) {
				if (b) {
					$.ajax({
						url : '${pageContext.request.contextPath}/dept/delete.action',
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
function _search(r) {
	datagrid.treegrid('options').url = "${pageContext.request.contextPath}/dept/listJson.action?divid="+r.divid;
	datagrid.treegrid('reload');
}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<shiro:hasRole name="sysadmin">
	<div data-options="region:'north',border:false,title:'查询'"
		style="height: 60px; overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar"
				style="width: 100%; height: 100%;">
				<tr>
					
					<td style="font-size: 12px; width: 20%; align=left;">单位:
						<input id="divid" name="divid" />
					</td>
					
				</tr>
			</table>
		</form>
	</div>
	</shiro:hasRole>
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<table id="treegrid"></table>
	</div>
	
	<div id="dept" class="easyui-menu" style="width:120px;display: none;">
		<div onclick="save();" data-options="iconCls:'icon-add'">新增</div>
		<div onclick="update();" data-options="iconCls:'icon-edit'">编辑</div>
		<div onclick="remove();" data-options="iconCls:'icon-remove'">删除</div>
	</div>
	
</body>
</html>