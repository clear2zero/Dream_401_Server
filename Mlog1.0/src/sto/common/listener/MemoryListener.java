package sto.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MemoryListener implements ServletContextListener{
	
	/**
	 * 服务器将要关闭时，ServletContextListener 的 contextDestroyed()方法被调用，
	 * 所以在里面保存缓存的更改。将更改后的缓存保存回文件或者数据库，更新原来的内容。
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println(">>>>>初始化数据字典到内存完成");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println(">>>>>初始化数据字典到内存");
	}

}
