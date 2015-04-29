<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" type="text/css" href="${ctx}/_static/js/ztree/zTreeStyle/zTreeStyle.css">
		<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
		<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
		<script type="text/javascript" src="${ctx}/_static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<div id="datagrid_rolelist" style="overflow: auto;"></div>
	</div>
	<script type="text/javascript" >
	var datagrid;
	$(function() {
		datagrid = $('#datagrid_rolelist').datagrid({
			url : '${pageContext.request.contextPath}/role/listJson.action',
			height:$(document.body).height()-100,
			iconCls : 'icon-save',
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
			}</shiro:hasRole>
			<shiro:hasRole  name="sysadmin">, '-', {
				text : '菜单权限',
				iconCls : 'icon-add',
				handler : function() {
					roleMenu();
				}
			}</shiro:hasRole>
			<shiro:hasRole  name="sysadmin">, '-', {
				text : '操作权限',
				iconCls : 'icon-add',
				handler : function() {
					roleButton();
				}
			}</shiro:hasRole> ],
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
			href : '${pageContext.request.contextPath}/role/save.action',
			width : 370,
			height : 270,
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
				      url : "${pageContext.request.contextPath}/role/saveDo.action",
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
				href : '${pageContext.request.contextPath}/role/update.action?id='+rows[0].id,
				width : 370,
				height : 270,
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
					      url : "${pageContext.request.contextPath}/role/updateDo.action",
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
						url : '${pageContext.request.contextPath}/role/delete.action',
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
	
	
	
	function roleMenu(){
		var rows =  datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '菜单权限',
				href : '${pageContext.request.contextPath}/role/roleMenu.action',
				width : 500,
				height : 400,
				buttons : [ {
					text : '保存',
					handler : function() {
						var f = p.find('form');
						var url='${pageContext.request.contextPath}/role/roleMenuDo.action?id='+rows[0].id;
						var pid = f.find("div[name='authIds']");
						var zTree = $.fn.zTree.getZTreeObj("authIds");
						var nodes = zTree.getCheckedNodes(true);
						//alert(JSON.stringify(nodes));
						var s = '';
						for(var i=0; i<nodes.length; i++){
							if (s != '') s += ',';
							s += nodes[i].id;
						}
						url = url+'&rids='+s;
						var pids = f.find("input[name='rids']");
						pids.val(s);
						
						
						$.ajax({
					      type : "post",
					      url : url,
					      //data : p.find('form').serialize(),
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
				} ] ,
				onLoad : function() {
					var f = p.find('form');
					var authIds = f.find("div[name='authIds']");
					
					f.form('load', {
						id : rows[0].id
					});
					//生成菜单tree
					var setting = {
						check: {
							enable: true
						}
					};
					$.ajax({
						   type: "POST",
						   url: '${pageContext.request.contextPath}/role/roleMenuJson.action?id='+rows[0].id,
						   dataType : 'json', 
						   success: function(msg){
							   //alert(JSON.stringify(msg));
							   $.fn.zTree.init(authIds, setting, msg);
							   $.fn.zTree.getZTreeObj("authIds").expandAll(true);
						   }
					});

					/**
					var ptree = authIds.tree({
						lines : true,
						checkbox:true,
						multiple : true,
						url : '${pageContext.request.contextPath}/role/roleMenuJson.action?id='+rows[0].id,
						editable : false,
						onLoadSuccess : function() {
							if(rows[0].authIds){
								var t = (rows[0].authIds).split(',');
								for ( var i = 0; i < t.length; i++) {
									var node =authIds.tree('find',t[i]);
								    if(node){
		                            authIds.tree('check', node.target);}
								}
							}
							$.messager.progress('close');
							}
					});**/
				 f.form('load', {
						 authIds : sy.getList(rows[0].authIds)
					} );
				} 
			});
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请先选择，再进行操作！', 'error');
		}
	}
	
	function roleButton(){
		var rows =  datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '操作权限',
				href : '${pageContext.request.contextPath}/role/roleButton.action',
				width : 700,
				height :450,
				buttons : [ {
					text : '保存',
					handler : function() {
						var f = p.find('form');
						var actions="";
						//存储所有选择的操作
						var rbuttons = new Array();
						//获取菜单
						var modules = new Array();
						f.find("input[name='menu']").each(function(index){
							modules[modules.length] = this.id;
						});
						//获取勾选按钮
				        f.find("input[type='checkbox']").each(function(index){
							if(this.checked){
								rbuttons[rbuttons.length] = this.name;//菜单id+按钮id
							}
				        });
						//循环遍历资源数组，从操作数组中找出对应的操作依次加入actions变量中
						for(var i=0;i<modules.length;i++){
							var flag=false;
							for(var j=0;j<rbuttons.length;j++){
								//如果操作中包含资源ID，表示当前操作输入该资源，就顺序加入actions中
								if(rbuttons[j].indexOf(modules[i]+":")==0){
									flag=true;
									actions+=rbuttons[j]+",";
								}
							}
							if(flag){
								actions=actions.substring(0,actions.length-1)+";";
							}
						}
						
						var idsInput = f.find('input[name=roleActions]');
						idsInput.val(actions);

						//var url='${pageContext.request.contextPath}/role/saveRoleModuleButton.action?id='+rows[0].id;
						$.ajax({
							   type: "POST",
							   url: '${pageContext.request.contextPath}/role/saveRoleModuleButton.action',
							   data :{id:rows[0].id,roleActions:actions},
							   dataType : 'json', 
							   success: function(json){
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
						/* f.form('submit', {
							url : url,
							success : function(json) {
								//var json = $.parseJSON(d);
								if (json.success) {
									datagrid.datagrid('reload');
									p.dialog('close');
								}
								parent.sy.messagerShow({
									msg : json.msg,
									title : '提示'
								});
							}
						}); */
					} 
				} ] ,
				onLoad : function() { 
					var f = p.find('form');
					var resact = f.find("table[name='dt-resAct']");
					f.form('load', {
						id : rows[0].id
					});
					resact.treegrid({
						url:'${pageContext.request.contextPath}/role/getRoleModulesButtons.action?id='+rows[0].id,
					    idField:'id',    
					    treeField:'name',
						columns : [ [{
						    title:'id',
							field : 'id',
							hidden:true
						},{
						    title:'资源名称',
							field : 'name',
							width : 150,
							formatter : function(value,rowData,rowIndex){
								return '<input type="hidden" id="'+rowData.id+'" name="menu"/>'+rowData.name;
							}
						}, {
							title:'操作',
							field : 'attributes',
							width : 500,
							formatter:function(value,rowData,rowIndex){
								var mbuttons = value.mbuttons;
								if(!!mbuttons && mbuttons.length>0){
									var html = "";
									for(var i=0;i<mbuttons.length;i++){
										html+='<input type="checkbox" ';
										if(mbuttons[i].checked==1){
											html+=' checked="checked" ';
										}
										//在每个操作的name属性的格式为：资源id:操作ename
										html+='name="'+rowData.id+':'+mbuttons[i].buttonid+'"/>'+mbuttons[i].bname;
									}
									return html;
								}else{
									return '<div style="color:red">未分配操作</div>';
								}} 
						}
						 ] ],
						 onLoadSuccess:function(row,data){
							 resact.treegrid("expandAll");
						 }}
					); 
					
				} 
			});
		} else if (rows.length > 1) {
			parent.sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			parent.sy.messagerAlert('提示', '请先选择角色，再进行编辑！', 'error');
		}
	}
	
</script>
</body>

</html>
