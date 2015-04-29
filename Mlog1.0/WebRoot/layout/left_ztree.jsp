<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<body>
	<div class="easyui-accordion" data-options="fit:true" style="overflow:auto">
		<ul id="uul" class="ztree"></ul>
	</div>
	
<script type="text/javascript">
var ztree = $("#uul");
//生成菜单tree
var setting = {
	edit: {
		enable: true,
		showRemoveBtn: false,
		showRenameBtn: false
	},
	view: {
		showIcon: true
	},
	data: {
		key: {
			children: "children",
			name:"name"
		}
	},
	callback: {
		beforeExpand : zTreeBeforeExpand,
		onClick : zTreeOnClick,
		beforeClick : zTreeBeforeClick
	}
};
function zTreeBeforeExpand(treeId, treeNode){
}
function zTreeBeforeClick(treeId, treeNode, clickFlag) {
	var treeObj =  $.fn.zTree.getZTreeObj("uul");
	var nodes = treeObj.getSelectedNodes();
	if (nodes.length>0) {
		var iconpath = nodes[0].icon;
		if(iconpath){
			var index = iconpath.lastIndexOf("/");
			if(-1 != index){
				iconpath = iconpath.substring(0,index+1)+iconpath.substring(index+3,iconpath.length);
			}
			nodes[0].icon = iconpath;
		}else {
			nodes[0].icon = "../_static/images/icons/log.png";
		}
		treeObj.updateNode(nodes[0]);
	}
    return true;
};
function zTreeOnClick(event, treeId, treeNode){
	var treeObj =  $.fn.zTree.getZTreeObj("uul");
	var iconpath = treeNode.icon;
	if(iconpath){
		var index = iconpath.lastIndexOf("/");
		if(-1 != index){
			iconpath = iconpath.substring(0,index+1)+'s_'+iconpath.substring(index+1,iconpath.length);
		}
		treeNode.icon = iconpath;
	}else {
		treeNode.icon = "../_static/images/icons/s_log.png";
	}
	treeObj.updateNode(treeNode);
	if(!treeNode.isParent){
		eoc.cms.main.openMainTabZtree(treeNode,1);
	}
}
$.ajax({
   type: "POST",
   url: '<%=request.getContextPath()%>/module/leftTree.action',
   dataType : 'json', 
   success: function(msg){
	   $.fn.zTree.init(ztree, setting, msg);
   }
});
</script>
</body>
</html>	