<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@page import="java.util.*,org.hebca.multidomain.filter.*,org.hebca.multidomain.config.*,org.claros.groupware.preferences.controllers.*,org.claros.groupware.webmail.utility.*,java.util.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
ResourceBundle res = ResourceBundle.getBundle("claros");
List supAdminList = RegisterAdminController.getUserCertList(Constants.SUP_ADMIN);//系统管理员
List audAdminList = RegisterAdminController.getUserCertList(Constants.AUDIT_ADMIN);//系统审计员
%>
<head>

<base href="javascript:getBaseURL()">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/client.jsp" %>
<script type="text/javascript" src="js/base.js"></script>
<script language="javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/blockUI.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=res.getString("claros_name")%></title>
<link href="css/login/css/loginstyle.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#downclient_center{
border:2px solid #fff; 
filter:alpha(opacity=80);
position:absolute;
left:300px;
padding-top:60px;
top:200px;
height:200px;
margin:50px auto; 
width:400px;
display:none;
background-color:#ddd;
} 
#downclient_loading{ 
width:318px; 
height:31px; 
background:url(images/bak.png) no-repeat; 
text-align:center;
margin:0 auto;
} 
#downclient_loading div{ 
width:0px; 
height:30px; 
background:url(images/pro.png) no-repeat; 
color:#fff; 
text-align:center; 
font-family:Tahoma; 
font-size:18px; 
line-height:30px; 
} 
#downclient_message{ 
width:210px; 
height:35px; 
font-family:Tahoma; 
font-size:16px; 
border:0px solid #187CBE; 
display:none; 
line-height:35px; 
text-align:center; 
margin:0 auto 10px; 

}
</style>
<script type="text/javascript">
var reqrandom = "";      //请求随即数,防止js缓冲和重复提交
var sealMode='<c:out value="${sealMode}" />';
var displayWord ='<c:out value="${displayWord}" />';
window.onbeforeunload=function(){
	//edit by xj 2012-12-18 讨论后不提示
	//if (document.body.offsetWidth-50 < event.clientX && event.clientY<0 && document.body.offsetWidth> event.clientX)
	//alert('退出<%=res.getString("claros_name")%>后，请拔Key');
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
} 
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
  var u;
  var install=false;
jQuery(document).ready(function(){
	try{
	    //setTimeout("Test('hebca')",300);
	}catch(e){
	  alert(e.message);
	}
	if(!parent.frames["mainFrame"]){
		//var tmphref = location+"";
		//location.href=tmphref.substring(0,tmphref.indexOf("profiling"));		
		var lc = location+"";
		var hou = lc.substring(8,lc.length);
		var zhong = hou.indexOf('/');
		var qian = lc.substring(0,(zhong+8));
		if(lc.indexOf("claros")!=-1){
			qian = qian+"/claros";
		}
		//location.href=qian;
	}
});
function changepin(){
    var obj;
	try
	{
	     obj = new ActiveXObject("HebcaP11X.CertMgr");
	     obj.Licence = ClientProvider.P11XLicence;
	     var d = obj.GetDevice(0); 
	     d.UIChangePin();
	}
	catch(e)
	{
	    alert(/*"错误代码：" + e.number + "，错误描述：" +*/ e.message);
	} 
}
//检验是否插key
function checkOutKey(){
	try{
		var cp=new ClientProvider();
		var client=cp.GetClient();
		if(!client.ImportCert())
		{
			alert("无法导入证书到浏览器");
			return ;
		}
		
		//if sealMode=key , get seal from key
		/*if(sealMode=='key')
		{
			document.all('seal').value = client.GetSeal();
		}
		*/
		//导入国密算法参数
		client.ImportKeyEncryptParam();
		var url = "preferences/registerStart.do";
		<%String visitDomain = request.getServerName();
		if (DomainConfig.isSupport()&&!visitDomain.endsWith(DomainConfig.getPlatformDomain())&&DomainConfig.isExist(visitDomain)) {
		out.print("url = 'preferences/registerStartUnit.do'");	
		}
		%>

		window.location.href=url;
	}catch (e) {
		if(e.message=='对象不支持“UILogin”属性或方法'){
			alert("登陆失败:请检查是否安装了数字证书助手和最新的邮件客户端");
		}else{
			alert(e.message);
		}
	}
	
}
function changeDisplay(el){
  $("#display_zone").css("display","none");
  $("#display_dianxin").css("display","none");
  $("#display_liantong").css("display","none");
  
  el.css("display","block");
}

function GetUrl(url)
{		
	var pageStr;
	var exp = /http.*\/claros/ig
	var match = exp.exec(url);
	if(null != match){	
		return match[0];
	}else
	{
		exp = /http:\/\/([^\/]+)\//i;
		match = exp.exec(url);
		if(null != match){
			return match[0];
		}
		return url;
	}
}



