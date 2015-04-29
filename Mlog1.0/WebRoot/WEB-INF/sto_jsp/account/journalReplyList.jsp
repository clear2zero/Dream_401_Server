<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/sto_jsp/include/taglib.jsp"%>
<div style="height:350px;overflow:true">
		<div style="overflow: auto;float:left ">
			<div class="one_entityext">
	            <div class="answercount">
	                <div class="diggitext">
	                    <div class="diggnumext unanswered">日志内容</div>
	                </div>
	                <div class="clear">
	                </div>
	            </div>
	            <div style="padding-bottom:8px;margin-left:80px;border:none">
	                <h2 class="news_entry">
	                </h2>
	                <div class="news_summary">
	                   ${content}
	                </div>
	                <div class="news_footer">
	                </div>
	                <div class="clear">
	                </div>
	            </div>
	        </div>
			<c:forEach items="${replyList}" var="map" varStatus="status">
				<div class="one_entity" style="float:left ">
	                    <div class="answercount">
	                        <div class="diggit">
	                            <div class="diggnum unanswered">${status.index+1}</div>
	                        </div>
	                        <div class="clear">
	                        </div>
	                    </div>
	                    <div class="news_item">
	                        <h2 class="news_entry">
	                        </h2>
	                        <div class="news_summary">
	                           ${map['recontent']}
	                        </div>
	                        <div class="news_footer">
	                            <div style="float: left;">
	                            </div>
	                            <div style="float: right; width: 190px;">
	                                <a href="javascript:void(0)" class="author">${map['name']}</a>
	                                    <span title="回复时间" class="date">${map['redate']}</span>                                
	                            </div>
	                        </div>
	                        <div class="clear">
	                        </div>
	                    </div>
	                </div>
	                <div class="clear"></div>
			</c:forEach>
	</div>
	<div style="float:left">
		<form id="searchForm">
			<input type="hidden" name="replyer" value="${replyer}" >
			<input type="hidden" id="journalid" name="journalid" value="${journalid}">
			<table style="width：80%;height: 100%;">
				
				<tr style="padding-left:10px;">
					<td style="height:90%;align:left;padding-left:0px;" >
					<textarea rows="12" cols="75" id="content" name="content"></textarea></td>
				</tr>	
				<tr>
					<td style="align:left;padding-left:420px;font-size: 12px;">
						<input type="button" id="button1" class="bgbutton" value="回复" onclick="reply();"/>
					</td>
				</tr>	
				<tr><td></td></tr>		
			</table>
		</form>
	</div>
</div>
<script type="text/javascript">
function reply(){
	var journalid = $("#journalid").val();
	var content = $("#content").val();
	$.ajax({
	      type : "post",
	      url : "${pageContext.request.contextPath}/journal/replyJournal.action",
	      data : {journalid:journalid,content:content},
	      dataType : 'json',
	      success : function(json){
			if (json.success) {
				$('#reply').dialog('refresh');
			}
			sy.messagerShow({
				msg : json.msg,
				title : '提示'
			});
	      }
	});
}
</script>
