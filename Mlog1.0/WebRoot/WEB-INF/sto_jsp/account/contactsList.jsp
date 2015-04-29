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
		datagrid = $('#datagrid').datagrid({
			url : '${pageContext.request.contextPath}/unit/listJson.action',
			iconCls: 'icon-ok',
            rownumbers: true,
            animate: true,
            collapsible: true,
            fitColumns: true,
            method: 'get',
            idField: 'id',
            treeField: 'name',
            pagination: true,
            pageSize: 2,
            pageList: [2,5,10],
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
				title : '硬证书',
				field : 'hcertcn',
				width : 120,
				align:'center'
			},{
				title : '软证书',
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
				field : 'isenable',
				width : 120,
				align:'center',
				formatter : function(value) {
					if (value==1) {
						return "正常";
					}
					return "停用";
				}
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
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					remove();
				}
			}, '-', {
				text : '绑定硬证书',
				iconCls : 'icon-edit',
				handler : function() {
					bindCert(0);
				}
			}, '-', {
				text : '绑定软证书',
				iconCls : 'icon-edit',
				handler : function() {
					bindCert(1);
				}
			}, '-', {
				text : '解绑硬证书',
				iconCls : 'icon-edit',
				handler : function() {
					unbindCert(0);
				}
			}, '-', {
				text : '解绑软证书',
				iconCls : 'icon-edit',
				handler : function() {
					unbindCert(1);
				}
			}, '-', {
				text : '修改密码',
				iconCls : 'icon-edit',
				handler : function() {
					alert(1);
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
	});

	
	
</script>
</body>
</html>
