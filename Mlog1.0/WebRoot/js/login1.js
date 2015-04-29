var helperVersion='50006';//数字证书助手要求最低版本 
var certMgrObj = null;
jQuery(document).ready(function(){
	var _client = new HebcaClient();
	certMgrObj = _client._GetClientCtrl();
});
function getWebRootPath() {
    var webroot=document.location.href;
    webroot=webroot.substring(webroot.indexOf('//')+2,webroot.length);
    webroot=webroot.substring(webroot.indexOf('/')+1,webroot.length);
    webroot=webroot.substring(0,webroot.indexOf('/'));
    var rootpath="/"+webroot;
    return rootpath;
}
var currentVersion=0;
function validate(ctrlMode) {
	var form = document.forms['loginForm'];
	//if use common ca mode, create a pkcs7
	//if use hebca mode , sign it
	document.all("authMode").value="SignLogin";
	
	//设置并获取服务器随机数	
	if( reqrandom != ''){
		alert("系统正在登陆,请稍候重试...");
		return ;
	}
	reqrandom = Math.floor(Math.random()*100000);
	var url = getWebRootPath()+"/loginRandom.cl?random="+reqrandom;
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
	jQuery.ajax({
		type:'GET',url:url,
		success:function(xhr){
			callback.success(xhr);
		},error:function(xhr, XMLHttpRequest, ajaxOptions, thrownError){
			callback.failure(xhr, XMLHttpRequest, ajaxOptions, thrownError);
		}
	});
}


function Sign(ran)
{
	try
	{
		var oridata = ran;
		certMgrObj.Reset();
		certMgrObj.ReloadDevice();
		var signCert = certMgrObj.SelectSignCert();
		var device = signCert.Device;
		document.all('signature').value=signCert.SignText(oridata,1);
		document.all('signCert').value=signCert.GetCertB64();
		document.all('cryptCert').value=device.GetEncryptCert(0).GetCertB64();
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
		certMgrObj.Reset();
		certMgrObj.ReloadDevice();
		var count = certMgrObj.GetSignCertCount();
		for(var i = 0; i < count; i++) {
			try{
				var signCert = certMgrObj.GetSignCert(i);
				var device = signCert.Device;
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
				var encryptCert = device.GetEncryptCert(0);
				if(certcn==$("input[name=certcn]").val()){
		    		document.all('signature').value=signCert.SignText(oridata,1);
		    		document.all('signCert').value=signCert.GetCertB64();
		    		document.all('cryptCert').value=encryptCert.GetCertB64();
		    		return true;
		        }else {
		        	return false;
		        }
			}catch(e){
				e.message;
			}
	    } 
		return true;
	}
	catch(e)
   	{
		reqrandom = "";
		if(e.message=='对象不支持“UILogin”属性或方法'){
			alert("登陆失败:请检查是否安装了数字证书助手");
		}else if(e.message=='对象不支持此属性或方法'){//add by Linxiao
			alert("您尚未安装数字证书助手，请下载安装数字证书助手");//add bu quchenyong
		}else{
			alert(e.message);
		}
   		return false;
   	}	
}