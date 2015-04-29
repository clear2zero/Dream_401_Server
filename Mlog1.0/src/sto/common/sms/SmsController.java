package sto.common.sms;


import javax.jws.WebParam.Mode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import com.hebca.sms.SmsDeviceStatus;
import com.hebca.sms.SmsProvider;
import com.hebca.sms.WebServiceSms.WebServiceSmsProvider;

public class SmsController {
	//private static Logger log = Logger.getLogger(RaMonitor.class);//临时监控下面class，只为了不报错
	private static Logger log = Logger.getLogger(SmsController.class);
	private static SmsProvider smsProvider;
	private static SmsDeviceStatus deviceStatus;
	public static SmsDeviceStatusCheckThread smsCheckThread=null;
	
	public static synchronized void startSmsThreads(boolean startSms){	
		if(startSms)
		{
			new SmsStartThread(true).start();
		}
		else
		{
			SmsController.smsCheckThread = new SmsDeviceStatusCheckThread();
			SmsController.smsCheckThread.start();
			log.info("<SMS>: SMS Device Status Check Thread has started.");
			
		}
	}
	
	public static synchronized void stopSmsThread(boolean stopSms){
		if(stopSms)
			stopSms();
		
		try
		{
			if(smsCheckThread!=null)
			{
				smsCheckThread.setColseFlag(true);
				smsCheckThread.interrupt();
				smsCheckThread=null;
				
			}
		}
		catch(Exception e)
		{
			smsCheckThread=null;
		}
		
	}
	/**
	 * 创建短信管理对象(provider)
	 * @return
	 * @throws Exception
	 */
	public static synchronized boolean startSms(boolean autoCheck)throws Exception{
		
		
		smsProvider = new WebServiceSmsProvider();
		
		return true;
	}
	
	public static boolean stopSms()
	{
		if(smsProvider==null)
		{
			return true;
		}
		try
		{
			smsProvider.stop();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static SmsProvider getSmsProvider() {
		return smsProvider;
	}


	public static SmsDeviceStatus getDeviceStatus() {
		return deviceStatus;
	}


	public static void setDeviceStatus(SmsDeviceStatus deviceStatus) {
		SmsController.deviceStatus = deviceStatus;
	}
}



