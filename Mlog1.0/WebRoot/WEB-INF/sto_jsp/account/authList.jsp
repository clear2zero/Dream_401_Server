<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/_static/js/ztree/zTreeStyle/zTreeStyle.css">
<style>
div.content_wrap {width: 600px;height:380px;}
div.content_wrap div.left{float: left;width: 250px;}
div.content_wrap div.right{float: right;width: 340px;}
div.zTreeDemoBackground {width:400px;text-align:left;border:1px solid #8080ff;overflow-y:auto}
.search{
    background: transparent url('${ctx}/_static/images/auth_query.png') no-repeat 0 -27px;
    border: 0 none;
    height: 28px;
    width: 142px;
    padding: 0 12px 0 40px;
    outline: none;
    font-size: 12px;
    line-height: 28px;
}
</style>
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<script type="text/javascript" src="${ctx}/_static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/_static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/_static/js/ztree/jquery.ztree.exhide-3.5.min.js"></script>
<script type="text/javascript" charset="utf-8">
$(function(){
	$("#l_div").css('height',$(document.body).height()-150);
	$("#r_div").css('height',$(document.body).height()-150);
	$('#divid').combobox({
	    url : '${ctx}/unit/listJsonNoPage.action',    
	    valueField : 'divid',    
	    textField : 'divname',
	    editable : false,
	    onSelect : function(r){
	    	initTree(r.divid);
	    }
	});
	
	var setting1;
	var alllNodes = [];
	function initTree(divid){
		var left = $("#left");
		//生成菜单tree
		setting1 = {
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			view: {
				showIcon: true,
				fontCss: getFontCss
			},
			callback: {
				beforeClick : zTreeBeforeClick,
				onClick : zTreeOnClick
				
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};
		
		var right = $("#right");
		//生成菜单tree
		var setting2 = {
			check: {
				enable: true
			},
			view: {
				showIcon: true
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};
		$.ajax({
		   type: "POST",
		   url: '${pageContext.request.contextPath}/auth/listJson.action',
		   data : {divid:divid},
		   dataType : 'json', 
		   success: function(data){
			   alllNodes = data;
			   $.fn.zTree.init(left, setting1, data);
			   $.fn.zTree.init(right, setting2, data);
		   }
		});
	}
	
	initTree(null);
	var nodeList = [];
	var nodeList1 = [];
	/* $('#username').bind("blur",function(event) {
		search_ztree('left','username');
	}); */
	$('#username').bind("keyup",function(event) {/* 增加回车提交功能 */
		if (event.keyCode == 13) {
			search_ztree('left','username');
		}
	});
	/**
     * 展开树
     * @param treeId  
     */
    function expand_ztree(treeId){
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        treeObj.expandAll(true);
    }
     
    /**
     * 收起树：只展开根节点下的一级节点
     * @param treeId
     */
    function close_ztree(treeId){
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        var nodes = treeObj.transformToArray(treeObj.getNodes());
        var nodeLength = nodes.length;
        for (var i = 0; i < nodeLength; i++) {
            if (nodes[i].id == '0') {
                //根节点：展开
                treeObj.expandNode(nodes[i], true, true, false);
            } else {
                //非根节点：收起
                treeObj.expandNode(nodes[i], false, true, false);
            }
        }
    }
     
    /**
     * 搜索树，高亮显示并展示【模糊匹配搜索条件的节点s】
     * @param treeId
     * @param searchConditionId 文本框的id
     */
    function search_ztree(treeId, searchConditionId){
        searchByFlag_ztree(treeId, searchConditionId, "");
    }
     
    /**
     * 搜索树，高亮显示并展示【模糊匹配搜索条件的节点s】
     * @param treeId
     * @param searchConditionId     搜索条件Id
     * @param flag                  需要高亮显示的节点标识
     */
    function searchByFlag_ztree(treeId, searchConditionId, flag){
        //<1>.搜索条件
        var searchCondition = $('#' + searchConditionId).val();
        //<2>.得到模糊匹配搜索条件的节点数组集合
        var highlightNodes = new Array();
        if (searchCondition != "") {
            var treeObj = $.fn.zTree.getZTreeObj(treeId);
            highlightNodes = treeObj.getNodesByParamFuzzy("name", searchCondition, null);
        }
        //<3>.高亮显示并展示【指定节点s】
        highlightAndExpand_ztree(treeId, highlightNodes, flag);
    }
     
    /**
     * 高亮显示并展示【指定节点s】
     * @param treeId
     * @param highlightNodes 需要高亮显示的节点数组
     * @param flag           需要高亮显示的节点标识
     */
    function highlightAndExpand_ztree(treeId, highlightNodes, flag){
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        //<1>. 先把全部节点更新为普通样式
        var treeNodes = treeObj.transformToArray(treeObj.getNodes());
        for (var i = 0; i < treeNodes.length; i++) {
            treeNodes[i].highlight = false;
            treeObj.updateNode(treeNodes[i]);
        }
        //<2>.收起树, 只展开根节点下的一级节点
        close_ztree(treeId);
        //<3>.把指定节点的样式更新为高亮显示，并展开
        if (highlightNodes != null) {
            for (var i = 0; i < highlightNodes.length; i++) {
                if (flag != null && flag != "") {
                    if (highlightNodes[i].flag == flag) {
                        //高亮显示节点，并展开
                        highlightNodes[i].highlight = true;
                        treeObj.updateNode(highlightNodes[i]);
                        //高亮显示节点的父节点的父节点....直到根节点，并展示
                        var parentNode = highlightNodes[i].getParentNode();
                        var parentNodes = getParentNodes_ztree(treeId, parentNode);
                        treeObj.expandNode(parentNodes, true, false, true);
                        treeObj.expandNode(parentNode, true, false, true);
                    }
                } else {
                    //高亮显示节点，并展开
                    highlightNodes[i].highlight = true;
                    treeObj.updateNode(highlightNodes[i]);
                    //高亮显示节点的父节点的父节点....直到根节点，并展示
                    var parentNode = highlightNodes[i].getParentNode();
                    var parentNodes = getParentNodes_ztree(treeId, parentNode);
                    treeObj.expandNode(parentNodes, true, false, true);
                    treeObj.expandNode(parentNode, true, false, true);
                }
            }
        }
    }
     
    /**
     * 递归得到指定节点的父节点的父节点....直到根节点
     */
    function getParentNodes_ztree(treeId, node){
        if (node != null) {
            var treeObj = $.fn.zTree.getZTreeObj(treeId);
            var parentNode = node.getParentNode();
            return getParentNodes_ztree(treeId, parentNode);
        } else {
            return node;
        }
    }
     
    /**
     * 设置树节点字体样式
     */
    function getFontCss(treeId, treeNode) {
        if (treeNode.id == 0) {
            //根节点
            return {color:"#333", "font-weight":"bold"};
        } else if (treeNode.isParent == false){
            //叶子节点
            return treeNode.highlight ? {color:"#ff0000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
        } else {
            //父节点
            return treeNode.highlight ? {color:"#ff0000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
        }
    }
});
function selectType(){
	var checkedItems = new Array(); 
	$("input[name='type']:checked").each(function() {checkedItems.push($(this).val());});
	var ltree = $.fn.zTree.getZTreeObj("left");
	var rtree = $.fn.zTree.getZTreeObj("right");
	var node = ltree.getSelectedNodes();
	if(node && node.length>0){
		if(checkedItems.length==1){
			var id = node[0].id;
			var los = id.indexOf("_");
			var userid = id.substring(los+1,id.length);
			$.ajax({
			   type: "POST",
			   url: '${pageContext.request.contextPath}/user/getDataAuthTreeids.action',
			   data:{type:checkedItems[0],userid:userid},
			   dataType : 'json', 
			   success: function(data){
				   rtree.checkAllNodes(false);
				   for(var i=0;i<data.length;i++){
					   var node = rtree.getNodeByParam("id", data[i], null);
					   //treeObj.selectNode(node,true);
					   rtree.checkNode(node, true, true);
				   }
			   }
			});
		}else {
			rtree.checkAllNodes(false);
		}
	}
	
	
}

function zTreeBeforeClick(treeId, treeNode, clickFlag) {
	var checkedItems = new Array(); 
	$("input[name='type']:checked").each(function() {checkedItems.push($(this).val());});
	
	if(checkedItems.length<=0){
		sy.messagerShow({
			msg : '请选择权限类型',
			title : '提示'
		});
		return false;
	}
	
	if(treeNode.children){
		return false;
	}
};

function zTreeOnClick(event, treeId, treeNode) {
	var checkedItems = new Array(); 
	$("input[name='type']:checked").each(function() {checkedItems.push($(this).val());});
	if(checkedItems.length>1){
		return false;
	}
	if(checkedItems.length<=0){
		sy.messagerShow({
			msg : '请选择权限类型',
			title : '提示'
		});
		return false;
	}
	var treeObj = $.fn.zTree.getZTreeObj("right");
	if(checkedItems.length==1){
		var id = treeNode.id;
		var los = id.indexOf("_");
		var userid = id.substring(los+1,id.length);
		$.ajax({
		   type: "POST",
		   url: '${pageContext.request.contextPath}/user/getDataAuthTreeids.action',
		   data:{type:checkedItems[0],userid:userid},
		   dataType : 'json', 
		   success: function(data){
			   treeObj.checkAllNodes(false);
			   for(var i=0;i<data.length;i++){
				   var node = treeObj.getNodeByParam("id", data[i], null);
				   //treeObj.selectNode(node,true);
				   treeObj.checkNode(node, true, true);
			   }
		   }
		});
	}else {
		treeObj.checkAllNodes(false);
	}
    //alert(treeNode.id + ", " + treeNode.deptname);
};
function save(){
	var ltree = $.fn.zTree.getZTreeObj("left");
	var rtree = $.fn.zTree.getZTreeObj("right");
	var checkedItems = new Array();
	$("input[name='type']:checked").each(function() {
		checkedItems.push($(this).val());
	});
	if(checkedItems.length<=0){
		sy.messagerShow({
			msg : '请选择权限类型',
			title : '提示'
		});
		return false;
	}
	var node = ltree.getSelectedNodes();
	if(!node){
		sy.messagerShow({
			msg : '请选择设置用户',
			title : '提示'
		});
		return false;
	}
	var nodes = rtree.getCheckedNodes(true);
	if(!nodes){
		sy.messagerShow({
			msg : '请选择设置可见用户',
			title : '提示'
		});
		return false;
	}
	//获取设置用户userid
	var lid = node[0].id;
	var los = lid.indexOf("_");
	var managerid = lid.substring(los+1,lid.length);
	//获取可见用户userids
	var userids="";
	for(var i=0;i<nodes.length;i++){
		//alert(JSON.stringify(nodes[i]));
		//alert(nodes[i].id);
		var rid = String(nodes[i].id);
		
		var los = rid.indexOf("_");
		if(los != -1){
			userids += rid.substring(los+1,rid.length)+",";
		} 
		
	}
	$.ajax({
		   type: "POST",
		   url: '${pageContext.request.contextPath}/user/saveDataAuth.action',
		   data:{managerid:managerid,types:checkedItems.join(","),userids:userids},
		   dataType : 'json', 
		   success: function(json){
			   	if (json.success) {
				}
				sy.messagerShow({
					msg : json.msg,
					title : '提示'
				});
		   }
		});
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
					<td style="font-size: 12px; width: 15%; align=left;">单位:
					<input id="divid" name="divid" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	</shiro:hasRole>
	<div data-options="region:'center',border:false" style="overflow: hidden">
		<form>
		<table>			
			<tr><td>选择设置权限类型：
				<input type="checkbox" name="type" id="attend"  value=1 onclick='selectType()'>考勤权限
				<input type="checkbox" name="type" id="journal" value=2 onclick='selectType()'>日志权限
				<input type="checkbox" name="type" id="position" value=3 onclick='selectType()'>定位权限	
			<hr>
			</td>
			<td align="right"><input  type="text" style="display:none" /> 
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">保存</a>
			</td>
			</tr>
			<tr>
				<td><div><img src="${ctx}/_static/images/auth_user1.png" style="margin-bottom:-8px;width:20px;height:20px;">设置用户
				<span style="padding-left:120px;"><input type="text" class="search" id="username" name="username" autocomplete="off"/></span></div></td>
				<td><img src="${ctx}/_static/images/auth_user2.png" style="margin-bottom:-8px;width:20px;height:20px;">设置可见用户
				</td>
			</tr>
			<tr>
				<td>
					<div id="l_div" class="zTreeDemoBackground left">
						<ul id='left' name='left' class="ztree"></ul>
					</div>
				</td>
				<td>
					<div id="r_div" class="zTreeDemoBackground left">
						<ul id='right' name='right' class="ztree"></ul>
					</div>
				</td>
			</tr>
		</table>
		</form>
	</div>
</body>
</html>