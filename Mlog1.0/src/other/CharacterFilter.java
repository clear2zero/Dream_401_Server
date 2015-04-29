package other;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 全局过滤器，设置请求的编码为UTF-8
 * @version 	1.0
 * @author		echo
 * 
 */
public class CharacterFilter implements Filter {
	/**
	* @see javax.servlet.Filter#void ()
	*/
	public void destroy() {
		
	}

	/**
	* @see javax.servlet.Filter#void (javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	*/
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

		resp.setContentType("text/html; charset=utf-8");
		req.setCharacterEncoding("utf-8");
/*		if (nonSafeCharsExists(req)) {
			throw new ServletException();
		} else {
*/
			chain.doFilter(req, resp);
			req.setCharacterEncoding("utf-8");
/*		} */
	}


	/*
	private boolean nonSafeCharsExists(ServletRequest request) {
		Enumeration enum = request.getParameterNames();
		String name, val = null;

		while (enum.hasMoreElements()) {
			name = (String)enum.nextElement();
			val = request.getParameter(name);
			if (val.indexOf("^") >= 0 ||
//				val.indexOf("[") >= 0 ||
				val.indexOf("\\") >= 0 ||
//				val.indexOf("(") >= 0 ||
//				val.indexOf(")") >= 0 ||
				val.indexOf("<") >= 0 ||
				val.indexOf(">") >= 0 ||
				val.indexOf(",") >= 0 ||
				val.indexOf("'") >= 0 ||
				val.indexOf("\"") >= 0 ||
//				val.indexOf(";") >= 0 ||
//				val.indexOf(":") >= 0 ||
				val.indexOf("{") >= 0 ||
				val.indexOf("}") >= 0 ||
				val.indexOf("%") >= 0 ||
				val.indexOf("#") >= 0 ||
				val.indexOf("!") >= 0 ||
				val.indexOf("&") >= 0 ||
				val.indexOf("-") >= 0 ||
				val.indexOf("*") >= 0 ||
				val.indexOf("+") >= 0 ||
				val.indexOf("$") >= 0) {
				return true;
			}
		}
		return false;
	}
	*/

	/**
	* Method init.
	* @param config
	* @throws javax.servlet.ServletException
	*/
	public void init(FilterConfig config) throws ServletException {

	}

}
