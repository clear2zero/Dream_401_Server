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
		<div id="datagrid_buttonlist"></div>
	</div>
	<script type="text/javascript" >
	var datagrid;
	$(function() {
		datagrid = $('#datagrid_buttonlist').datagrid({
			url : '${pageContext.request.contextPath}/button/getButtonsContainCheckstatus.action?mid=${mid}',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [5,10,20,30,50],
			pageNumber:1,
			//width:750,
			height:400,
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
				hidden:true
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
				title : '分配',
				field : 'checked',
				width : 150,
				align:'center',
				checkbox:true
			} ] ],
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onLoadSuccess:function(row){//当表格成功加载时执行               
                var rowData = row.rows;
                $.each(rowData,function(idx,val){//遍历JSON
                      if(val.checked==1){
                        $("#datagrid_buttonlist").datagrid("selectRow", idx);//如果数据行为已选中则选中改行
                        $("#datagrid_buttonlist").datagrid("checkRow", idx);
                      }
                });              
            }


		});

	});
</script>
</body>
</html>
