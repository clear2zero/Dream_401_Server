<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
	<OBJECT classid="CLSID:AE745FFD-E912-4411-AD53-91B19211B667"  id="doit" VIEWASTEXT width="1" height="1"></object>
</head>
<body class="easyui-layout" data-options="fit:true">

<div data-options="region:'north',border:false,title:'查询'"
		style="height: 60px; overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar"
				style="width: 100%; height: 100%;">
				<tr>
					<td style="font-size: 12px; width: 20%; align=left;">账号:
					<input name="username" />
					</td>
					<shiro:hasRole name="sysadmin">
					<td style="font-size: 12px; width: 20%; align=left;">单位:
						<input id="divid" name="divid" />
					</td>
					</shiro:hasRole>
					<shiro:hasAnyRoles name="sysadmin,unitmanager">
					<td style="font-size: 12px; width: 20%; align=left;">部门:
						<input id="deptid" name="deptid" />
					</td>
					</shiro:hasAnyRoles>
					<td><input  type="text" style="display:none" /> 
					<a href="javascript:_search();" class="easyui-linkbutton">查询</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<table id="datagrid" style="overflow: auto;"></table>
	</div>
	   
<script type="text/javascript" >
	var datagrid;
	$(function() {
		$('input:text:first').focus(); //把焦点放在第一个文本框
		$('input').keypress(function (e) { 
		    if (e.which == 13) {//回车
		        _search();
		    }
		});
		try{
			$("#divid").combobox({
			    url:'${ctx}/unit/listJsonNoPage.action',
			    valueField:'divid',
			    textField: 'divname',
			    panelHeight:'250',
			    onSelect : function(r){
			    	$("#deptid").combotree('clear');
			    	$("#deptid").combotree('reload','${ctx}/dept/listJson.action?divid='+r.divid);  
			    },
			    onChange : function(n,o){
					if(n=='' || n == 'undefined'){
						$("#divid").combobox('reset');
					}		    	
			    }
			});
			$("#deptid").combotree({
			    url: '${ctx}/dept/listJson.action',
			    editable : true,
			    onBeforeSelect : function(node){
			    	/* var parent = $("#deptid").combotree('tree').tree('getParent',node.target);
			    	if(parent == null){
			    		alert("请选择具体部门");
			    		return false;
			    	} */
			    	return true;
			    },
			    onBeforeLoad : function(param){
			    },
			    onChange : function(n,o){
					if(n=='' || n == 'undefined'){
						$("#deptid").combotree('reset');
					}		    	
			    }
			});
		}catch(e){
			e.message;
		}
	
		datagrid = $('#datagrid').datagrid({
			url : '${pageContext.request.contextPath}/user/listJson.action',
			height:$(document.body).height()-100,
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [5,10,20,30,50],
			pageNumber:1,
			fitColumns : true,
			nowrap : true,
			border : false,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			checkOnSelect : true,
			selectOnCheck : true,
			columns : [ [ {
				title : '编号',
				field : 'id',
				width : 150,
				sortable : true,
				checkbox : true
			}, {
				title : '账号',
				field : 'username',
				width : 120,
				align:'center'
			}, {
				title : '用户名',
				field : 'name',
				width : 120,
				align:'center'
			}, {
				title : '服务端证书',
				field : 'hcertcn',
				width : 120,
				align:'center'
			},{
				title : '移动端证书',
				field : 'scertcn',
				width : 120,
				align:'center'
			}, {
				title : '手机号',
				field : 'mobilephone',
				width : 120,
				align:'center'
			},{
				title : '身份证号',
				field : 'identitycard',
				width : 120,
				align:'center'
			},{
				title : '状态',
				field : 'status',
				width : 120,
				align:'center',
				formatter : function(v,row,index) {
					if (row.isdelete != 0 || row.isenable != 1) {
						return "已冻结";
					}else {
						return "正常";
					}
				}
			},{
				title : '所属单位',
				field : 'divname',
				width : 150,
				align:'center'
			},{
				title : '部门',
				field : 'deptname',
				width : 150,
				align:'center'
			},{
				title : '角色',
				field : 'rolename',
				width : 150,
				align:'center'
			} ] ],
			toolbar : [ {
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
				text : '冻结',
				iconCls : 'icon-edit',
				handler : function() {
					remove();
				}
			}, '-', {
				text : '解冻',
				iconCls : 'icon-edit',
				handler : function() {
					unfreeze();
				}
			}/* , '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			} */, '-', {
				text : '绑定服务端证书',
				iconCls : 'icon-edit',
				handler : function() {
					bindCert(0);
				}
			}, '-', {
				text : '绑定移动端证书',
				iconCls : 'icon-edit',
				handler : function() {
					bindCert(1);
				}
			}, '-', {
				text : '解绑服务端证书',
				iconCls : 'icon-edit',
				handler : function() {
					unbindCert(0);
				}
			}, '-', {
				text : '解绑移动端证书',
				iconCls : 'icon-edit',
				handler : function() {
					unbindCert(1);
				}
			}, '-', {
				text : '修改用户密码',
				iconCls : 'icon-edit',
				handler : function() {
					modifyPasswd();
				}
			}<shiro:hasAnyRoles name="sysadmin,unitmanager">, '-', {
				text : '发送短信',
				iconCls : 'icon-add',
				handler : function() {
					sendSms();
				}
			}</shiro:hasAnyRoles>
			<shiro:hasAnyRoles name="sysadmin,unitmanager">, '-', {
				text : '全部发送',
				iconCls : 'icon-add',
				handler : function() {
					sendSmsAll();
				}
			}</shiro:hasAnyRoles>],
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onLoadSuccess : function(data){
				datagrid.datagrid('uncheckAll');
			}
		});

	});

	function save() {
		var p = sy.dialog({
			title : '新增',
			href : '${pageContext.request.contextPath}/user/save.action',
			width : 480,
			height : 380,
			buttons : [ {
				text : '保存',
				handler : function() {
					if (!p.find('form').form('validate')) {
						return false;
					}
					var des = p.find('#logonid').val();
					if(des==""){
						sy.messagerAlert('提示', '名称不能空', 'error');
						return;
					}
					$.ajax({
				      type : "post",
				      url : "${pageContext.request.contextPath}/user/saveDo.action",
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
			}
		});
	}
	
	function update() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '修改',
				href : '${pageContext.request.contextPath}/user/update.action?id='+rows[0].id,
				width : 470,
				height : 400,
				buttons : [ {
					text : '保存',
					handler : function() {
						if (!p.find('form').form('validate')) {
							return false;
						}
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/user/updateDo.action",
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
				}
				
			});
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请勾选要编辑的记录！', 'error');
		}
	}
	function sendSms() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			var p = sy.dialog({
				title : '短信发送',
				href : '${pageContext.request.contextPath}/user/send.action',
				width : 470,
				height : 350,
				buttons : [ {
					text : '发送',
					handler : function() {
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/user/sendSms.action",
					      data : p.find('form').serialize(),
					      success : function(json){
							if (json.success) {
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
					var f_ids = f.find("input[id=ids]");
					//var f_divid = f.find("input[id=divid]");
					var idarr = new Array();
					for(var i=0;i<rows.length;i++){
						idarr.push(rows[i].id);
					}
					var ids = idarr.join(",");
					f_ids.val(ids);
				}
				
			});
		} else {
			sy.messagerAlert('提示', '请选择发送用户！', 'error');
		}
	}
	
	function sendSmsAll() {
		if($("#divid").combobox('getText')==''){
			$("#divid").combobox('setValue','');
		}
		var divname = $("#divid").combobox('getText');
		var divid = $("#divid").combobox('getValue');
		var p = sy.dialog({
			title : '短信发送',
			href : '${pageContext.request.contextPath}/user/send.action',
			width : 470,
			height : 350,
			buttons : [ {
				text : '发送',
				handler : function() {
					if (!p.find('form').form('validate')) {
						return false;
					}
					var confirm = "";
					if(divid != ''){
						confirm = "向【"+divname+"】该单位所有人发送短信？";
					}else{
						confirm = "向所有单位的所有用户发送短信？";
					}
					sy.messagerConfirm('请确认', confirm, function(r) {
						if (r) {
							$.ajax({
							      type : "post",
							      url : "${pageContext.request.contextPath}/user/sendSmsAll.action",
							      data : p.find('form').serialize(),
							      success : function(json){
									if (json.success) {
										p.dialog('close');
									}
									sy.messagerShow({
										msg : json.msg,
										title : '提示'
									});
							      }
							});
						}
					});
				}
			} ],
			onLoad : function() {
				var f = p.find('form');
				var f_divid = f.find("input[id=divid]");
				f_divid.val($("#divid").combobox('getValue'));
			}
		});
	}
	function modifyPasswd() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '修改用户密码',
				href : '${pageContext.request.contextPath}/user/modifyPasswd.action?id='+rows[0].id,
				width : 600,
				height : 200,
				buttons : [ {
					text : '保存',
					handler : function() {
						var f = p.find('form');
						if (!f.form('validate')) {
							return false;
						}
						var ps1 = f.find('input[name=pwd]');
						var ps2 = f.find('input[name=rpwd]');
						if(String(ps1.val()).replace('/\s/','') == '' || String(ps2.val()).replace('/\s/','') == ''){
							sy.messagerShow({
								msg : "密码不能为空",
								title : '提示'
							});
							return false;
						}
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/user/modifyPasswdDo.action",
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
				onLoad : function(){
				}
			});
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请勾选要编辑的记录！', 'error');
		}
	}
	
	/* function resetPassword() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			sy.messagerConfirm('请确认', '重置后密码为123456，您要重置该用户的密码吗？', function(r) {
				if (r) {
					$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/user/resetPassword.action",
					      data : {id:rows[0].id},
					      success : function(json){
							if (json.success) {
							}
							sy.messagerShow({
								msg : json.msg,
								title : '提示'
							});
					      }
					});
				}
			});
			
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能选择一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请勾选记录！', 'error');
		}
	} */
	
	
	function remove() {
		var rows = datagrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			sy.messagerConfirm('请确认', '您要冻结选择用户吗？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/user/delete.action',
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
			sy.messagerAlert('提示', '请选择要冻结的用户！', 'error');
		}
	}
	function unfreeze() {
		var rows = datagrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			sy.messagerConfirm('请确认', '您要解冻选择用户吗？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/user/unfreeze.action',
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
			sy.messagerAlert('提示', '请选择要解冻的用户！', 'error');
		}
	}
	function bindCert(flag){
		var rows = datagrid.datagrid('getChecked');
		var ids = [];
		if (rows.length == 1) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
			var certcn = "";
			if(flag == 0){
				var certINfo = doit.GetCertInfoTotal();
				if (certINfo != ""){
					var certCn2 = "";
					var certInfoArray = certINfo.split(",");
					for (i = 0; i < certInfoArray.length; i++){
						if (certInfoArray[i].substring(0,2) == "G="){
							certcn = certInfoArray[i].substring(2);
						}
						if (certInfoArray[i].substring(0,2) == "CN") {
							certCn2 = certInfoArray[i].substring(3);
						}
					}
					if (certcn.length < certCn2.length) {
						certcn = certCn2;
					}
					sy.messagerConfirm('请确认', '确定要将当前证书“'+certcn+'”绑定到选择的用户吗？', function(r) {
						if (r) {
							$.ajax({
								url : '${pageContext.request.contextPath}/user/bindCert.action',
								data : {
									ids : ids.join(','),
									flag : flag,
									certcn : certcn
								},
								dataType : 'JSON',
								success : function(d) {
									if(d.success){
										datagrid.datagrid('load');
										datagrid.datagrid('unselectAll');
									}
									sy.messagerShow({
										title : '提示',
										msg : d.msg
									}); 
								}
							});
						}
					});
				}else {
					$.messager.prompt('请输入', '服务端证书唯一项:', function(r){
						if (r){
							certcn = r;
							$.ajax({
								url : '${pageContext.request.contextPath}/user/bindCert.action',
								data : {
									ids : ids.join(','),
									flag : flag,
									certcn : certcn
								},
								dataType : 'json',
								success : function(d) {
									if(d.success){
										datagrid.datagrid('load');
										datagrid.datagrid('unselectAll');
									}
									sy.messagerShow({
										title : '提示',
										msg : d.msg
									});
								}
							});
						}
					});
					return;
				}
			}else{
				$.messager.prompt('请输入', '移动端证书唯一项:', function(r){
					if (r){
						certcn = r;
						$.ajax({
							url : '${pageContext.request.contextPath}/user/bindCert.action',
							data : {
								ids : ids.join(','),
								flag : flag,
								certcn : certcn
							},
							dataType : 'json',
							success : function(d) {
								if(d.success){
									datagrid.datagrid('load');
									datagrid.datagrid('unselectAll');
								}
								sy.messagerShow({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
				return;
			}
			
			
		} else {
			sy.messagerAlert('提示', '请选择一条记录！', 'error');
		}
	}
	function unbindCert(flag){
		var rows = datagrid.datagrid('getChecked');
		var ids = [];
		if (rows.length == 1) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
			$.ajax({
				url : '${pageContext.request.contextPath}/user/unbindCert.action',
				data : {
					ids : ids.join(','),
					flag : flag
				},
				dataType : 'JSON',
				success : function(d) {
					if(d.success){
						datagrid.datagrid('load');
						datagrid.datagrid('unselectAll');
					}
					sy.messagerShow({
						title : '提示',
						msg : d.msg
					}); 
				}
			});
		} else {
			sy.messagerAlert('提示', '请选择一条记录！', 'error');
		}
	}
	function _search() {
		try{
			if($("#divid").combobox('getText') == ''){
				$("#divid").combobox('setValue','');
			}
		}catch(e){
			e.message;
		}
		
		try{
			if($("#deptid").combotree('getText') == ''){
				$("#deptid").combotree('setValue','');
			}
		}catch(e){
			e.message;
		}
		
		datagrid.datagrid('uncheckAll');
		datagrid.datagrid('load', sy.serializeObject($('#searchForm')));
		
	}
	
</script>
</body>
</html>
