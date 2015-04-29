<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script src="${ctx}/_static/js/jquery/jquery-1.8.3.min.js"></script>
<link rel="shortcut icon" href="${ctx}/_static/images/front/favicon.ico" type="image/x-icon" />
<link href="${ctx}/_static/css/front/main.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${ctx}/js/niceValidator/jquery.validator.css">
<script type="text/javascript" src="${ctx}/js/niceValidator/jquery.validator.js"></script>
<script type="text/javascript" src="${ctx}/js/niceValidator/local/zh_CN.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
$(function(){
	$("#form")[0].reset();
	$("#isRegistered").attr('checked',false);
	$("#isRegistered").val("0");
	$("input[name=repwd]").removeAttr("novalidate");
	$("input[name=divname]").removeAttr("novalidate");
	$("input[name=addr]").removeAttr("novalidate");
	$("input[name=tel]").removeAttr("novalidate");
	$("input[name=corporation]").removeAttr("novalidate");
	$("input[name=name]").removeAttr("novalidate");
	$("input[name=mobilephone]").removeAttr("novalidate");
	$("input[name=identitycard]").removeAttr("novalidate");	
	$("input[name=verifycode]").removeAttr("novalidate");	
	$("li[name=code_li]").css("display","block");
	$("li[name=info_li]").css("display","block");
	$("#regbuttonm").html('<a href="javascript:void(0)" onclick="register()" class="button orange">立即注册</a>');
	
	$("#isRegistered").bind('click',function(){
		var checked = false;
		if($("#isRegistered").attr("checked") == "checked"){
			checked = true;
			$("#isRegistered").val("1");
			$("input[name=repwd]").attr("novalidate","novalidate");
			$("input[name=divname]").attr("novalidate","novalidate");
			$("input[name=addr]").attr("novalidate","novalidate");
			$("input[name=tel]").attr("novalidate","novalidate");
			$("input[name=corporation]").attr("novalidate","novalidate");
			$("input[name=name]").attr("novalidate","novalidate");
			$("input[name=mobilephone]").attr("novalidate","novalidate");
			$("input[name=identitycard]").attr("novalidate","novalidate");
			
			$("li[name=code_li]").css("display","block");
			$("li[name=info_li]").css("display","none");
			$("input[name=verifycode]").removeAttr("novalidate");
			
			$("#regbuttonm").html('<a href="javascript:void(0)" onclick="applyCert()" class="button orange">申请证书</a>');
		}else {
			checked = false;
			$("#isRegistered").val("0");
			$("input[name=repwd]").removeAttr("novalidate");
			$("input[name=divname]").removeAttr("novalidate");
			$("input[name=addr]").removeAttr("novalidate");
			$("input[name=tel]").removeAttr("novalidate");
			$("input[name=corporation]").removeAttr("novalidate");
			$("input[name=name]").removeAttr("novalidate");
			$("input[name=mobilephone]").removeAttr("novalidate");
			$("input[name=identitycard]").removeAttr("novalidate");
			
			$("input[name=verifycode]").removeAttr("novalidate");
			
			$("li[name=code_li]").css("display","block");
			$("li[name=info_li]").css("display","block");
			
			
			$("#regbuttonm").html('<a href="javascript:void(0)" onclick="register()" class="button orange">立即注册</a>');
		}
		$("#form")[0].reset();
		if(checked){
			$("#isRegistered").attr('checked',true);
			$("#isRegistered").val("1");
		}else {
			$("#isRegistered").attr('checked',false);
			$("#isRegistered").val("0");
		} 
	});
	
	
	$('#form').validator({
	    rules: {
	        //自定义一个规则，用来代替remote（注意：要把$.ajax()返回出来）remote[${ctx}/unit/frontCheckcode.action, username]
	    	usernameRemote: function(element){
	    		if($("#isRegistered").attr("checked") != "checked"){
	    			return $.ajax({
		                url: '${ctx}/unit/frontCheckUsername.action',
		                type: 'post', 
		                data: element.name +'='+ element.value,
		                dataType: 'json',
		                success: function(json){
		                	
		                }
		            });
	    		}else {
		            return true;
	    		}
	        },codeRemote: function(){
	            return $.ajax({
	                url: '${ctx}/unit/frontQueryUnit.action',
	                type: 'post', 
	                data: {verifycode:$("#verifycode").val(),username:$("#username").val(),password:$("#password").val()},
	                dataType: 'json',
	                success: function(json){
	                	if(json.success && json.success == true){
		                	$("#acceptno").val(json.data.acceptno);
	  						$("#divname").val(json.data.divname);
	                	}
	                }
	            });
	        },divnameRemote: function(element){
	            return $.ajax({
	                url: '${ctx}/unit/frontCheckDivname.action',
	                type: 'post', 
	                data: element.name +'='+ element.value,
	                dataType: 'json',
	                success: function(json){
	                	
	                }
	            });
	        }
	    },
	    fields: {
	        'verifycode': '验证码:required;length[6];codeRemote;','username':'用户名:required;username;usernameRemote;','divname':'企业名称:required;divname;divnameRemote'
	    }
	});
});
function clearForm(){
}
function checkForm(){	
	if($("input[name=agreest]").attr("checked")!="checked"){
		alert("请同意服务协议");
		return false;
	}
	return true;
}

