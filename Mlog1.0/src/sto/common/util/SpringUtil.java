package sto.common.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;


/**
 * @category 获取spring中bean
 * @author Administrator
 * 
 */
public class SpringUtil extends HttpServlet {

	private static XmlWebApplicationContext o;
	private static Properties props;

	public void init() throws ServletException {
		ServletConfig config = getServletConfig();
		ServletContext sc = config.getServletContext();
		o = (XmlWebApplicationContext) sc
				.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
		// 初始化配置文件
		Resource resource = new ClassPathResource(sc.getContextPath()
				+ "/config/config.properties");
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @category 初始化
	 * @param request
	 * @throws IOException
	 */
	public static void init(HttpServletRequest request) throws IOException {
		ServletContext sc = request.getSession().getServletContext();
		if (o != null) {
			o = (XmlWebApplicationContext) sc
					.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
		}

		// 初始化配置文件
		Resource resource = new ClassPathResource(sc.getContextPath()
				+ "/config/config.properties");
		props = PropertiesLoaderUtils.loadProperties(resource);
	}

	/**
	 * @category 获取bean
	 * @param request
	 */
	public static Object getBean(String name) {
		return o.getBean(name);
	}

	public static Object getBean(String name, HttpServletRequest request) {
		if (o != null) {
			o = (XmlWebApplicationContext) request
					.getSession()
					.getServletContext()
					.getAttribute(
							"org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
		}
		return o.getBean(name);
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

}
