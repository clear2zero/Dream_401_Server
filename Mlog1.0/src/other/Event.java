package other;

import java.util.ArrayList;
import java.util.List;

/**
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014年9月3日 上午10:04:44 <br/>
 * 
 * @author shenqn
 * @version 1.0 Copyright (c) 2014, 河北腾翔软件科技有限公司 All Rights Reserved.
 */
public class Event {
	private List<Object> params = new ArrayList<Object>();
	private EventKey key;

	public Event(EventKey key) {
		this.key = key;
	}

	public static enum EventKey {
		updateLastLogTime, updateUserSettings, updateUserFolder, updateUserCert
	}

	// 用户事件
	public static class UserEvent extends Event {

		public UserEvent(String user, EventKey key) {
			super(key);
			this.getParams().add(user);
		}

		public String getUser() {
			return (String) this.getParams().get(0);
		}
	}

	// 证书事件
	public static class CertEvent extends Event {
		public CertEvent(String certId, EventKey key) {
			super(key);
			this.getParams().add(certId);
		}

		public String getCertId() {
			return (String) this.getParams().get(0);
		}
	}

	// 单位事件
	public static class UnitEvent extends Event {
		public UnitEvent(long uid, EventKey key) {
			super(key);
			this.getParams().add(uid);
		}

		public long getUid() {
			return (Long) this.getParams().get(0);
		}
	}

	public List<Object> getParams() {
		return params;
	}

	public EventKey getKey() {
		return key;
	}

	public void setKey(EventKey key) {
		this.key = key;
	}

}
