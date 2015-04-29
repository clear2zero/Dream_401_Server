/**   
 * @Title: URLPermissionsFilter.java 
 * @Package com.tx.service.account 
 * @author chenxiaojia  
 * @date 2014-3-21 上午11:29:32 
 * @version V1.0   
 */
package sto.service.account;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

/**
 * 基于URL的权限判断过滤器
 * <p>
 * 我们自动根据URL产生所谓的权限字符串，这一项在Shiro示例中是写在配置文件里面的，默认认为权限不可动态配置
 * <p>
 * URL举例：/User/create.do?***=*** -->权限字符串：/User/create.do
 * 
 * @ClassName: URLPermissionsFilter
 * @Description:
 * @author chenxiaojia
 * @date 2014-3-21 上午11:29:32
 * 
 */
public class URLPermissionsFilter extends PermissionsAuthorizationFilter {
	/**
	 * @param mappedValue
	 *            指的是在声明url时指定的权限字符串，如/User/create.do=perms[User:create].
	 *            我们要动态产生这个权限字符串，所以这个配置对我们没用
	 */
	@Override
	public boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws IOException {
		// return super.isAccessAllowed(request, response, mappedValue);
		return super.isAccessAllowed(request, response,
				 buildPermissions(request,mappedValue));
	}

	/**
	 * 根据请求URL产生权限字符串，这里只产生，而比对的事交给Realm 
	* @Description: 
	* @param @param request
	* @param @return    设定文件 
	* @return String[]    返回类型 
	* @throws
	 */
	protected String[] buildPermissions(ServletRequest request,Object obj) {
		
		if(obj!=null && obj instanceof String[] )
		{
			String [] str = (String [] )obj;
			if(str.length>0 && str[0].contains(":")){
				return str;
			}
		}
		
		String[] perms = new String[1];
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getServletPath();
		perms[0] = path;// path直接作为权限字符串
		
		/*
		 * String regex = "/(.*?)/(.*?)\\.(.*)"; 
		 * if(url.matches(regex)){ 
		 *   Pattern pattern = Pattern.compile(regex); 
		 *   Matcher matcher = pattern.matcher(url); 
		 *   String controller = matcher.group(1); 
		 *   String action = matcher.group(2);		 * 
		 * }
		 */
		return perms;
	}

}
