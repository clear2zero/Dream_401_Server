/**   
 * @Title: CAUsernamePasswordToken.java 
 * @Package com.tx.service.account 
 * @author chenxiaojia  
 * @date 2014-3-22 下午2:45:49 
 * @version V1.0   
 */
package sto.service.account;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @ClassName: CAUsernamePasswordToken
 * @Description:TOCKEN
 * @author chenxiaojia
 * @date 2014-3-22 下午2:45:49
 * 
 */
public class CaUsernamePasswordToken extends UsernamePasswordToken {

	private String useCertLogin;
	private String authMode;
	private String caMode;
	private String cryptCert;
	private String signCert;
	private String signature;
	private String keyType;
	private String loginRandom;
	private String server;
	private String deviceType;
	private String platform;
	


	public CaUsernamePasswordToken(String username,String host, String useCertLogin,
			String authMode, String caMode, String cryptCert, String signCert,
			String signature, String keyType, String loginRandom,
			String server,  String deviceType,
			String platform) {
		super(username, "", false, host);
		this.useCertLogin = useCertLogin;
		this.authMode = authMode;
		this.caMode = caMode;
		this.cryptCert = cryptCert;
		this.signCert = signCert;
		this.signature = signature;
		this.keyType = keyType;
		this.loginRandom = loginRandom;
		this.server = server;
		this.deviceType = deviceType;
		this.platform = platform;
		

	}

	public String getUseCertLogin() {
		return useCertLogin;
	}

	public void setUseCertLogin(String useCertLogin) {
		this.useCertLogin = useCertLogin;
	}

	public String getAuthMode() {
		return authMode;
	}

	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	public String getCaMode() {
		return caMode;
	}

	public void setCaMode(String caMode) {
		this.caMode = caMode;
	}

	public String getCryptCert() {
		return cryptCert;
	}

	public void setCryptCert(String cryptCert) {
		this.cryptCert = cryptCert;
	}

	public String getSignCert() {
		return signCert;
	}

	public void setSignCert(String signCert) {
		this.signCert = signCert;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getLoginRandom() {
		return loginRandom;
	}

	public void setLoginRandom(String loginRandom) {
		this.loginRandom = loginRandom;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

}
