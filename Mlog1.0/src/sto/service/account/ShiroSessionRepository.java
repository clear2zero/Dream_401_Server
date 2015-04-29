/**   
 * @Title: ShiroSessionRepository.java 
 * @Package sto.service.account 
 * @author chenxiaojia  
 * @date 2014-7-31 上午9:22:10 
 * @version V1.0   
 */
package sto.service.account;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * @ClassName: ShiroSessionRepository
 * @Description:
 * @author chenxiaojia
 * @date 2014-7-31 上午9:22:10
 * 
 */
public interface ShiroSessionRepository {
	void saveSession(Session session);

	void deleteSession(Serializable sessionId);

	Session getSession(Serializable sessionId);

}