function register(){
	$("#form").attr('action','${ctx}/unit/frontRegisterDo.action');
	$("#form").submit();
}
function applyCert(){
	$("#form").attr('action','${ctx}/unit/frontUnitLoginDo.action');
	$("#form").submit();
}
</script>
</head>
<body>
<div id="sitop">
	<div id="sitopcenter">
	<div id="toplogo"><img src="${ctx}/_static/images/front/indexlogo_05.png" width="220" height="80" /></div>
	<div id="toptexta">用户注册</div>
	<div id="toptextb"><a href="${ctx}/front/main.action">返回首页</a> | <a href="javascript:void(0)">帮助中心</a></div>
	</div>
</div><!-----sitop------>
<div id="regwrap">
	<div id="signcenter">
		<div id="regt"></div>
		<div id="regc">
		<form id="form" method="post" action="${ctx}/unit/frontRegisterDo.action" onSubmit="return checkForm()" autocomplete="off">
			<input id="acceptno" name="acceptno" type="hidden"/>
			<div class="regtable">
				<div class="regtableh1">
					<ul>
						<li></li>
						<li>*登录名</li>
						<li>*密码</li>
						<li name="info_li">*确认密码</li>
						<li name="info_li">*企业名称</li>
						<li name="info_li">*企业地址</li>
						<li name="info_li">*电　　话</li>
						<li name="info_li">*企业法人</li>
						<li name="info_li">*您的姓名</li>
						<li name="info_li">*您的手机</li>
						<li name="info_li">*您的身份证号码</li>
						<li name="code_li" style="display:none">*验证码</li>
						<li></li>
						<li name="istest_li" style="display:none">是否试用</li>
					</ul>
				</div><!-----regtableh1------>
				
				<div class="regtableh2">
					<ul>
						<li><input id="isRegistered" name="isRegistered" type="checkbox" class="regtableh2check" value="0" ><font style='color:red'>是否已提交注册申请，未安装证书</font></li>
						<li><input id="username" name="username" type="text" class="regtableh2input required" ></li>
						<li><input id="password" name="password" type="password" class="regtableh2input" data-rule="密码:required;password"></li>
						<li name="info_li"><input id="repwd" name="repwd" type="password" class="regtableh2input" data-rule="确认密码:required;match(password);"></li>
						<li name="info_li"><input id="divname" name="divname" type="text" class="regtableh2input"></li>
						<li name="info_li"><input id="addr" name="addr" type="text" class="regtableh2input" data-rule="企业地址:required;addr"></li>
						<li name="info_li"><input id="tel" name="tel" type="text" class="regtableh2input" data-rule="公司电话:required;tel"></li>
						<li name="info_li"><input id="corporation" name="corporation" type="text" class="regtableh2input" data-rule="企业法人:required;corporation"></li>
						<li name="info_li"><input id="name" name="name" type="text" class="regtableh2input" data-rule="姓名:required;name"></li>
						<li name="info_li"><input id="mobilephone" name="mobilephone" type="text" class="regtableh2input" data-rule="手机号:required;mobilephone"></li>
						<li name="info_li"><input id="identitycard" name="identitycard" type="text" class="regtableh2input" data-rule="身份证号:required;identitycard"/></li>
						<li name="code_li" style="display:none"><input name="verifycode" id="verifycode" class="regtableh2inputext" ><img id='verifycode_img' src="${ctx}/unit/frontRand.action?type=reg" alt="点击更换验证码" align="absmiddle" style="cursor: pointer"
								onClick="this.src='${ctx}/unit/frontRand.action?type=reg&t='+new Date().getTime()" ><span class="msg-box" for="verifycode"></span></li>
						<li><input id="agreest" name="agreest" type="checkbox" class="regtableh2check" value="" checked="checked" >同意<a href="#" target="_blank">移动办公系统服务协议</a></li>
						<li name="istest_li" style="display:none"><input name="istest" type="checkbox" class="regtableh2check" value="" checked="checked" >是　 <input name="istest" type="checkbox" class="regtableh2check" value="" checked="checked" />否</li>
						</ul>
				</div><!-----regtableh2------>
				
				<%-- <div class="regtableh3">
					<ul>
						<li></li>
						<li id="username_li"><h6><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="username_span">企业管理员用户名（4位以上英文/数字），如果已提交注册申请，请输入注册时用户名</span></h6></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg"  /><span id="password_span">如果已提交注册申请，请输入注册时密码</span></li>
						<li name="code_li" style="display:none"><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg"  /><span id="verifycode_span">为了您的安全，请输入您在该图中看到的字符</span></li>
						<li id="divname_li"><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg"  /><span id="divname_span">请填写真实名称，否则帐户可能会被关闭</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="addr_span">请输入企业地址</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="tel_span">请输入企业座机电话</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="corporation_span">将用于您的密码找回和身份确认，我们不会透露给任何第三方</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="name_span">请输入您的姓名</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="mobilephone_span">请输入您的手机</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" /><span id="identitycard_span">您的身份证号码</span></li>
						<li><img src="${ctx}/_static/images/front/regico_08.png" width="21" height="21" class="regtableimg" />我们会稍后协助您开通</li>
						<li name="istest_li" style="display:none"></li>
					</ul>
				</div> --%><!-----regtableh3------>
				<div class="clearfloat"></div>
				<div id="regbuttonm"><a href="javascript:void(0)" onClick="register()" class="button orange">立即注册</a></div>
			</div><!-----regtable------>
			</form>
		</div>
		<div id="regb"></div>
	</div><!-----signcenter------>
	<div class="clearfloat"></div>
	<div class="signbottom">版权所有：2013-2014河北腾翔软件科技有限公司  冀ICP备13016849号-2  电话：0311-69025838</div>
</div><!-----signwrap------>
</body>
</html>
