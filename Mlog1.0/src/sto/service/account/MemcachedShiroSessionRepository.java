/**   
 * @Title: MemcachedShiroSessionRepository.java 
 * @Package sto.service.account 
 * @author chenxiaojia  
 * @date 2014-7-31 下午2:07:05 
 * @version V1.0   
 */
package sto.service.account;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;

/**
 * @ClassName: MemcachedShiroSessionRepository
 * @Description:
 * @author chenxiaojia
 * @date 2014-7-31 下午2:07:05
 * 
 */
public class MemcachedShiroSessionRepository implements ShiroSessionRepository {
	private Log log = LogFactory.getLog(MemcachedShiroSessionRepository.class);

	@Override
	public void saveSession(Session session) {
		if (session == null || session.getId() == null) {
			log.error("session或者session id为空");
			return;
		}
		String key = session.getId().toString();
		/*try {
			CacheManagerTX.getInstance().set(key, session);
		} catch (Exception e) {
			log.error("session保存失败");
			e.printStackTrace();
		}*/
	}

	@Override
	public void deleteSession(Serializable sessionId) {
		if (sessionId == null) {
			log.error("id为空");
			return;
		}
		/*try {
			CacheManagerTX.getInstance().del(sessionId.toString());
		} catch (Exception e) {
			log.error("删除session失败");
			e.printStackTrace();
		}*/
	}

	@Override
	public Session getSession(Serializable sessionId) {
		if (sessionId == null) {
			log.error("id为空");
			return null;
		}
		Session s = null;
		String key = sessionId.toString();
		/*try {
			Object obj = CacheManagerTX.getInstance().get(key);
			System.out.println("get cache:" + key);
			if (obj != null)
				s = (Session) obj;
		} catch (Exception e) {
			log.error("读取session失败，id：" + sessionId);
			e.printStackTrace();
		}*/
		return s;
	}

}
