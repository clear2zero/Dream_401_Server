package sto.common.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hebca.sms.SmsDeviceStatus;
import com.hebca.sms.SmsProvider;

/**
 * 短信设备状态检测线程
 * 
 * @company HEBCA
 * @author xiaojia
 * @date 2012-2-3上午09:09:53
 * @comment:
 */
public class SmsDeviceStatusCheckThread extends Thread {
	private Log log = LogFactory.getLog(SmsDeviceStatusCheckThread.class);
	private boolean colseFlag = false;

	public SmsDeviceStatusCheckThread() {
		this.setName("SmsDeviceStatusCheck");
	}

	public boolean isColseFlag() {
		return colseFlag;
	}

	public void setColseFlag(boolean colseFlag) {
		this.colseFlag = colseFlag;
	}

	public void run() {
		try {
			int count = 0;
			while (true) {
				if (this.colseFlag) {
					log.info("<SMS>: SMS Device Status Check Thread has stoped.");
					return;
				}
				if (count != 0)
					Thread.sleep(30 * 1000);
				count = 1;
				try {
					SmsProvider smsProvider = SmsController.getSmsProvider();
					if (smsProvider != null) {
						SmsController.setDeviceStatus(smsProvider
								.getDeviceStatus());
					} else {
						SmsController.setDeviceStatus(new SmsDeviceStatus());
					}

					log.debug("<SMS>: signal Level "
							+ SmsController.getDeviceStatus().getSignalLevel());

				} catch (Exception e) {
					log.debug("<SMS:Exception>:设置短信状态发生错误" + e.getMessage());
				}
			}
		} catch (Exception e) {
			log.debug("<SMS>:状态检测线程发生错误已退出,错误原因:" + e.getMessage());
			return;
		}
	}
}
