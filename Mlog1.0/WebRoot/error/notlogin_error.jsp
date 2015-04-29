<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/includes/top_main.jsp"%>
<html>
<head>
<style type="text/css">
	.icon1{background: url("../images/alertico.gif");}
	</style>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
	<tr>
			
		<td valign="top" colspan="2" width="95%">
			<table width="100%" border="0" height="40" align="left" cellpadding="0" cellspacing="0">
		<!-- Begin You are here -->
				<tr>
					<td colspan="2">
						<table width="99%" height="30" border="0" cellpadding="3" cellspacing="3" align="left">
							<tr>
								<td nowrap width="96%">
								&nbsp;
								<img src="../images/grey_bullet.gif" width="5" height="12">
								<span>错误处理: </span>								</td>
								<td>&nbsp;
								
								</td>
						  </tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%" height="1" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td bgcolor="#147CB1"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="19px" bgcolor="#9fbef3">&nbsp;
					
					</td>
				</tr>
				<tr>
					<td width="40px">&nbsp;
					
					</td>
					<td>
					<br>
						<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
							<tr>
								<td valign="top" width="99%">
									<table width="600px" align="left" border="0" cellspacing="3" cellpadding="0">
										<tr>
											<td height="40px">
												<br>						
<p>&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:14px;line-height:16px;width:15px" class="icon1">&nbsp;&nbsp;</span><span style="font-size:16px">&nbsp; 系统出现错误</span></p>
<hr style="border:1px dotted #39f;margin-top: -3px;" size="0px">
<dir class="condiv">
	<B class="icoIfo2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</B>系统中出现了错误<span id="firstReadCon">，详细信息为：</span>
	<br>
	<br>
	<pre>
			<c:out value="${myexception.userMessage}"></c:out>
	</pre>
<hr style="border:1px dotted #39f;margin-top: -3px;" size="0px">
</dir>
<table border="0px" width="136" style="margin-left: 100px;margin-top: 40px">
	<tr>
			<td width="130px" height="33"
			style="background: url('../images/sendmail/3.gif')"
			onmouseover="this.style.background='url(../images/sendmail/2.gif)'"
			onmouseout="this.style.background='url(../images/sendmail/3.gif)'">
			<a style="padding-left:35px;font-weight: 600;"
				href="javascript:history.go(-1);">
				<font color='#444444'>返回上一步</font></a></td>
								</tr>
							</table>

					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<!-- END CONTENT -->


					</td>
				</tr>
				<tr>
					<td colspan="2" height="19px" bgcolor="#73a2d6">&nbsp;
						
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%" height="1" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td bgcolor="#147CB1"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>


		</td>
	</tr>
</table>

</body>
</html>
