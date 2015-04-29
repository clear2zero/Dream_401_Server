package sto.common.sms;

import java.sql.Timestamp;
import java.util.Date;


import com.hebca.sms.MessageNotification;
import com.hebca.sms.MessageStatus;
import com.hebca.sms.ShortMessage;

public class SmsMessageNotification implements MessageNotification {

	public void process(ShortMessage msg) {
		//添加发送短信的结果到数据库中
		try
		{
			String messageStatus=null;
			MessageStatus status=msg.getMessageStatus();
			if(status==MessageStatus.SENT)
			{
				messageStatus="已发送";
			}
			else if(status==MessageStatus.UNSENT)
			{
				messageStatus="未发送";
			}
			else if(status==MessageStatus.FAILED)
			{
				messageStatus="发送失败";
			}
			//SmsLogController.UpdateSmsLog(Long.parseLong(msg.getSenderName()),messageStatus);
		}
		catch(Exception e)
		{
			return;
		}
	}

}
