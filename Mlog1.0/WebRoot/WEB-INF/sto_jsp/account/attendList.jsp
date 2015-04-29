<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">

<div data-options="region:'north',border:false,title:'查询条件'"
		style="height: 60px; overflow: hidden;" align="left">
		<form id="searchForm">
			<table class="tableForm datagrid-toolbar"
				style="width：100%;height: 100%;">
				<tr>
					<td style="font-size: 12px;">姓名:</td>
					<td><input id="name" name="name" /></td>
					<td style="font-size: 12px;">开始日期：</td>
					<td><input id="startDate" name="startDate" class="easyui-datebox" data-options="editable:false" value="${startDate}" ></td>
					<td style="font-size: 12px;">结束日期：</td>
					<td><input id="endDate" name="endDate" class="easyui-datebox" data-options="editable:false" value="${endDate}"></td>
					<td style="font-size: 12px;">
						<input name="text" style="display:none">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="_search();">查询</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false" style="overflow: hidden;">
		<table id="datagrid" style="overflow: auto;height:450px;"></table>
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
			url : '${pageContext.request.contextPath}/attend/listJson.action',
			height:$(document.body).height()-100,
			queryParams: {
				name: $('#name').val(),    
			    startDate: $('#startDate').datebox('getValue'),
			    endDate:$('#endDate').datebox('getValue')
			},
			pagination : true,
			pageSize : 10,
			pageList : [5,10,20,30,50],
			pageNumber:1,
			nowrap : true,
			border : false,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			checkOnSelect : true,
			singleSelect : true,
			columns : [ [ {
				title : '编号',
				field : 'id',
				hidden : true
			},{
				field : 'userid',
				hidden : true
			}, {
				title : '序号',
				field : 'orderid',
				width : 30,
				align:'center',
				formatter:function(value, row, index){
					return index+1;
				}
			}, {
				title : '用户名',
				field : 'username',
				width : 120,
				align:'center'
			}, {
				title : '姓名',
				field : 'name',
				width : 120,
				align:'center'
			}, {
				title : '考勤日期',
				field : 'date',
				width : 120,
				align:'center'
			}, {
				title : '考勤状态',
				field : 'status',
				width : 120,
				align:'center'
			}] ],
			toolbar : [ {
				text : '详细打卡记录',
				iconCls : 'icon-search',
				handler : function() {
					attendcardmx();
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
	function attendcardmx() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length == 1) {
			var p = sy.dialog({
				title : '打卡记录',
				href : '${pageContext.request.contextPath}/attend/attendcardmxList.action',
				width : 870,
				height : 400,
				buttons : [],
				onLoad : function() {
					var f = p.find('form');
					f.form('load', {
						date:rows[0].date,
						userid:rows[0].userid
					});
					var mxdatagrid = f.find("table[id='mxdatagrid']").datagrid({
						url : '${pageContext.request.contextPath}/attend/getAttendCardListByUserid.action?date='+rows[0].date+"&userid="+ rows[0].userid,
						/* queryParams: {
							date: rows[0].date,    
						    userid: rows[0].userid
						}, */
						height:350,
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
						columns : [ [ {
							title : 'id',
							field : 'id',
							width : 150,
							hidden : true
						},{
							title : '序号',
							field : 'orderid',
							width : 30,
							align:'center',
							formatter:function(value, row, index){
								return index+1;
							}
						}, {
							title : '打卡类型',
							field : 'type',
							width : 50,
							align:'center',
							formatter:function(v){
								if(v==1){
									return "上班";
								}
								if(v==2){
									return "下班";
								}
								return "打卡异常";
							}
							
						}, {
							title : '打卡时间',
							field : 'time',
							width : 80,
							align:'center'
						}, {
							title : '经度',
							field : 'longitude',
							width : 100,
							align:'center'
						}, {
							title : '纬度',
							field : 'latitude',
							width : 100,
							align:'center'
						}, {
							title : '打卡地址',
							field : 'address',
							width : 200,
							align:'center'
						}] ]
					});
					
			}
				
			});
		} else if (rows.length > 1) {
			sy.messagerAlert('提示', '同一时间只能查看一条记录！', 'error');
		} else {
			sy.messagerAlert('提示', '请选择要查看的记录！', 'error');
		}
	}
	
	function _search() {
		datagrid.datagrid('load', sy.serializeObject($('#searchForm')));
	}
	
</script>
</body>
</html>
