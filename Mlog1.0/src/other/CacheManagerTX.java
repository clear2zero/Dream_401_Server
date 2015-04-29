/**   
 * @Title: CacheManagerTX.java 
 * @Package com.hebca.cache.memcached 
 * @author chenxiaojia  
 * @date 2014-8-1 上午9:51:21 
 * @version V1.0   
 */
package other;

/**
 * @ClassName: CacheManagerTX
 * @Description:
 * @author chenxiaojia
 * @date 2014-8-1 上午9:51:21
 * 
 */
public class CacheManagerTX {

	CacheManagerTXI cache;
	public static boolean open = false;
	
	
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p>  
	 */
	public CacheManagerTX() {
		if(cache==null){
			cache = new MemcachedManager();
		} 
	}

	public boolean hasCache() {
		if (cache == null) {
			return false;
		}
		{
			return true;
		}
	}

	public CacheManagerTXI getCache() {
		return cache;
	}

	public void setCache(CacheManagerTXI cache) {
		this.cache = cache;
	}

	public static CacheManagerTX getInstance() {
		return new CacheManagerTX();
	}

	public void del(String key) throws Exception {
		if (hasCache())
			cache.del(key);

	}

	public void add(String key, Object o) throws Exception {
		if (hasCache())
			cache.add(key, o);

	}

	public void add(String key, int exp, Object o) throws Exception {
		if (hasCache())
			cache.add(key, o);

	}

	public void replace(String key, int exp, Object o) throws Exception {
		if (hasCache())
			cache.replace(key, o);

	}

	public void replace(String key, Object o) throws Exception {
		if (hasCache())
			cache.replace(key, o);

	}

	public void set(String key, Object object) throws Exception {
		if (hasCache())
			cache.set(key, object);

	}

	public void set(String key, int exp, Object object) throws Exception {
		if (hasCache())
			cache.set(key, exp, object);
	}

	public Object get(String key) throws Exception {
		if (hasCache())
			return cache.get(key);
		return null;
	}

}
