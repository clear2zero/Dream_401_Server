package other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import other.Event.CertEvent;
import other.Event.EventKey;
import other.Event.UserEvent;

/**
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014年8月27日 上午11:03:36 <br/>
 * 
 * @author shenqn
 * @version 1.0 Copyright (c) 2014, 河北腾翔软件科技有限公司 All Rights Reserved.
 */
public class Finder {
	private static Log log = LogFactory.getLog(Finder.class);

	public static Object find(Key k, String v, NoResultQuery result) {
		try {
			if (!CacheManagerTX.open) {
				return result.query();
			}
			long start=System.currentTimeMillis();
			String ky = k + v;
			
			Object value = CacheManagerTX.getInstance().get(ky);
			if (value == null) {
				value = result.query();
				KV kv = new KV();
				kv.setValue(value);
				CacheManagerTX.getInstance().set(ky, kv);

			} else {
				value = ((KV) value).getValue();
				if(value==null)
					return result.query();
				log.debug("*read Cache key["+(System.currentTimeMillis()-start)+" ms]*" + ky);
			}
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 解决缓存值不能设置为空的问题
	public static class KV implements Serializable {
		private static final long serialVersionUID = 3560310136604724493L;
		private Object value;

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}

	private static final Map<EventKey, List<Key>> eventKey = new HashMap<EventKey, List<Key>>();

	public static enum Key {
		// 最后登录时间(登录时更新)
		lastLogTime(
				"$user$org.claros.groupware.admin.controllers.LogController.getLastLog."),
		// 获取用户
		getUserCertByCertId(
				"$certId$org.claros.groupware.preferences.controllers.UserCertController.getUserCertByCertId."),
		// 获取用户设置
		getUserSettings(
				"$user$org.claros.groupware.preferences.controllers.UserPrefsController.getUserSettings."),
		// 获取单位信息
		getUnitInfo(
				"$unit$org.claros.groupware.admin.controllers.AdminService.getUnitInfo."),
		// 获取部门
		getDeparts(
				"$user$org.claros.groupware.admin.controllers.AdminService.getDeparts."),
		// 获取模板
		getTemplateByUid(
				"$unit$org.claros.groupware.admin.controllers.TemplateController.getTemplateByUid."),
		// 获取用户证书
		getUserCertsByUser(
				"$user$org.claros.groupware.preferences.controllers.UserCertController.getUserCertsByUser."),
		// 获取用户所有文件夹
		getFs(
				"$user$org.claros.groupware.webmail.controllers.DbFolderControllerImpl.getFs."),
		// 获取用户自定义群发
		getTpRecord(
				"$user$org.claros.groupware.template.actions.TemPlateAction.getTpRecord."),
		//获取用户当前签名图片
		getCurrentSeal("$user$org.claros.groupware.admin.controllers.SealController.getCurrentSeal."),
		//获取用户名片
		getECards("$user$org.claros.groupware.ecard.controllers.ECardController.getAuthInusedECards."),
		//获取单位策略
		getPolicy("$unit$org.claros.groupware.webmail.utility.MailUtil.getPolicy."),
		//获取用户自定义邮件过滤器
		getFilters("$user$org.claros.groupware.filters.controllers.FilterController.getFilters.");
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private Key(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.getName();
		}

	}

	static {
		// 登录事件引起的需要更新的cache
		List<Key> loginKey = new ArrayList<Key>();
		loginKey.add(Key.lastLogTime);
		eventKey.put(EventKey.updateLastLogTime, loginKey);
		
		//更新用户设置表
		List<Key> updateUserSetting=new ArrayList<Key>();
		updateUserSetting.add(Key.getUserSettings);
		eventKey.put(EventKey.updateUserSettings, updateUserSetting);
		
		//更新用户文件夹
		List<Key> updateUserFolder=new ArrayList<Key>();
		updateUserFolder.add(Key.getFs);
		eventKey.put(EventKey.updateUserFolder, updateUserFolder);
		//更新用户证书
		List<Key> updateUserCert=new ArrayList<Key>();
		updateUserCert.add(Key.getUserCertByCertId);
		eventKey.put(EventKey.updateUserCert, updateUserCert);
	}

	public static void fire(Event e) {
		EventKey ek=e.getKey();
		List<Key> keys=eventKey.get(ek);
		String appendStr="";
		if(e instanceof UserEvent){
			UserEvent ue=(UserEvent)e;
			appendStr=ue.getUser();
		}else if(e instanceof CertEvent){
			CertEvent ce=(CertEvent)e;
			appendStr=ce.getCertId();
		}
		
		for(Key key:keys){
			String sk=key.getName()+appendStr;
			try {
				CacheManagerTX.getInstance().del(sk);
			} catch (Exception e1) {
				log.error(e1);
			}
			log.debug("*delete Cache key*" + sk);
		}
	}
}
