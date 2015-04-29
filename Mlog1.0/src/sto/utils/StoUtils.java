package sto.utils;

import java.util.List;

import sto.common.util.Parameter;
import sto.common.util.Util;
import sto.model.account.Module;
import sto.service.account.ModuleService;

import javax.servlet.ServletContext;

/**
 * 工具类
 * @author zzh
 * @date 2014/10/21
 */
public class StoUtils {
	
	private static ModuleService moduleService = SpringContextHolder.getBean(ModuleService.class);
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);

	private static final String STO_CACHE = "stoCache";
	
	/**
	 * 获得菜单列表
	 */
	public static List<Module> getMenuList(String colname, Integer colvalue){
		@SuppressWarnings("unchecked")
		List<Module> moduleList = (List<Module>)CacheUtils.get(STO_CACHE, "moduleList");
		if (moduleList == null){
			moduleList = moduleService.find("from Module where status = :p2 and " + colname + "= :p1", new Parameter(colvalue, Util.DEL_FLAG_NORMAL));
			CacheUtils.put(STO_CACHE, "moduleList", moduleList);
		}
		return moduleList;
	}
	
}