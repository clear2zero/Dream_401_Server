<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<%@page import="java.util.*"%>
<%
   String certcn = (String)session.getAttribute("certcn");
%>
<body>
	<div class="header">
	<div class="header1">
		<div class="item-list item-fixed-top">
			<div class="item-inner">
				<div class="brand"></div>
				<div class="item-collapse">
				<ul class="item-menu">
					<c:forEach items="${fnc:getMenuList('superid',0)}" var="menu" varStatus="idxStatus">
					 	<li><a href="javascript:void(0);" onclick='showmenu("${menu.id}");'>${menu.name}</a></li> 
					 </c:forEach>
				</ul>
			</div>
			<div class="item-menu loginInfo">
				<span>当前用户：${name}</span> <span><a
				href="javascript:logout();">安全退出</a></span>
				<br>
				<div id="warn">
					<c:if test="${iswarn==1}">
						<span><font id="blink" color="red">您的日志有新回复!</font></span>
					</c:if>
				</div>
			</div>
	</div>
	</div>
	</div>
		
	</div>
	<script type="text/javascript">
		var timer = "";
		try{
			if(blink){
				timer = setInterval(function(){blink.color=blink.color=='#0000ff'?'red':'blue'},100);
			}
		}catch(e){
			e.message;
		}
		
		$(function() {
			showmenu(1);
		});
		function showmenu(id) {
			var tabs = eoc.cms.main.showTabs;
			// 设置tab标签的显示or隐藏
			for(var i in tabs) {
				if (tabs[i].id != id) {
					$("#cms_content").tabs('getTab',tabs[i].text).panel('options').tab.hide();
				} else {
					$("#cms_content").tabs('getTab',tabs[i].text).panel('options').tab.show();
				}
			}
			 //保存之前选中的tab
			var selectedTab = $("#cms_content").tabs('getSelected').panel('options');
			eoc.cms.main.selectedTabs[selectedTab.id] = {'id':selectedTab.id,'text':selectedTab.title};
			// 设置tab标签的选中状态
			var selectTab = eoc.cms.main.selectedTabs;
			for (var m in selectTab) {
				if (selectTab[m].id == id) {
					$("#cms_content").tabs('select',selectTab[m].text);
					break;
				} else {
					$("#cms_content").tabs('select',0);
				}
			}
			<%-- $("#uul").tree({
				url:'<%=request.getContextPath()%>/module/listJsonForMenu.action?id='+id,
				onBeforeExpand:function(node,param){
					$('#uul').tree('options').url = '<%=request.getContextPath()%>/module/listJson.action?id='+node.id
				},
				onClick : function(node){
					eoc.cms.main.openMainTab(node,id);
				}
			}); --%>
			//alert(11);
			
			eoc.cms.main.lastMenuRootId = id;
		}
		function checkWarn(){
			$.ajax({
			   type: "POST",
			   url: '${ctx}/journal/getWarnStatus.action',
			   dataType : 'json', 
			   success: function(json){
				  if(json.iswarn == 1){
					  $("#warn").css('display','block');
					  clearInterval(timer);
						timer = setInterval(function(){blink.color=blink.color=='#0000ff'?'red':'blue'},100);
				  }else{
					  clearInterval(timer);
					  $("#warn").css('display','none');
				  }
			   }
			});
		}

		function logout(){
			window.location.href="${ctx}/account/logout.action";
			var client = new HebcaClient();
			var obj = client._GetClientCtrl();
			obj.ReloadDevice();
			obj.Reset();//重置选择状态
			var count = obj.GetCertCount();
			for(var i = 0; i < count; i++) { 
		        var cert = obj.GetCert(i);
		        if(cert.IsSignCert){
		        	try{
		        		var group1=cert.GetSubjectItem('CN');
						var group2=cert.GetSubjectItem('G');
						var cnLength=group1.replace(/[^\x00-\xff]/g,"**").length;
						var gLength=group2.replace(/[^\x00-\xff]/g,"**").length;			    
						if(cnLength>gLength){
							certcn = group1;
						}else{
							certcn = group2;
						}
						var curcertcn = "<%=certcn%>";
						if(curcertcn == certcn && cert.Logined){
							cert.Logout();
							break;
						}
		        	}catch(e){
		        		e.message;
		        	}
		        	
		        }
		    }
		}
	</script>
</body>
</html>