/**   
 * @Title: CustomShiroSessionDAO.java 
 * @Package sto.service.account 
 * @author chenxiaojia  
 * @date 2014-7-31 上午9:19:52 
 * @version V1.0   
 */
package sto.service.account;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

/**
 * @ClassName: CustomShiroSessionDAO
 * @Description:
 * @author chenxiaojia
 * @date 2014-7-31 上午9:19:52
 * 
 */
public class CustomShiroSessionDAO extends AbstractSessionDAO {
	private Log log = LogFactory.getLog(CustomShiroSessionDAO.class);
	private ShiroSessionRepository shiroSessionRepository;

	public ShiroSessionRepository getShiroSessionRepository() {
		return shiroSessionRepository;
	}

	public void setShiroSessionRepository(
			ShiroSessionRepository shiroSessionRepository) {
		this.shiroSessionRepository = shiroSessionRepository;
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		getShiroSessionRepository().saveSession(session);
	}

	@Override
	public void delete(Session session) {
		if (session == null) {
			log.error("session can not be null,delete failed");
			return;
		}
		Serializable id = session.getId();
		if (id != null)
			getShiroSessionRepository().deleteSession(id);

	}

	@Override
	public Collection<Session> getActiveSessions() {
		return Collections.EMPTY_SET;
	}


	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		getShiroSessionRepository().saveSession(session);
		return sessionId;
	}


	@Override
	protected Session doReadSession(Serializable sessionId) {
		return getShiroSessionRepository().getSession(sessionId);
	}

}
