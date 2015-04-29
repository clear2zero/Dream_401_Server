var forceUpdate=true;//是否强制升级安全邮客户端
var forceUpdateHelper=true;//是否强制升级数字证书助手
var helperVersion='50006';//数字证书助手要求最低版本 
var clientVersion='46370';//安全邮客户端要求最低版本 
var platformDomain = '';

window.onbeforeunload=function(){
	//if (document.body.offsetWidth-50 < event.clientX && event.clientY<0 &&  document.body.offsetWidth> event.clientX)
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
	/*try{
	    setTimeout("Test('hebca')",50);
	}catch(e){
	  alert(e.message);
	}
	if(!parent.frames["mainFrame"]){
		var lc = getBaseURL();
		location.href=lc;
	}
	isShowRegComp();*/
	
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
function checkOutKey(flag){
	/*try{
		var cp=new ClientProvider();
		var client=cp.GetClient();
		var result = client.UILogin();
		if(result!=true ){
			reqrandom = "";
			if(result!=false)
				alert(result);
		   return ;
		}
		document.all('signCert').value=client.GetSignCert();
		document.all('cryptCert').value=client.GetCryptCert();
		
		if(!client.ImportCert())
		{
			alert("无法导入证书到浏览器");
			return ;
		}
		
		//导入国密算法参数
		client.ImportKeyEncryptParam();
		
		window.location.href="../preferences/registerStart.do";
	}catch (e) {
		if(e.message=='对象不支持“UILogin”属性或方法'){
			alert("登陆失败:请检查是否安装了数字证书助手和最新的邮件客户端");
		}else{
			alert(e.message);
		}
	}*/
	//var href = window.location.href;//试用变量保存当前页面href，否则重新指定会报js权限问题，（貌似是jquery在ie6，7下导致）
	window.location.href="../unit/frontRegister.action";
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


function SetHome(obj,vrl){
    try{
            obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);
    }
    catch(e){
            if(window.netscape) {
                    try {
                            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
                    }
                    catch (e) {
                            alert("此操作被浏览器拒绝！");
                    }
                    var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
                    prefs.setCharPref('browser.startup.homepage',vrl);
             }
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
		f.action = "../preferences/registerStartUnit.do?from=unit";
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
//是否显示“单位注册”
function isShowRegComp(){
	var localDomain = window.location.host;
	if(localDomain && platformDomain !=""){
		if(localDomain.indexOf(platformDomain) >= 0){
			$("#RegCompLi").show();
			RegCompFlag = true;
		}
	}
}

var currentVersion=0;
function validate(ctrlMode) {
	//检测客户端安装情况 2013-08-05 郭璇
	var cp=new ClientProvider();
	var  installFlag = cp.GetClient().CheckInstall(true);
	if(!installFlag){
		return;
	}
	
	var form = document.forms['loginForm'];
	
	//if use common ca mode, create a pkcs7
	if(ctrlMode=="common")
	{
		document.all("authMode").value="Pkcs7Login";
		if(CreatPkcs7())
			document.forms['loginForm'].submit();
	}
	else
	{
		//if use hebca mode , sign it
		document.all("authMode").value="SignLogin";
		
		//设置并获取服务器随机数	
		if( reqrandom != ''){
			alert("系统正在登陆,请稍候重试...");
			return ;
		}
		reqrandom = Math.floor(Math.random()*100000);
		
		var url = "${ctx}/loginRandom.cl?random="+reqrandom;
		var callback = {
			success : function(o) {
				if(ctrlMode == "auto"){
					if(autoSign(o)){
						document.forms['loginForm'].submit();
					}
				}else {
					if(Sign(o)){
						document.forms['loginForm'].submit();
					}
				}
			},
			failure : function(o, XMLHttpRequest, ajaxOptions, thrownError) {
				reqrandom = "";
				alert("登录失败,可能由于网络原因,请重试");
			},
			argument : []
		};
		jQuery.ajax({type:'GET',url:url,
		success:function(xhr){
			callback.success(xhr);
		},error:function(xhr, XMLHttpRequest, ajaxOptions, thrownError){
			callback.failure(xhr, XMLHttpRequest, ajaxOptions, thrownError);
		}
		});
	}
}


function Sign(ran)
{
	try
	{
		//var oridata=document.all("loginRandom").value;
		var oridata = ran;
		var cp=new ClientProvider();
		var client=cp.GetClient();
		var obj = client._GetP11Mgr();
		obj.ReloadDevice();
		client._GetMailCtrl().logout();
		var result = client.UILogin();
		if(result!=true ){
			reqrandom = "";
			if(result!=false)
				alert(result);
		   return ;
		}
		document.all('signature').value=client.Sign(oridata);
		document.all('signCert').value=client.GetSignCert();
		document.all('cryptCert').value=client.GetCryptCert();
		
		if(!client.ImportCert())
		{
			alert("无法导入证书到浏览器");
			return false;
		}
		
		//导入国密算法参数
		client.ImportKeyEncryptParam();
		
		return true;
	}
	catch(e)
   	{
		reqrandom = "";
		if(e.message=='对象不支持“UILogin”属性或方法'){
			alert("登陆失败:请检查是否安装了数字证书助手和最新的邮件客户端");
		}else if(e.message=='对象不支持此属性或方法'){//add by Linxiao
			//alert("您的电脑没有安装客户端软件,请下载安装");
			alert("您尚未安装数字证书助手，请下载安装数字证书助手");//add bu quchenyong
		}else{
			alert(e.message);
		}
   		return false;
   	}	
}

function autoSign(ran)
{
	try
	{
		//var oridata=document.all("loginRandom").value;
		var oridata = ran;
		var cp=new ClientProvider();
		var client=cp.GetClient();
		var obj = client._GetP11Mgr();
		obj.ReloadDevice();
		var count = obj.GetDeviceCount();
		
		for(var i = 0; i < count; i++) {
			var device = obj.GetDevice(i);
			var signCert = device.GetSignCert(0);
			var encryptCert = device.GetEncryptCert(0);
			var certcn = "";
			var group1=signCert.GetSubjectItem('CN');
			var group2=signCert.GetSubjectItem('G');
			var cnLength=group1.replace(/[^\x00-\xff]/g,"**").length;
			var gLength=group2.replace(/[^\x00-\xff]/g,"**").length;			    
			if(cnLength>gLength){
				certcn = group1;
			}else{
				certcn = group2;
			}
			//if("ceshi8444323"==$("input[name=certcn]").val()){
			if(certcn==$("input[name=certcn]").val()){
	        	var result = client.UILogin();
	    		if(result!=true ){
	    			reqrandom = "";
	    			if(result!=false)
	    				alert(result);
	    		   return false;
	    		}
	    		document.all('signature').value=client.Sign(oridata);
	    		document.all('signCert').value=signCert.GetCertB64();
	    		document.all('cryptCert').value=encryptCert.GetCertB64();
	    		return true;
	        }else {
	        	return false;
	        }
	    } 
		
		return true;
	}
	catch(e)
   	{
		reqrandom = "";
		if(e.message=='对象不支持“UILogin”属性或方法'){
			alert("登陆失败:请检查是否安装了数字证书助手和最新的邮件客户端");
		}else if(e.message=='对象不支持此属性或方法'){//add by Linxiao
			//alert("您的电脑没有安装客户端软件,请下载安装");
			alert("您尚未安装数字证书助手，请下载安装数字证书助手");//add bu quchenyong
		}else{
			alert(e.message);
		}
   		return false;
   	}	
}


function CreatPkcs7()
{
	var thc=TestHebcaClient();
	var thmc=TestHebcaMailClient();
	
	//client setup ok
	if(!( thc==0 && thmc==0) )
	{
		alert("您没有正确安装客户端软件，或者系统拦截了ActiveX 控件！");
		return false;
	}

  	try
	{
		mc.clear();
		var pkcs7="";
   	 	oridata=document.all("loginRandom").value;
   	 	
		pkcs7=mc.Pkcs7Sign(1,oridata,true);
		if(mc.HasError())
		{
			var errno=mc.GetError();
			if(errno==17)
				alert("您还没有导入证书，请先通过\"开始->程序->河北CA->数字证书工具->证书导入\"导入您的证书！");
			else		
				alert(mc.GetErrorDescription());
				
			return false;
		}
		else
		{
			document.all('pkcsCode').value=pkcs7;
			return true;
		}
	}
	catch(e)
	{
		alert("您没有正确安装客户端软件，或者系统拦截了ActiveX 控件！");
		return false;
	}
}

function Test(caMode)
{
	var cp=new ClientProvider();
	cp.GetClient().CheckInstall();
	
}

function ReTest()
{
	window.location.reload();
}
