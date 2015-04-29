<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
		<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false,title:'过滤条件'"
		style="height: 60px; overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar"
				style="width: 100%; height: 100%;">
				<tr>
					<td style="font-size: 12px; width: 15%; align=left;">操作员CN:
					<input name="certcn" />
					<a href="javascript:_search();" class="easyui-linkbutton">查询</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<div id="datagrid_operateloglist" style="overflow: auto;"></div>
	</div>
	<script type="text/javascript" >
	var datagrid;
	$(function() {
		datagrid = $('#datagrid_operateloglist').datagrid({
			url : '${pageContext.request.contextPath}/operatelog/listJson.action',
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
				title : '操作员',
				field : 'certcn',
				width : 50,
				align:'center'
			}, {
				title : 'ip',
				field : 'ip',
				width : 50,
				align:'center'
			}, {
				title : '操作时间',
				field : 'createtime',
				width : 50,
				align:'center'
			}, {
				title : '执行方法',
				field : 'moduleid',
				width : 70,
				align:'center'
			}, {
				title : '操作描述',
				field : 'description',
				width : 200,
				align:'center'
			}] ],
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
	
	function _search() {
		datagrid.datagrid('load', sy.serializeObject($('#searchForm')));
	}
</script>
</body>

</html>
