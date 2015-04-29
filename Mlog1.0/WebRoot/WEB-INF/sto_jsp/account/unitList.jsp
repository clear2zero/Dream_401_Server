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
	<table id="datagrid"></table>
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
	
		datagrid = $('#datagrid').datagrid({
			url : '${pageContext.request.contextPath}/unit/listJson.action',
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
			singleSelect : true,
			columns : [ [ {
				title : '编号',
				field : 'id',
				width : 150,
				sortable : true,
				hidden : true
			}, {
				title : '序号',
				field : 'orderid',
				width : 30,
				align:'center',
				formatter:function(value, row, index){
					return index+1;
				}
			},{
				title : '单位ID',
				field : 'divid',
				width : 80,
				align:'center'
			}, {
				title : '单位名称',
				field : 'divname',
				width : 200,
				align:'center'
			}, {
				title : '单位地址',
				field : 'addr',
				width : 200,
				align:'center'
			},{
				title : '单位电话',
				field : 'tel',
				width : 100,
				align:'center'
			}, {
				title : '联系人',
				field : 'linkman',
				width : 50,
				align:'center'
			},{
				title : '法人',
				field : 'corporation',
				width : 50,
				align:'center'
			}/* ,{
				title : '证书许可数量',
				field : 'licence',
				width : 50,
				align:'center'
			} */] ],
			toolbar : [ /* {
				text : '注册单位',
				iconCls : 'icon-add',
				handler : function() {
					registerUnit();
				}
			},'-', */ {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					update();
				}
			}, /* '-', {
				text : '查看详细信息',
				iconCls : 'icon-search',
				handler : function() {
					query();
				}
			}, */ '-', {
				text : '单位系统设置',
				iconCls : 'icon-edit',
				handler : function() {
					unitsettings();
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

	function registerUnit() {
		var p = sy.dialog({
			title : '注册单位',
			href : '${pageContext.request.contextPath}/unit/register.action',
			width : 470,
			height : 400,
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
				      url : "${pageContext.request.contextPath}/unit/registerDo.action",
				      data : p.find('form').serialize(),
				      success : function(json){
				    	  alert(json);
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
				href : '${pageContext.request.contextPath}/unit/unitUpdate.action?id='+rows[0].id,
				width : 470,
				height : 400,
				buttons : [ {
					text : '保存',
					handler : function() {
						var name = p.find('#logonid').val();
						if(name==""){
							sy.messagerAlert('提示', '登录名不能空', 'error');
							return;
						}
						$.ajax({
					      type : "post",
					      url : "${pageContext.request.contextPath}/unit/unitUpdateDo.action",
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
	function query() {		
		var p = sy.dialog({
			title : '单位信息',
			href : '${pageContext.request.contextPath}/unit/unitQuery.action',
			width : 470,
			height : 400,
			buttons : [],
			onLoad : function() {
			}
			
		});
	}
	function unitsettings() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '单位系统设置',
				href : '${pageContext.request.contextPath}/unitsettings/list.action',
				width : 670,
				height : 400,
				buttons : [{
					text : '保存',
					handler : function() {
						var dg = p.find("table[id='datagrid']");
						
						dg.datagrid('acceptChanges');
						//var rows = dg.datagrid('getChanges','updated');
						var rows = dg.datagrid('getRows');
						var params=[];
						for(var i=0;i<rows.length;i++){
							param={};
							param.id=rows[i].id;
							param.value=rows[i].value;
							params.push(param);
						}
						$.ajax({
						      type : "post",
						      url : "${pageContext.request.contextPath}/unitsettings/updateDo.action",
						      data : {params:JSON.stringify(params)},
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
				}],
				onLoad : function() {
					var f = p.find('form');
					f.form('load', {
						date:rows[0].date,
						userid:rows[0].userid
					});
					var datagrid = f.find("table[id='datagrid']").datagrid({
						url : '${pageContext.request.contextPath}/unitsettings/listJson.action',
						queryParams: {
							divid: rows[0].divid,    
						    flag: 0
						},
						pagination : true,
						pageSize : 10,
						pageList : [5,10,20,30,50],
						pageNumber:1,
						pagePosition : 'bottom',
						fitColumns:true,
						nowrap : true,
						border : false,
						idField : 'orderid',
						sortName : 'orderid',
						sortOrder : 'desc',
						checkOnSelect : true,
						singleSelect : true,
						onClickCell : onClickCell,
						columns : [ [ {
							title : '唯一项',
							field : 'id',
							width : 20,
							sortable : true,
							hidden : true
						}, {
							title : '序号',
							field : 'orderid',
							width : 30,
							halign:'center',
							align:'right',
							formatter:function(value, row, index){
								return index+1;
							}
						},{
							title : '设置项',
							field : 'name',
							width : 200,
							halign:'center',
							align:'left'
						}, {
							title : '唯一标识',
							field : 'skey',
							width : 50,
							halign:'center',
							align:'left'
						},{
							title : '设置值',
							field : 'value',
							width : 100,
							halign:'center',
							align:'left',
							editor:{
								type:'text'
							}
						}] ],
						toolbar : [ {
							text : '从服务器更新配置',
							iconCls : 'icon-redo',
							handler : function() {
								var divid = rows[0].divid;
								p.find("table[id='datagrid']").datagrid('load',{divid:rows[0].divid,flag:1});
							}
						},'-', {
							text : '重置服务器默认配置',
							iconCls : 'icon-redo',
							handler : function() {
								p.find("table[id='datagrid']").datagrid('load',{divid:rows[0].divid,flag:2});
							}
						}]
					});
					
					$.extend($.fn.datagrid.defaults.editors, {
						text: {
							init: function(container, options){
					            var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
					            return input;
					        },
					        destroy: function(target){
					            $(target).remove();
					        },
					        getValue: function(target){
					           return $(target).val();
					        },
					       setValue: function(target, value){
					            $(target).val(value);
					        },
					       resize: function(target, width){
					           $(target)._outerWidth(width);
					       }
					    }
					});
					 $.extend($.fn.datagrid.methods, {
			             editCell: function(jq,param){
			                 return jq.each(function(){
			                     var opts = $(this).datagrid('options');
			                     var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
			                     for(var i=0; i<fields.length; i++){
			                         var col = $(this).datagrid('getColumnOption', fields[i]);
			                         col.editor1 = col.editor;
			                         if (fields[i] != param.field){
			                             col.editor = null;
			                        }
			                     }
			                    $(this).datagrid('beginEdit', param.index);
			                     for(var i=0; i<fields.length; i++){
			                         var col = $(this).datagrid('getColumnOption', fields[i]);
			                     col.editor = col.editor1;
			                 }
			             });
			         }
			        });

					var editIndex = undefined;
			        function endEditing(){
			            if (editIndex == undefined){return true}
			            if (datagrid.datagrid('validateRow', editIndex)){
			            	datagrid.datagrid('endEdit', editIndex);
			                editIndex = undefined;
			                return true;
			            } else {
			                return false;
			            }
			        }

					function onClickCell(index, field){
						if (endEditing()){
							datagrid.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
							editIndex = index;
						}
					}


					
			}
				
			});
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能编辑一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请选择要编辑的记录！', 'error');
		}
	}
	
</script>
</body>
</html>
