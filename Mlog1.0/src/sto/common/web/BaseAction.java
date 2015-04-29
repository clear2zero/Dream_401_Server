/**   
* @Title: BaseAction.java 
* @Package com.tx.web.common 
* @author chenxiaojia  
* @date 2014-3-21 下午10:45:41 
* @version V1.0   
*/ 
package sto.common.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import other.AuthProfile;

/** 
 * @ClassName: BaseAction 
 * @Description: 
 * @author chenxiaojia
 * @date 2014-3-21 下午10:45:41 
 *  
 */
public class BaseAction {
	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session; 
    
    /**
     * 
    * @Description: ModelAttribute的作用 
    * 	1)放置在方法的形参上：表示引用Model中的数据
	*	2)放置在方法上面：表示请求该类的每个Action前都会首先执行它，也可以将一些准备数据的操作放置在该方法里面。
    * @param @param request
    * @param @param response    设定文件 
    * @return void    返回类型 
    * @throws
     */
    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;
    	//this.request = org.hebca.base.action.BaseAction.getRequest();
        this.response = response;  
        this.session = request.getSession();  
    }  

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public AuthProfile getCurrentUser() {
		AuthProfile user = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
    
	public Object getVariable(HttpServletRequest request, String name) {
		Object obj = request.getParameter(name);
		if (obj == null) {
			obj = request.getAttribute(name);
			if (obj == null) {
			    obj = request.getSession().getAttribute(name);
			    if (obj == null) {
			        obj = getCookie(request, name);			       
			    }
			}
		}
		return obj;
	}
	
	public String getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		Cookie cookie = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
