<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<%@ include file="/WEB-INF/sto_jsp/include/css.jsp"%>
	 <style type="text/css">
		#preview_wrapper{
		    display:inline-block;
		    width:300px;
		    height:300px;
		    background-color:#CCC;
		}
		#preview_fake{ /* 该对象用户在IE下显示预览图片 */
		    filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);
		}
		#preview_size_fake{ /* 该对象只用来在IE下获得图片的原始尺寸，无其它用途 */
		    filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);
		    visibility:hidden;
		}
		#preview{ /* 该对象用户在FF下显示预览图片 */
		    width:300px;
		    height:300px;
		}
    </style>
    <%@ include file="/WEB-INF/sto_jsp/include/js.jsp"%>
<script type="text/javascript">
/****图片预览操作****/
function onUploadImgChange(sender){
    if( !sender.value.match( /.jpg|.gif|.png|.bmp/i ) ){
        alert('图片格式无效！');
        return false;
    }
    var objPreview = document.getElementById( 'preview' );
    var objPreviewFake = document.getElementById( 'preview_fake' );
    var objPreviewSizeFake = document.getElementById( 'preview_size_fake' );
    objPreviewSizeFake.style.display = '';
    if( sender.files && sender.files[0] ){
        objPreview.style.display = 'block';
        objPreview.style.width = 'auto';
        objPreview.style.height = 'auto';
        // Firefox 因安全性问题已无法直接通过 input[file].value 获取完整的文件路径
        objPreview.src = window.URL.createObjectURL(sender.files[0]); //sender.files[0].getAsDataURL();
    }else if( objPreviewFake.filters ){
        // IE7,IE8 在设置本地图片地址为 img.src 时出现莫名其妙的后果
        //（相同环境有时能显示，有时不显示），因此只能用滤镜来解决
        // IE7, IE8因安全性问题已无法直接通过 input[file].value 获取完整的文件路径
        sender.select();
        var imgSrc = document.selection.createRange().text;
        objPreviewFake.filters.item(
            'DXImageTransform.Microsoft.AlphaImageLoader').src = imgSrc;
        objPreviewSizeFake.filters.item(
            'DXImageTransform.Microsoft.AlphaImageLoader').src = imgSrc;
        autoSizePreview( objPreviewFake,
            objPreviewSizeFake.offsetWidth, objPreviewSizeFake.offsetHeight );
        objPreviewSizeFake.style.display = 'none';
    }
}
function onPreviewLoad(sender){
    autoSizePreview( sender, sender.offsetWidth, sender.offsetHeight );
}
function autoSizePreview( objPre, originalWidth, originalHeight ){
    var zoomParam = clacImgZoomParam( 300, 300, originalWidth, originalHeight );
    objPre.style.width = zoomParam.width + 'px';
    objPre.style.height = zoomParam.height + 'px';
    objPre.style.marginTop = zoomParam.top + 'px';
    objPre.style.marginLeft = zoomParam.left + 'px';
}
function clacImgZoomParam( maxWidth, maxHeight, width, height ){
    var param = { width:width, height:height, top:0, left:0 };
    if( width>maxWidth || height>maxHeight ){
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;
        if( rateWidth > rateHeight ){
            param.width = maxWidth;
            param.height = height / rateWidth;
        }else{
            param.width = width / rateHeight;
            param.height = maxHeight;
        }
    }
    param.left = (maxWidth - param.width) / 2;
    param.top = (maxHeight - param.height) / 2;
    return param;
}
function checkData() {
	if (String($("#amcontent").val()).replace('\s','') == '') {
		alert('请填写日志内容');
		return false;
	}
	return true;
}

function save(){
	$("#form").form('submit',{
		onSubmit: function(param) {
			//去掉此onSubmit,提交不执行。可能easyui的Bug
			var ischeck = checkData();
			if(ischeck == false){
				$("#btn").attr("disabled",false);
			}
			return ischeck;
		},
    	success:function(json){
    		$("#btn").attr("disabled",false);
    		var jsonObj = eval('(' + json + ')');
			if (jsonObj.success) {
				$("#amcontent").val('');
			};
			sy.messagerShow({
				msg : jsonObj.msg,
				title : '提示'
			});
	      }
	}); 
}

</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<form id="form" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/journal/saveDo.action">
<input id="userid" name="userid" type="hidden" value="${user.id}" >
<input id="name" name="name" type="hidden" value="${user.name}" >
<input id="createtime" name="createtime" type="hidden" value="${createtime}">
<div data-options="region:'north',border:false" style="overflow: hidden;">
	<div align="top" style="padding: 5px; overflow: hidden;">
		<table class="tableForm" style="width: 50%; height: 100%;">
			<tr>
				<td style="font-size: 12px;">填写日期：<input id="createtime1" name="createtime1" class="easyui-datebox" data-options="editable:false,disabled:true" value="${createtime}" ></td>
				<td style="font-size: 12px;">填写人：<input id="name1" name="name1" readonly value="${user.name}"></td>
				<td><input id="btn" class="btn4" type="button" value="提交" onclick="this.disabled=true;save()"/></td>
			</tr>
		</table>
	</div>
</div>
<div data-options="region:'center',border:false" style="overflow: hidden;">
	<div align="left" style="padding: 5px; overflow: hidden;">
			<table border="1" cellspacing="0" cellpadding="0" >
				<tr>
					<td align="right" rowspan="5" >工作日志</td>
				    <td id="tdDesArea" align="left" rowspan="5"><textarea rows="25" cols="100" id="amcontent" name="amcontent" ></textarea></td>
				</tr>
				</tr>
			</table>
	</div>
</div>
</form>
</body>