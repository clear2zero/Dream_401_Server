package sto.common.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SmsStartThread extends Thread{
	private boolean startSmsTheads;
	private Log log=LogFactory.getLog(SmsStartThread.class);
	/**
	 * 
	 * @param startSmsTheads 是否启动状态监测和邮件监测线程
	 */
	public SmsStartThread(boolean startSmsTheads)
	{
		this.startSmsTheads=startSmsTheads;
		this.setName("SmsStart");
	}
	public void run()
	{
		try
		{
			log.info("<SMS>: SMS is starting...");			
			if(SmsController.startSms(true)==false)
			{
				log.error("<SMS>: Start SMS failed.");
				return;
			}
			
		}
		catch(Exception e)
		{
			log.error("<SMS>:Exception Start SMS failed - "+e.getMessage());
		}
		
	}
}
