/**   
* @Title: CacheManager.java 
* @Package com.hebca.cache 
* @author chenxiaojia  
* @date 2014-8-1 上午9:32:28 
* @version V1.0   
*/ 
package other;

/** 
 * @ClassName: CacheManager 
 * @Description: 
 * @author chenxiaojia
 * @date 2014-8-1 上午9:32:28 
 *  
 */
public interface CacheManagerTXI {
	public void del(String key)throws Exception;
	public void add(String key, Object o) throws Exception;
	public void add(String key, int exp, Object o) throws Exception;
	public void replace(String key, int exp, Object o) throws Exception;
	public void replace(String key, Object o) throws Exception;
	public void set(String key, Object object) throws Exception;
	public void set(String key, int exp, Object object) throws Exception;
	public Object get(String key) throws Exception;
}
