/**   
 * @Title: AuthenCAFilter.java 
 * @Package com.tx.service.account 
 * @author chenxiaojia  
 * @date 2014-3-22 下午2:44:35 
 * @version V1.0   
 */
package sto.service.account;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import other.SessionControllers;

/**
 * @ClassName: AuthenCAFilter
 * @Description:获取表单提交数据并保存到TOKEN中
 * @author chenxiaojia
 * @date 2014-3-22 下午2:44:35
 * 
 */
public class CaFormAuthenticationFilter extends FormAuthenticationFilter {
	private static final Logger LOG = LoggerFactory
			.getLogger(CaFormAuthenticationFilter.class);
	public static final String DEFAULT_CertLogin_PARAM ="useCertLogin";
	public static final String DEFAULT_AUTHMODE_PARAM ="authMode";
	public static final String DEFAULT_CAMODE_PARAM ="caMode";
	public static final String DEFAULT_CRYPTCERT_PARAM = "cryptCert";
	public static final String DEFAULT_SIGNCERT_PARAM = "signCert";
	public static final String DEFAULT_SIGNDATA_PARAM = "signature";
	public static final String DEFAULT_KEYTYPE_PARAM = "keyType";
	public static final String DEFAULT_LOGINRANDOM_PARAM = "loginRandom";
	public static final String DEFAULT_SERVER_PARAM = "server";
	public static final String DEFAULT_PLATFORM_PARAM = "platForm";
	public static final String DEFAULT_DEVICETYPE_PARAM = "deviceType";

	private String certLoginParam = DEFAULT_CertLogin_PARAM;
	private String authModeParam = DEFAULT_AUTHMODE_PARAM;
	private String caModeParam = DEFAULT_CAMODE_PARAM;
	private String cryptcertParam = DEFAULT_CRYPTCERT_PARAM;
	private String signcertParam = DEFAULT_SIGNCERT_PARAM;
	private String signdataParam = DEFAULT_SIGNDATA_PARAM;
	private String keyTypeParam = DEFAULT_KEYTYPE_PARAM;
	private String loginRandomParam = DEFAULT_LOGINRANDOM_PARAM;
	private String serverParam = DEFAULT_SERVER_PARAM;
	private String platFormParam = DEFAULT_PLATFORM_PARAM;
	private String deviceTyperParam = DEFAULT_DEVICETYPE_PARAM;
	

	public CaFormAuthenticationFilter() {

	}

	// 保存异常对象到request
	@Override
	protected void setFailureAttribute(ServletRequest request,
			AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), ae.getMessage());
	}

	@Override
	protected CaUsernamePasswordToken createToken(ServletRequest request,
			ServletResponse response) {
		
		String useCertLogin = WebUtils.getCleanParam(request, getCertLoginParam());
		String authMode = WebUtils.getCleanParam(request, getAuthModeParam());
		String caMode = WebUtils.getCleanParam(request, getCaModeParam());
		String signCert = getSignCert(request);
		String cryptCert = getCryptCert(request);
		String signature = getSigndata(request);
		String keyType = WebUtils.getCleanParam(request,getKeyTypeParam());
		String loginRandom = "";
		String server = request.getServerName();
		String platForm = WebUtils.getCleanParam(request,getPlatFormParam() );
		String deviceType = WebUtils.getCleanParam(request, getDeviceTyperParam());
		String host = getHost(request);
		String username = getUsername(request);
		HttpServletRequest requesth = null;
		if (request instanceof HttpServletRequest) {
			requesth = (HttpServletRequest) request;
			Object o = SessionControllers.get(requesth, SessionControllers.LoginRandom);
			if (o != null){
				loginRandom = (String) o;
			}
		}
		if(StringUtils.isBlank(loginRandom)){
			return null;
		}
		return new CaUsernamePasswordToken(username,host, useCertLogin, authMode, caMode, cryptCert,
				signCert, signature,keyType,loginRandom,server,deviceType,platForm);

	}

	@Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
            throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		//统一跳转到一个页面：/account/loginEnd.action
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+ getSuccessUrl()); 
		//跳转到用户登录前打开的页面
		//WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
		return false;
	}
	@Override
	protected boolean executeLogin(ServletRequest request,
			 ServletResponse response) throws Exception {
		CaUsernamePasswordToken token = createToken(request, response);
		if(token != null){
			try {
				Subject subject = getSubject(request, response);
				subject.login(token);

				return onLoginSuccess(token, subject, request, response);
			} catch (AuthenticationException e) {
				e.printStackTrace();
				return onLoginFailure(token, e, request, response);
		 	}
		}else {
			return onLoginFailure(token, new AuthenticationException("身份验证失败"), request, response);
		}
		
	 }
	public String getPlatFormParam() {
		return platFormParam;
	}

	public void setPlatFormParam(String platFormParam) {
		this.platFormParam = platFormParam;
	}

	public String getDeviceTyperParam() {
		return deviceTyperParam;
	}

	public void setDeviceTyperParam(String deviceTyperParam) {
		this.deviceTyperParam = deviceTyperParam;
	}

	public String getCryptcertParam() {
		return cryptcertParam;
	}

	public void setCryptcertParam(String cryptcertParam) {
		this.cryptcertParam = cryptcertParam;
	}

	public String getSigncertParam() {
		return signcertParam;
	}

	public void setSigncertParam(String signcertParam) {
		this.signcertParam = signcertParam;
	}

	

	public String getCertLoginParam() {
		return certLoginParam;
	}

	public void setCertLoginParam(String certLoginParam) {
		this.certLoginParam = certLoginParam;
	}

	public String getAuthModeParam() {
		return authModeParam;
	}

	public void setAuthModeParam(String authModeParam) {
		this.authModeParam = authModeParam;
	}

	public String getCaModeParam() {
		return caModeParam;
	}

	public void setCaModeParam(String caModeParam) {
		this.caModeParam = caModeParam;
	}

	public String getKeyTypeParam() {
		return keyTypeParam;
	}

	public void setKeyTypeParam(String keyTypeParam) {
		this.keyTypeParam = keyTypeParam;
	}

	public String getLoginRandomParam() {
		return loginRandomParam;
	}

	public void setLoginRandomParam(String loginRandomParam) {
		this.loginRandomParam = loginRandomParam;
	}

	public String getServerParam() {
		return serverParam;
	}

	public void setServerParam(String serverParam) {
		this.serverParam = serverParam;
	}

	public String getSigndataParam() {
		return signdataParam;
	}

	public void setSigndataParam(String signdataParam) {
		this.signdataParam = signdataParam;
	}


	protected String getSignCert(ServletRequest request) {
		return WebUtils.getCleanParam(request, getSigncertParam());
	}

	protected String getCryptCert(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCryptcertParam());
	}

	protected String getSigndata(ServletRequest request) {
		return WebUtils.getCleanParam(request, getSigndataParam());
	}

}
