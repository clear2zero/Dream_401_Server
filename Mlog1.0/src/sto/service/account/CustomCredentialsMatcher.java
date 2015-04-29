/**   
* @Title: CustomCredentialsMatcher.java 
* @Package sto.service 
* @author chenxiaojia  
* @date 2014-7-25 下午6:36:04 
* @version V1.0   
*/ 
package sto.service.account;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/** 
 * @ClassName: CustomCredentialsMatcher 
 * @Description: CA认证
 * @author chenxiaojia
 * @date 2014-7-25 下午6:36:04 
 *  
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {  

		return true;
	}
}