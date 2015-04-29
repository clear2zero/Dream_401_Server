/**   
 * @Title: ShiroCARealm.java 
 * @Package org.springside.examples.showcase.service 
 * @author chenxiaojia  
 * @date 2014-3-11 下午1:44:07 
 * @version V1.0   
 */
package sto.service.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import other.AuthMode;
import other.AuthProfile;
import other.ConnectionProfile;
import other.ConnectionProfileList;
import other.SessionControllers;
import other.WebMailAuth;
import sto.common.exception.LoginException;
import sto.dao.account.RoleModuleDao;
import sto.model.account.Module;
import sto.model.account.Role;
import sto.model.account.RoleModule;

import com.hebca.pki.Cert;
import com.hebca.pki.CertParse;

/**
 * @ClassName: ShiroCARealm
 * @Description:
 * @author chenxiaojia
 * @date 2014-3-11 下午1:44:07
 * 
 */
public class ShiroCARealm extends AuthorizingRealm {
	private Log log = LogFactory.getLog(ShiroCARealm.class);
	public final static String GIVENNAME = "givenName";
	
	@Autowired
	public RoleService roleService;
	@Resource
	RoleModuleDao roleModuleDao;
	@Resource
	ModuleButtonService moduleButtonService;
	/**
	 * 增加认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {
		CaUsernamePasswordToken userToken = (CaUsernamePasswordToken) arg0;	
		Session session = SecurityUtils.getSubject().getSession();
		String signcert = userToken.getSignCert();
		String encryptcert = userToken.getCryptCert();
		String certDN = "";
		AuthProfile auth = null;
		
		try{
			if (AuthMode.SignLogin.equals(userToken.getAuthMode())) {
				auth = this.SignAuth(userToken);//签名认证和差数据库
				session.setAttribute("userToken", userToken);
			} else {
				throw new LoginException("非法认证模式");
			}
			if(auth!=null){
				checkCertIsEnable(auth);
				ConnectionProfile profile = ConnectionProfileList
						.getProfileByShortName(userToken.getServer());
			}
			certDN = getCertIdentify(new CertParse(new Cert(signcert)));
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(auth,"", certDN);
			return info;
		}catch (LoginException e) {
			throw new LoginException(e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new LoginException("网络错误，请稍后重试");
		}
	}


	//@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * 增加权限Role permission
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		AuthProfile auth = (AuthProfile) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//加载操作权限，因为暂时没用到操作权限，先注释
		if (auth != null) {
			Role role = auth.getRole();
			if (role != null) {
				info.addRole(role.getEnname());
				List<RoleModule> roleModuleList = roleModuleDao.find("from RoleModule where role.id=" + role.getId());
				for(RoleModule rm : roleModuleList){
					Module m = rm.getModule();
					Map<String,Object> parammap = new HashMap<String,Object>();
					parammap.put("mid", m.getId());
					parammap.put("rid", role.getId());
					List<Map<String,Object>> list = moduleButtonService.getModuleButtonsAuth(parammap);
					for(Map<String,Object> map : list){
						if(map.get("checked") != null && String.valueOf(map.get("checked")).equals("1")){
							info.addStringPermission(m.getEnname()+":"+map.get("bename"));
						}
					}
				}
			}
			return info;
		}
		return null;
	}


	public static String getCertIdentify(CertParse parser) {
		String cn1 = parser.getSubject(CertParse.DN_CN);
		String cn2 = parser.getSubject(CertParse.DN_GIVENNAME);
		return cn1.length() > cn2.length() ? cn1 : cn2;
	}
	
	/**
	 * 判断签名和是否注册
	* @Description: 
	* @param @param bean
	* @param @return
	* @param @throws Exception    设定文件 
	* @return AuthProfile    返回类型 
	* @throws
	 */
	public static AuthProfile SignAuth(CaUsernamePasswordToken bean)throws Exception{
		String signCert = bean.getSignCert();
		String cryptCert = bean.getCryptCert();
		String signature = bean.getSignature();
		
		if (signCert == null || signCert.length() == 0 || cryptCert == null
				|| cryptCert.length() == 0 || signature == null
				|| signature.length() == 0) {
			throw new LoginException("缺少参数");
		}
		try {
			String random =  bean.getLoginRandom();	
			AuthProfile auth = WebMailAuth.authenticateInWebMailServerBySign(signCert, signature, random);
			/*WebMailAuth.authenticateInWebMailServerBySign(signCert, signature, random);	*/		
			auth.setAuthMode(bean.getAuthMode());
			auth.setCaMode(bean.getCaMode());
			return auth;
		} catch(Exception e){
			e.printStackTrace();
			throw new LoginException(e.getMessage());//提交用户数据不完整
		} finally {
			// 随机数不再需要保存
			//SessionControllers.remove(bean.getRequest(), SessionControllers.LoginRandom);
		}
		
	}
	
	
	public void checkCertIsEnable(AuthProfile auth)  {

		if (auth != null) {
			Boolean enable = auth.getEnabled();
			int status = auth.getStatus();
			// 由于读取时，null被读取成false ，以前老系统需要全部需要default 1
			if (true == enable) {
				log.debug("证书已被启用");
				if (status == -1) {
					throw new LoginException("您的账户已被冻结,请联系管理员");
				} else if (status == -2) {
					throw new LoginException("您还未通过管理员的审核");
				} else if (status == -3) {
					throw new LoginException("您没有通过管理员审核");
				}
			} else {
				log.debug("证书已被禁用");
				throw new LoginException("证书被禁用");
			}
		} else {
			throw new LoginException("服务器忙，请稍候重试");
		}
	}

}
