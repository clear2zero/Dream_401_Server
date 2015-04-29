<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<%@ page import="sto.common.util.RoleType" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
	<script src="${ctx}/js/My97DatePicker/WdatePicker.js"	type="text/javascript"></script>
</head>
<body class="easyui-layout" data-options="fit:true">

<div data-options="region:'north',border:false,title:'查询条件'"
		style="height: 60px; overflow: hidden;" align="left">
		<form id="searchForm" method="post">
			<input id="columns" name="columns" type="hidden"/>
			<input id="width" name="width" type="hidden"/>
			<table class="tableForm datagrid-toolbar"
				style="width：100%;height: 100%;">
				<tr>
					<shiro:hasAnyRoles name="<%=RoleType.UNUNIT_USERGROUP.getName()%>">
					<td style="font-size: 12px; width: 20%; align=left;">单位:
						<input id="divid" name="divid" />
					</td>
					</shiro:hasAnyRoles>
					<td style="font-size: 12px; width: 20%; align=left;">部门:
						<input id="deptid" name="deptid" />
					</td>
					<%-- <td style="font-size: 12px;">开始日期：</td>
					<td><input id="startDate" name="startDate" class="easyui-datebox" data-options="editable:false" value="${startDate}" ></td>
					<td style="font-size: 12px;">结束日期：</td>
					<td><input id="endDate" name="endDate" class="easyui-datebox" data-options="editable:false" value="${endDate}"></td>
					 --%>
					<td style="font-size: 12px;">开始日期：</td>
					<td><input id="startDate" name="startDate" style="width: 155px;" class="Wdate" onFocus="WdatePicker({onpicked:function(dp){selectDate();},isShowClear:false,readOnly:true,maxDate:'#F{$dp.$D(\'endDate\');}',minDate:'#F{$dp.$D(\'endDate\',{d:-35});}' })" value="${startDate}" ></td>
					<td style="font-size: 12px;">结束日期：</td>
					<td><input id="endDate" name="endDate" style="width: 155px;" class="Wdate" onFocus="WdatePicker({onpicked:function(dp){selectDate();},isShowClear:false,readOnly:true,minDate:'#F{$dp.$D(\'startDate\');}',maxDate:'#F{$dp.$D(\'startDate\',{d:35});}' })" value="${endDate}"></td>
					<td style="font-size: 12px;">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="_search();">查询</a>
					</td>
					<td style="font-size: 12px;">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="exportToExcel();">导出到excel</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false" style="overflow-y: hidden;">
		<div id="datagrid" style="overflow:auto;height:450px;"></div>
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
		}catch(e){
			e.message;
		}
		try{
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
		$.ajax({
		      type : "post",
		      url : "${pageContext.request.contextPath}/attend/statisticQueryColumns.action",
		      data : sy.serializeObject($('#searchForm')),
		      success : function(json){
				if (json.success) {
					$("#columns").val(JSON.stringify(json.columns));
					//$("#width").val(json.width);
					loadDatagrid(json.columns,parseInt(json.width));
				}else {
					sy.messagerShow({
						msg : json.msg,
						title : '提示'
					});
				}
		      }
		});
	});
	function selectDate(){
		$.ajax({
		      type : "post",
		      url : "${pageContext.request.contextPath}/attend/statisticQueryColumns.action",
		      data : sy.serializeObject($('#searchForm')),
		      success : function(json){
				if (json.success) {
					$("#columns").val(JSON.stringify(json.columns));
					//$("#width").val(json.width);
					loadDatagrid(json.columns,parseInt(json.width));
				}else {
					sy.messagerShow({
						msg : json.msg,
						title : '提示'
					});
				}
		      }
		});
	}
	function exportToExcel() {
		try{
			if($("#divid").combobox('getValue') == ''){
				sy.messagerShow({
					msg : "请选择要导出的单位",
					title : '提示'
				});
				return false;
			}
		}catch(e){
			e.message;
		}
		if($("#startDate").val() == ''){
			sy.messagerShow({
				msg : "请选择开始日期",
				title : '提示'
			});
			return false;
		}
		if($("#endDate").val() == ''){
			sy.messagerShow({
				msg : "请选择结束日期",
				title : '提示'
			});
			return false;
		}
		$('#searchForm').attr('action',"${pageContext.request.contextPath}/attend/exportToExcel.action");
		$('#searchForm').submit();
	}
	function loadDatagrid(columns,width){
		var datagrid = $('#datagrid').datagrid({
			//width:width,
			height:$(document.body).height()-100,
			pagination : true,
			//url:"${pageContext.request.contextPath}/attend/statisticQuery.action",
			pageSize : 10,
			pageList : [5,10,20,30,50],
			pageNumber:1,
			rownumbers : true,
			nowrap : true,
			border : false,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			checkOnSelect : true,
			singleSelect : true,
			columns : [columns],
			/* toolbar : [ {
				text : '导出到excel',
				iconCls : 'icon-search',
				handler : function() {
					exportToExcel();
				}
			}], */
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
		scrollShow($("#datagrid"),width);
	}
	function _search() {
		try{
			if($("#divid").combobox('getValue') == ''){
				sy.messagerShow({
					msg : "请选择要查询的单位",
					title : '提示'
				});
				return false;
			}
		}catch(e){
			e.message;
		}
		if($("#startDate").val() == ''){
			sy.messagerShow({
				msg : "请选择开始日期",
				title : '提示'
			});
			return false;
		}
		if($("#endDate").val() == ''){
			sy.messagerShow({
				msg : "请选择结束日期",
				title : '提示'
			});
			return false;
		}
		$("#datagrid").datagrid('options').url="${pageContext.request.contextPath}/attend/statisticQuery.action";
		$("#datagrid").datagrid('load', sy.serializeObject($('#searchForm')));
	}
	function scrollShow(datagrid,width) {
	    //datagrid.prev(".datagrid-view2").children(".datagrid-body").html("<div style='width:" + datagrid.prev(".datagrid-view2").find(".datagrid-header-row").width() + "px;border:solid 0px;height:1px;'></div>");
		datagrid.prev(".datagrid-view2").children(".datagrid-body").html("<div style='width:" + width + "px;border:solid 0px;height:1px;'></div>");
	}
</script>
</body>
</html>
