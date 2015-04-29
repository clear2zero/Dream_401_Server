/**   
* @Title: LoginException.java 
* @Package sto.common.exception 
* @author chenxiaojia  
* @date 2014-8-28 下午4:57:56 
* @version V1.0   
*/ 
package sto.common.exception;

import org.apache.shiro.authc.AuthenticationException;

/** 
 * @ClassName: LoginException 
 * @Description: 
 * @author chenxiaojia
 * @date 2014-8-28 下午4:57:56 
 *  
 */
public class LoginException extends AuthenticationException {

	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p>  
	 */
	public LoginException(String msg) {
		super(msg);
	}
	
}
