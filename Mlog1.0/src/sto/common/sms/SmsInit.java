package sto.common.sms;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 短信初始化入口
 *@company HEBCA 
 *@author xiaojia
 *@date 2012-2-3上午08:29:02
 *@comment:
 */
public class SmsInit extends HttpServlet{
	private Log log=LogFactory.getLog(SmsInit.class);
	/**
	 * 是否初始化
	 */
	private static boolean inited=false;
	private static Object lock=new Object();
	
	public void init() throws ServletException {
		initSMS();
		
	}

	
	/**
	 * 初始化SMS
	 */
	private void initSMS() {
		
		synchronized (lock) {
			if(inited==false)   //make sure sms thread only start once
			{
				log.info("<SMS>: SMS model is enable.");
				SmsController.startSmsThreads(true);
				inited=true;
			}
		}
	}
	
}