//检验是否插key
function companyRegister(){
	try{
		var cp=new ClientProvider();
		var client=cp.GetClient();
		client._GetMailCtrl().logout();
		var sCert = client._GetP11Mgr().SelectSignCert();
		var info = createCertInfo(sCert);
		if(!info.isOrgCert()){
			alert("请插入企业数字证书");
			return;
		}
		var newSignCert = sCert.GetCertB64();
		if (newSignCert == null || newSignCert == "" ){
			alert("读取证书信息错误，请重试！");
			return ;
		}
		//post方式提交
		var f = document.createElement("form");
		document.body.appendChild(f);
		var i = document.createElement("input");
		i.type = "hidden";
		f.appendChild(i);
		i.name = "signCert";
		i.value = newSignCert;
		f.method="post";
		f.action = "preferences/registerStartUnit.do?from=unit";
		f.submit();
	}catch (e) {
		if(e.message=='对象不支持“UILogin”属性或方法'){
			alert("登陆失败:请检查是否安装了数字证书助手和最新的邮件客户端");
		}else{
			alert(e.message);
		}
	}
}
//创建证书对象，并初始化
function createCertInfo(sCert){
	
 	var info=new CertInfo();
   	try{info.subject.givenName=sCert.GetSubjectItem("G");}catch(e){
   	info.subject.givenName=sCert.GetSubjectItem("CN");}
   	try{info.subject.org=sCert.GetSubjectItem("O");}catch(e){}
   	try{info.subject.orgUnit=sCert.GetSubjectItem("OU");}catch(e){}
   	try{info.subject.telphone=sCert.GetSubjectItem("Phone");}catch(e){}
   	try{info.subject.alias=sCert.GetSubjectItem("Alias");}catch(e){}
   	try{info.subject.email=sCert.GetSubjectItem("E");}catch(e){}
   		try{info.subject.cn=sCert.GetSubjectItem("CN");}catch(e){}
   	return info;	
}

</script>
<style type="text/css">
#nosure {	color: #0066CC;
	text-decoration: none;}
#nosure:hover {color: #FF6600;
	text-decoration: underline;}
</style>
</head>
<body  style="margin:0px;background-color: white">
<table name="ccr" id="ccr" width="100%" bgcolor="#FFE0E0" style="padding: 0px;margin: 0px;position: absolute;top:0;font-size:14px;z-index:999;left:0;"></table>
<div name="ccr2" id="ccr2" width="100%" bgcolor="#FFE0E0" style="padding: 0px;margin: 0px;display:none"></div>
<div id="downclient_center"> 
<div id="downclient_message"></div> 
<div id="downclient_loading"><div></div></div> 
</div>
	<div class="login_top">
		<div class="logo">
			<img src="css/login/images/logo.png"/>
		</div>
		<div class="help">
			
		</div>
	</div>
	<div class="main_bg">
<form action="/loginEnd" method="post" styleId="loginForm" style="margin: 0px;" >
	<div class="login_mid">
		<div class="login_cnt">
			<div class="login_box">
			</div>
			<div class="login_box_1">
				<div class="corner"></div>
				<div class="clear"></div>
				<div class="txt" style="margin-top:60px;">请插入USBkey登录</div>
				<div style="margin:12px 0 10px 0;text-align: center;color: red;">
				<%if(supAdminList.size()>0) { %>
    				系统管理员已创建，请不要重复操作
		    	<%}else{ %>
		    		<a href="preferences/registerAdmin.do?c=<%=Constants.SUP_ADMIN%>" class="btn">创建系统管理员</a>
		    	<%} %>
				</div>
				<div style="margin:12px 0 10px 0;text-align: center;color: red;">
					<%if(audAdminList.size()>0) { %>
			    		系统审计员已创建，请不要重复操作
			    	<%}else{ %>
			    		<a href="preferences/registerAdmin.do?c=<%=Constants.AUDIT_ADMIN%>" class="btn">创建系统审计员</a>
			    	<%} %>
				</div>
				
				<!-- <div style="text-align:center;">
					<a href="javascript:changepin();" style="font-size:14px;color:#FFFFFF; text-decoration:underline;"><em>点击修改USBkey密码</em></a>
				</div> -->
				<div style="text-align:center;padding-top:10px;">
					<div style="display:none">
				  		
					</div>
					<em class="error">
						<c:if test="${not empty loginInvalid }">
					  	登录失败,<c:out value="${ loginInvalid }"/>
						</c:if>
						<c:if test="${sessionExpired == 'true'&&param.msg ne 'timeout'}">
							超时，请重新登录
						</c:if>
						<c:if test="${myexception != null}">
							<c:out value="${myexception}"/>
						</c:if>
						<c:if test="${param.msg eq 'timeout'}">
							由于您长时间没有任何操作,系统已自动退出
						</c:if>
						<c:if test="${param.msg eq 'withoutkey'}">
							由于您将KEY已拔出,系统已自动退出
						</c:if>
						&nbsp;
					</em>
				</div>
				
			</div>
<input type="hidden" name="useCertLogin" />  
<input type="hidden" name="f" value="manageLogin"/>   

<input type="hidden" name="loginRandom" value="<c:out value="${requestScope.loginRandom}" />" /> 
		</div>
	</div>
</form>
	
<script type="text/javascript">
function setmid(){
	var clientHeight = document.documentElement.clientHeight;
	var paddTop = (clientHeight-440)/2-80;
	if(paddTop>0){
		document.body.style.paddingTop=paddTop+"px";
	}
}
window.onload = setmid;
window.onresize = setmid;
ShowPanel=function(message){
	var divobj=document.getElementById('ccr2');
	var warningHTML="<table width=\"100%\" style=\"padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px; margin-top: 0px; margin-right: 0px; margin-bottom: 0px; margin-left: 0px;\" bgColor=\"#ffe0e0\">";
	    warningHTML+="<tbody><tr><td align=\"left\">&nbsp;&nbsp;<img src=\"images/netdisk-images/warn.gif\" />&nbsp;<span class=\"errorMessage\"><font color=\"#ff0000\">";
	    warningHTML+=message;
	    warningHTML+="</a><//a></font></span></td></tr></tbody></table>";
	    divobj.innerHTML=warningHTML;
	    divobj.style.display='';
}

if(msieversion()==0){
	ShowPanel("您当前所使用的浏览器与“安全邮”不兼容，这将会造成“安全邮”无法使用！建议关闭当前页面，并启动微软IE（Internet Explorer）浏览器进行访问。");
}
</script>
</body>
</html>