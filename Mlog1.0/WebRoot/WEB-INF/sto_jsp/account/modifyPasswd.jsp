<%@ page language="java" pageEncoding="UTF-8"%>
<style type="text/css">
ul{list-style:none;}
.div1{ width:30%; float:left;}
.div2{ width:30%; float:left;}
.div3{ width:40%; float:left;}
.div1 ul{}
.div1 ul li{float:none; line-height:40px; height:40px;margin-right:10px; text-align:right;color:#222222;font-size:16px;}
.div2 ul li{float:none; line-height:40px; height:40px;margin-right:10px; text-align:right;color:#222222;font-size:16px;}
.div3 ul li{float:none; line-height:40px; height:40px;margin-right:10px; text-align:left;color:#222222;font-size:16px;}
</style>
<script type="text/javascript">
$('#pwd').bind('focus',function(){
	$("#pwd_li").html('');
});
$('#rpwd').bind('focus',function(){
	$("#rpwd_li").html('');
});
$("#pwd").bind('change',function(){
	if($("#pwd").val().length < 6){
		$("#pwd").val('');
		$("#pwd_li").html('<span><font color="red">密码至少6位以上</font></span>');
	}
});
$("#rpwd").bind('change',function(){
	if(String($("#pwd").val()).replace('/\s/','') != ''){
		if($("#rpwd").val() != $("#pwd").val()){
			$("#rpwd").val('');
			$("#rpwd_li").html('<span><font color="red">与输入密码不一致</font></span>');
		}
	}else {
		$("#rpwd").val('');
		$("#pwd_li").html('<span><font color="red">密码至少6位以上</font></span>');
	}
});
</script>
<div width="100%" style="padding: 5px; overflow: hidden;">
	<form method="post">
		<input type="hidden" name="id" value="${o.id}" />
		<div class="div1">
			<ul>
				<li>新密码</li>
				<li>确认密码</li>
			</ul>
		</div>
		<div class="div2">
			<ul>
				<li><input id="pwd" name="pwd" type="password" class="easyui-validatebox" autocomplete='off' data-options="required:true"/></li>
				<li><input id="rpwd" name="rpwd" type="password" class="easyui-validatebox" autocomplete='off' required="required" /></li>
			</ul>
		</div>
		<div class="div3">
			<ul>
				<li id="pwd_li"></li>
				<li id="rpwd_li"></li>
			</ul>
		</div>
	</form>
</div>