<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<style>
/* #cms_content{//ie8下回造成鼠标事件无法点击
	filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${pageContext.request.contextPath}/_static/images/welcome.jpg',sizingMethod='scale'); 
 	background-repeat: no-repeat; 
 	background-positon: 100%, 100%;
 	margin:0;
} */
#cms_content{
	background-image:url('${pageContext.request.contextPath}/_static/images/welcome_bg.jpg');
 	background-repeat: no-repeat;
 	background-positon: 50%, 50%;
 	margin:0;
 	z-index:1000;
}
#index{
	background:none;
	margin-left:100px;
	margin-top:20px;
	height:200px;
	background-repeat:no-repeat;
	opacity: 0.8;                    /* Firefox, Safari(WebKit), Opera */  
	-ms-filter: "alpha(opacity=80)"; /* IE 8 */  
	filter: alpha(opacity=80);       /* IE 4-7 */  
	zoom: 1;	
}
</style>
<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'north',border:false,href:'${pageContext.request.contextPath}/layout/top_ztree.jsp'" style="height:65px;">north region</div>
<div data-options="region:'west',split:true,title:'菜单信息',href:'${pageContext.request.contextPath}/layout/left_ztree1.jsp'" id="cms_left" style="width:200px;">west content</div>
<div data-options="region:'center'" id="cms_content">
    <div title="欢迎" id="index">
    	<img id="welcome" style="padding-left:100px;padding-top:50px;" src="${pageContext.request.contextPath}/_static/images/welcome.png"/>
    </div>
</div>
<div id="tabsMenu" style="width: 120px;display:none;">
	<div type="refresh">刷新</div>
	<div class="menu-sep"></div>
	<div type="close">关闭</div>
	<div type="closeOther">关闭其他</div>
	<div type="closeAll">关闭所有</div>
</div>
<%@ include file="footer.jsp"%>
<script type="text/javascript">
eoc.cms.main();
$("#welcome").css('width',967/(621+967)*(window.screen.height+300));
$("#welcome").css('height',621/(621+967)*(window.screen.height+300));
/* $.post("${pageContext.request.contextPath}/unit/isneedinit.action",{},
	function(json){
		if(json.success == true){
			var p = sy.dialog({
				id : 'init',
				title : '新增',
				href : '${pageContext.request.contextPath}/unit/fastinit.action',
				width : 650,
				height : 520
			}); 
		}
}); */

</script>
</body>
</html>