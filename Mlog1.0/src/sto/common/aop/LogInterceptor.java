package sto.common.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import other.AuthProfile;
import sto.common.service.BaseServiceImpl;
import sto.common.util.StringUtils;
import sto.dao.account.OperateLogDao;
import sto.model.account.OperateLog;
import sto.utils.SpringContextHolder;

/**
 * 系统拦截器
 * @author ThinkGem
 * @version 2013-6-6
 */
public class LogInterceptor extends BaseServiceImpl<OperateLog> implements HandlerInterceptor {

	private static OperateLogDao operateLogDao = SpringContextHolder.getBean(OperateLogDao.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
//		if(modelAndView!=null) {
//			String viewName = modelAndView.getViewName();
//			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent")); 
//			if(viewName.startsWith("modules/") && DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType())){
//				modelAndView.setViewName(viewName.replaceFirst("modules", "mobile"));
//			}
//		}
	}

	@Override
	@Transactional(readOnly = false)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		
		String requestRri = request.getRequestURI();
//		String uriPrefix = request.getContextPath() + Global.getAdminPath();
//		
		if ((StringUtils.endsWith(requestRri, "/saveDo.action") || (StringUtils.endsWith(requestRri, "/updateDo.action")
				|| StringUtils.endsWith(requestRri, "/delete.action"))) || ex!=null){
		
			AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject()
					.getPrincipal();
			if (curruser!=null && curruser.getCertIdentify()!=null){
				
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
				
				OperateLog log = new OperateLog();
				log.setCertcn(curruser.getCertIdentify());
				log.setCreatetime(new Date());
				log.setDescription(params.toString());
				log.setIp(StringUtils.getRemoteAddr(request));
				log.setModuleid(request.getRequestURI());
				log.setOperatorid(String.valueOf(curruser.getUser().getId()));
				log.setException(ex != null ? ex.toString() : "");
				operateLogDao.save(log);
				operateLogDao.flush();
				
			}
		}
		
//		logger.debug("最大内存: {}, 已分配内存: {}, 已分配内存中的剩余空间: {}, 最大可用内存: {}", 
//				Runtime.getRuntime().maxMemory(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory(), 
//				Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()); 
		
	}

}
