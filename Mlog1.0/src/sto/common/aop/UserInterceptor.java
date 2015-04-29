package sto.common.aop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.common.util.StringUtils;
import sto.model.account.UnitSettings;
import sto.model.account.User;
import sto.service.account.UnitSettingsService;
import sto.service.account.UserService;

/**
 * 系统拦截器
 * @author ThinkGem
 * @version 2013-6-6
 */
public class UserInterceptor extends BaseServiceImpl implements HandlerInterceptor {

	//private static OperateLogDao operateLogDao = SpringContextHolder.getBean(OperateLogDao.class);
	@Resource
	UnitSettingsService unitSettingsService;
	@Resource
	UserService userService;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	@Transactional(readOnly = false)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		
        String requestRri = request.getRequestURI();
        List<String> list = new ArrayList<String>();
        if (StringUtils.endsWith(requestRri, "/user/saveDo.action")){
        	String divid = request.getParameter("divid");
        	if(divid != null && !divid.equals("")){
        		list.add(divid);
        	}
        }
        if (StringUtils.endsWith(requestRri, "/user/updateDo.action")){
        	String divid = request.getParameter("divid");
        	if(divid != null && !divid.equals("")){
        		list.add(divid);
        	}
        }
        if (StringUtils.endsWith(requestRri, "/user/delete.action")){
        	String ids = request.getParameter("ids");
        	String[] idsarr = ids.split(",");
        	for(String id:idsarr){
        		User user = userService.get(Integer.parseInt(id));
        		if(user.getUnit() != null){
        			list.add(user.getUnit().getDivid());
        		}
        	}
        }
        
        if (StringUtils.endsWith(requestRri, "/user/unfreeze.action")){
        	String ids = request.getParameter("ids");
        	String[] idsarr = ids.split(",");
        	for(String id:idsarr){
        		User user = userService.get(Integer.parseInt(id));
        		if(user.getUnit() != null){
        			list.add(user.getUnit().getDivid());
        		}
        	}
        }
        
        for(String divid : list){
        	List<UnitSettings> list1 = unitSettingsService.find(" from UnitSettings where skey='lastupdated' and divid=:p1", new Parameter(divid));
    		if(list1 != null && list1.size()>0){
    			UnitSettings unitSettings = list1.get(0);
    			unitSettings.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        		System.out.println("单位【"+divid+"】通讯录已更新");
        		unitSettingsService.save(unitSettings);
    		}
        }
	}

}
