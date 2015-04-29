/**   
 * @Title: MemcachedManager.java 
 * @Package com.hebca.cache.memcached 
 * @author chenxiaojia  
 * @date 2014-3-17 上午10:04:48 
 * @version V1.0   
 */
package other;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * @ClassName: MemcachedManager
 * @Description:
 * @author chenxiaojia
 * @date 2014-3-17 上午10:04:48
 * 
 */
public class MemcachedManager implements CacheManagerTXI{

	/** 是否开启memcached */
	private boolean openCache = true;

	/** 过期时间（单位是秒） */
	public int exp = 60 * 60 * 2;

	/** Memcached cache */
	public static MemcachedClient cache;

	public MemcachedManager(){
		//isOpen();
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setOpenCache(boolean openCache) {
		this.openCache = openCache;
	}

	/**
	 * 
	 * @Description: 如果没有则插入,不替换
	 * @param @param key
	 * @param @param o
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return OperationFuture<Boolean> 返回类型
	 * @throws
	 */
	public void add(String key, Object o)
			throws Exception {
		if (openCache)
			getCache().add(key, exp, o);
	}
	
	public void del(String key) throws Exception {
		remove(key);
	}
	/**
	 * 
	 * @category:(这里用一句话描述这个方法的作用). <br/> 
	 * 
	 * @author shenqn 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public void remove(String key)
			throws Exception {
		if (openCache)
			getCache().delete(key);
	}

	/**
	 * 
	 * @Description: 如果没有则插入,不替换
	 * @param @param key
	 * @param @param exp
	 * @param @param o
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return OperationFuture<Boolean> 返回类型
	 * @throws
	 */
	public void add(String key, int exp, Object o)
			throws Exception {
		if (openCache)
			getCache().add(key, exp, o);
	}

	/**
	 * 
	 * @Description: 只替换，不新增
	 * @param @param key
	 * @param @param exp 过期时间（单位是秒）
	 * @param @param o
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return OperationFuture<Boolean> 返回类型
	 * @throws
	 */
	public void replace(String key, int exp, Object o)
			throws Exception {
		if (openCache)
			getCache().replace(key, exp, o);
	}

	/**
	 * 
	 * @Description:只替换，不新增
	 * @param @param key
	 * @param @param o
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return OperationFuture<Boolean> 返回类型
	 * @throws
	 */
	public void replace(String key, Object o)
			throws Exception {
		if (openCache)
			getCache().replace(key, exp, o);
	}

	/**
	 * 
	 * @Description:设置缓冲 如果没有则插入，如果有则修改
	 * @param @param key
	 * @param @param object
	 * @param @throws Exception 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void set(String key, Object object)
			throws Exception {
		if (openCache)
			getCache().set(key, exp, object);
	}

	/**
	 * 
	 * @Description:设置缓冲 如果没有则插入，如果有则修改
	 * @param @param key
	 * @param @param exp 过期时间（单位是秒）
	 * @param @param object
	 * @param @throws Exception 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void set(String key, int exp,
			Object object) throws Exception {
		if (openCache)
			getCache().set(key, exp, object);
	}

	/**
	 * 获取缓冲
	 * 
	 * @Description:
	 * @param @param key
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	public Object get(String key) throws Exception {
		if (!openCache)
			return null;
		return getCache().get(key);
	}

	/**
	 * @Description: 是否开启memcached
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private boolean isOpen()  {
		String open = "false";
		try {
			open = "true";//"mailbox.memcached.open";
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("true".equals(open))
			openCache = true;
		else
			openCache = false;
		return openCache;
	}

	public boolean isOpenCache() {
		return openCache;
	}
	/**
	 * 创建一个新的实例，用完了需要调用cache.shutdown()关闭
	 * 
	 * @Description:
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return MemcachedClient 返回类型
	 * @throws
	 */
	public static MemcachedClient createNew() throws Exception {
		String ips = "192.168.3.107";//"mailbox.memcached.ips";
		String port = "10000";//"mailbox.memcached.port";
		String[] ipa = ips.split(",");
		InetSocketAddress[] isas = new InetSocketAddress[ipa.length];
		for (int i = 0; i < ipa.length; i++) {
			isas[i] = new InetSocketAddress(ipa[i], Integer.parseInt(port));
		}
		return new MemcachedClient(isas);
	}

	/**
	 * 获取单态的实例
	 * 
	 * @Description:
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return MemcachedClient 返回类型
	 * @throws
	 */
	public static MemcachedClient getCache() throws Exception {
		if (cache != null) {
			return cache;
		}
		synchronized (MemcachedManager.class) {
			if (cache == null) {
				String ips = "192.168.3.107";//"mailbox.memcached.ips";
				String port = "10000";//"mailbox.memcached.port";
				String[] ipa = ips.split(",");
				InetSocketAddress[] isas = new InetSocketAddress[ipa.length];
				for (int i = 0; i < ipa.length; i++) {
					isas[i] = new InetSocketAddress(ipa[i],
							Integer.parseInt(port));
				}
				cache = new MemcachedClient(isas);
			}
		}

		return cache;
	}

	/**
	 * 
	 * @Description:测试用
	 * @param @param args
	 * @param @throws IOException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) throws IOException {

		MemcachedClient cache = new MemcachedClient(new InetSocketAddress(
				"192.168.15.51", 10000));
//		UserCert user = new UserCert();
//		user.setId((long) 6);
//		user.setCertId("xj6");
//		cache.set("T00016", 3600, user);
//		UserCert myObject = (UserCert) cache.get("T00016");
//		System.out.println("Get object from mem :" + myObject);

//		String appid = (String) cache.get("org.claros.groupware.admin.controllers.TemplateController.getTemplateTitleAndId.11000000");
//		System.out.println(appid);
		cache.delete("org.claros.groupware.admin.controllers.TemplateController.getTemplateTitleAndId.11000000");
		cache.shutdown();
	}

}
