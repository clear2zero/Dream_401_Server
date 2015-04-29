package other;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 监听器，监听Session的创建和删除等，维持在线用户列表
 * @version 	1.0
 * @author		Umut G�kbayrak
 */
public class SessionListener implements HttpSessionListener {
	private static int onlineUsersCount = 0;
	private static HashMap onlineUsers = new HashMap();
	private static Log log = LogFactory.getLog(SessionListener.class);
	private static long maxOnlineUser  =  0;

	public void sessionCreated(HttpSessionEvent arg0) {
		onlineUsersCount++;
	}
	
	public static void addOnlineUser(String username, String sessionId) {
		if(onlineUsers.size()>maxOnlineUser){
			maxOnlineUser = onlineUsers.size();
		}
		onlineUsers.put(username, sessionId);
		onlineUsersCount++;
	}
	

	public static long getMaxOnlineUser() {
		return maxOnlineUser;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		onlineUsersCount--;
		
		//remove from list
		AuthProfile auth = (AuthProfile)arg0.getSession().getAttribute("auth");
		if (auth != null) {
			log.debug("用户"+auth.getUsername()+"session销毁,用户退出");
			onlineUsers.remove(auth.getUsername());
		}else{
			log.debug("用户session已经销毁");
		}		
		//close chat
		HttpSession sess = (HttpSession)arg0.getSession();
	}
	
	public static String getUserBySessionId(String sessionId) {
		Iterator iter = onlineUsers.keySet().iterator();
		String tmp = null;
		while (iter.hasNext()) {
			tmp = (String)iter.next();
			if (onlineUsers.get(tmp).equals(sessionId)) {
				return tmp;
			}
		}
		return null;
	}

	/**
	 * @return the onlineUsersCount
	 */
	public static int getOnlineUsersCount() {
		return onlineUsersCount;
	}

	/**
	 * @return the onlineUsers
	 */
	public static HashMap getOnlineUsers() {
		return onlineUsers;
	}
}
