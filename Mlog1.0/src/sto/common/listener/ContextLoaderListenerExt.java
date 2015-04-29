package sto.common.listener;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import other.CacheKey;
import sto.model.account.Module;
import sto.model.account.Role;
import sto.service.account.ModuleService;
import sto.service.account.RoleService;
import sto.utils.CacheUtils;

public class ContextLoaderListenerExt extends ContextLoaderListener{
	private ModuleService moduleService;  
	private Module module;
	private RoleService roleService;  
	private Role role;

	public void contextInitialized(ServletContextEvent event) {  
		super.contextInitialized(event);
		
		System.out.println(">>>>开始初始化不同角色菜单到内存");
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		RoleService roleService = (RoleService) applicationContext.getBean("roleService");
		ModuleService moduleService = (ModuleService) applicationContext.getBean("moduleService");
		
		//module = (Module) applicationContext.getBean("sto.model.account.Module");
		//role = (Role) applicationContext.getBean("sto.model.account.Role");
		
		List<Role> list = roleService.getAll();
		for(Role role : list){
			String sql = "select m.* from platform_t_module m,"
					+ "platform_t_role_module r where m.superid=1 and r.moduleid=m.id and r.roleid='" + role.getId() + "' order by orderid";
			
			CacheUtils.put(CacheKey.MENU, role.getEnname(), moduleService.findBySql(sql, null, Module.class));
		}
		System.out.println(">>>>初始化结束");
	}  
}
