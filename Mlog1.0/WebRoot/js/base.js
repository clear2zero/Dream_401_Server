//是否打开js调试模式
var sysDebug = true;

function getBaseURL(){
 var mail_base_url = location.protocol+"//"+ location.host
 	if(location.protocol=="https:"){
	   if(location.port=="")
		   mail_base_url = mail_base_url+":443";
 	}else{
	   if(location.port=="")
		   mail_base_url = mail_base_url+":80";
 	}
 	baseContextPath = ctx;
 	if(baseContextPath){
 		if(baseContextPath.indexOf("/")==0)
 			mail_base_url = mail_base_url +baseContextPath+"/";
 		else
 			mail_base_url = mail_base_url + "/" +baseContextPath+"/";
 	}else{
 		if(location.pathname.indexOf("/claros")==0)
 			mail_base_url = mail_base_url +"/claros/";
 		else
 			mail_base_url = mail_base_url +"/";
 	}
   
   return mail_base_url;
}


//add by xj 2013年12月27日 判断是否为IE浏览器  
function msieversion()
{
var ua = window.navigator.userAgent;
var msie = ua.indexOf ("MSIE");
if ( msie > 0 )      // Internet Explorer
  //遨游，IE11等浏览器 不能返回争取的版本号
  // return parseInt (ua.substring (msie+5, ua.indexOf (".", msie )));
	return 1;
else                 
	return 0;
}

//add by xj 2013/10/15 获取安全邮控件
function getMailCtrl(){
   var mail_ctrl;
   if(mc)
	   return mc;
   if(top.frames['checkKeyFrame']){
	 var mc = top.frames['checkKeyFrame'].mymc;
   }else{
	   if(document.body){
		      document.body.insertAdjacentHTML(
					"afterEnd",
					'<OBJECT classid="CLSID:78D1D24A-C552-4402-95FA-E9CA2E8924F2"  CODEBASE="../client/Package.cab#version=1,0,1.1" id="mc" VIEWASTEXT width="1" height="1"></object>'
					);
			  mail_ctrl=document.getElementById("mc");
			  jsdebug("base.js - getMailCtrl() - 创建mail_ctrl");
		   }else{
			   jsdebug("base.js - getMailCtrl() - 创建mail_ctrl失败");
		   }
   }
 if(mc){
    mail_ctrl=mc;
    jsdebug("base.js - getMailCtrl() - 从checkKey.jsp中获取mail_ctrl");
 }
   return mail_ctrl;
}

//add by xj 2013/10/15 获取P11 证书对象
function getCertMgr(){
   var mail_CertMgr;
   var mc = top.frames['checkKeyFrame'].certMgrObj;
   try{
     if(mc){
    	 mail_CertMgr=mc;
    	 jsdebug("base.js - getCertMgr() - 从checkKey.jsp中获取CertMgr");
     }
   }catch(e){
	   jsdebug("base.js - getCertMgr() - 创建CertMgr");
	   var cp=new ClientProvider();
	   mail_CertMgr= cp.GetClient()._GetP11Mgr();
   }
   return mail_CertMgr;
}

//add by xj 2013/10/9 增加调试信息
function jsdebug(msg){
	if(sysDebug){
		var d = new Date();
		debugstr = "["+ (d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds() 
		+":"+d.getMilliseconds()+ "] - "+msg +" - 访问地址：" 	+location.pathname;
		try {
			//window.clipboardData.setData("Text",debugstr);
			console.log(debugstr);
		} catch (e) {
		
		}
	}
}


function toggleAll(form) {
	var newStatus = form.nope.checked;
	var inp = form.getElementsByTagName("input");
	for (var i=0;i<inp.length;i++) {
		inp[i].checked = newStatus;
	}
}

function toggleAlls(form) {
	var newStatus = form.nope.checked;
	var inp=document.getElementsByName("selectuser");
	for (var i=0;i<inp.length;i++) {
		inp[i].checked = newStatus;
	}
}
function trim(str) {
	if (str == null) return null;
	return str.replace(/^\s*|\s*$/g,"");
}
