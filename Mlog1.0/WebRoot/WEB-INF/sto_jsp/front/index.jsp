<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@ page import="org.apache.shiro.*,sto.common.exception.*,org.apache.shiro.authc.AuthenticationException,java.lang.*"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>快办365-外勤管理-移动办公</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="ie-stand">
<meta name="keywords" content="快办,快办365,外勤,外勤管理,考勤,考勤管理,移动办公,移动办公系统,个人">
<meta name="description" content="『快办365』是一款全方位提升企事业信息化管理水平的优秀软件，用户使用日志管理、考勤管理、外勤人员位置以及轨迹查看，进行动态交流，提高了管理者和员工之间的沟通效率，并结合数字证书密码保证数据传输的安全,是中小企业科学管理外勤人员的真正福音。">
<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
<link href="${ctx}/_static/css/front/main.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/_static/css/focus/lrtk.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="${ctx}/_static/images/front/favicon.ico" type="image/x-icon" />
<%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<script type="text/javascript" src="${ctx}/_static/js/focus/lrtk.js"></script>
<script type="text/javascript" src="${ctx}/js/login1.js?version=20130427&r=<%=Math.random()%>"></script>
<script type="text/javascript">
var reqrandom = "";      //请求随即数,防止js缓冲和重复提交
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?d2dac1c5fd3154285a888e203ddfa81d";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
</head>
<body>
<div id="content" style="display:none">快办，快办365，外勤，外勤管理，考勤，考勤管理，移动办公，移动办公系统，个人移动办公系统，腾翔，河北腾翔，腾翔快办，腾翔快办365。『快办365』是一款全方位提升企事业信息化管理水平的优秀软件，用户使用日志管理、考勤管理、外勤人员位置以及轨迹查看，进行动态交流，提高了管理者和员工之间的沟通效率，并结合数字证书密码保证数据传输的安全,是中小企业科学管理外勤人员的真正福音。主要功能：
1、手机打卡
通过手机进行考勤打卡，满足了非统一办公地点（分公司或办事处）和弹性工作制特点的企业要求。
2、移动日志
员工使用手机填写日志，也可拍照上传图片，详细记录工作细节，管理人员使用手机以微博形式浏览查看部门所有员工日志，并进行批复和双向沟通。
3、消息共享
员工对于个人思想火花、心得体会等以文字形式记录后分享给公司员工，参与分享的员工对观点展开深入广泛的讨论和探索。
4、消息提醒
用户收到分享消息或待回复日志后，系统会以图标和铃声等多媒体形式及时通知用户，体现更加人性化的服务理念。
5、通讯录
员工可以快速查找其他同事的电话，并直接和对方进行通话。
6、位置与轨迹
在工作期间按照固定时间周期上传员工位置信息，科学描绘运行轨迹并显示在数字地图上，方便企业管理人员随时了解外勤人员的活动路线。</div>
<div id="index_nav">
<form action="${ctx }/account/login.action" method="post" id="loginForm" style="margin: 0px;" >
	<input type="hidden" name="useCertLogin" />  
	<input type="hidden" name="authMode"  />   
	<input type="hidden" name="caMode"  value="hebca"/>  
	<input type="hidden" name="signCert" /> 
	<input type="hidden" name="cryptCert" /> 
	<input type="hidden" name="signature" />   
	<input type="hidden" name="seal" /> 
	<input type="hidden" name="keyType" value="usbkey证书"/> 
	<input type="hidden" name="username" />
	<input type="hidden" name="loginRandom" value="<c:out value="${requestScope.loginRandom}" />" /> 
</form>
<div id="indextopline"></div>
<div id="indexmainbgtop">
	<div id="indextopbanner">
		<div id="indextopbannerlogo"></div>
		<div id="index_topbanner_nav">
			<ul>
				<li><a href="${ctx}/front/main.action">首页</a></li>
				<li><a href="${ctx}/includes/tgtuandui.html" target="_blank">快办介绍</a></li>
				<li><a href="http://www.tecshield.com/" target="_blank">腾翔官网</a></li>
				<li><a href="http://shop112116066.taobao.com/?v=1" target="_blank">商城</a></li>
				<li><a href="${ctx}/front/help.action" target="_blank">快速指南</a></li>
			</ul>
		</div>
		<div id="index_topbanner_login"><div style="padding-top:5px;"><a href="javascript:validate('hebca');"><img src="${ctx}/_static/images/front/indexlogo_07.png" width="90" height="57" /></a>
		<a href="${ctx}/unit/frontRegister.action"><img src="${ctx}/_static/images/front/indexlogo_08.png" width="90" height="57" /></a></div><div style="padding-left:15px;"><img src="${ctx}/_static/images/front/phone.png" width="18" height="20" /><span style="font-style:italic;">400-789-9696</div></div>
	</div><!-----indextopbanner------>
	<div class="clearfloat"></div>
</div><!-----indexmainbgtop------>
</div>
<div id="index_main_pf1">
	<div id="index_main_pf1_1">
	<img id="bg_img" src="${ctx}/_static/images/front/indexbg1_12.jpg" width='100%' height='749'></img>
		<div id="box">
			<span style="display:block;">
				<ul>
		        	<li><img class="box_img" style="width:900px;" src="${ctx}/_static/images/focus/1.png" alt="快办365" title="快办365" /></li>
		        	<li><img class="box_img" style="width:900px;" src="${ctx}/_static/images/focus/2.png" alt="快办365" title="快办365" /></li>
		        	<li><img class="box_img" style="width:900px;" src="${ctx}/_static/images/focus/3.png" alt="快办365" title="快办365" /></li>
		    	</ul>
	    	</span>
	    	<span id="index_main_bit2">
				<img src="${ctx}/_static/images/front/indexbit_16.png" usemap="#map" width="136" height="53" />
				<map name="map" id="map">
        			<area shape="rect" coords="0,0,136,53" href="http://www.kuaiban365.com/download/HebcaMLog.apk"/>
    			</map>
			</span>
			<span id="index_main_bit1"><img src="${ctx}/_static/images/front/indexbit_13.png" width="130" height="131" /></span>
		</div>
			
	</div><!-----index_main_pf1------>
	<div id="index_main_bottom"><div style="float:left;width:135px;"><img src="${ctx}/_static/images/front/index_bottom.png" width="121" height="45"/></div><div style="padding-top:15px;float:left;">Copyright©2015-2017	河北腾翔软件科技有限公司	 冀ICP备13016849号-4 	保留所有权利	热线电话：400-7899696	QQ：1710789396</div>
	</div>
</div>
</body>
</html>
