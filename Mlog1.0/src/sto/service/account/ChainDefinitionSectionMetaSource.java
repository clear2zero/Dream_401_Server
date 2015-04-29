package sto.service.account;

import java.text.MessageFormat;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import sto.model.account.Module;

/**
 * 获取url与授权的资源
* @ClassName: ChainDefinitionSectionMetaSource 
* @Description: 授权链，服务启动的时候会加载
* @author chenxiaojia
* @date 2014-7-8 下午1:50:44 
*
 */
public class ChainDefinitionSectionMetaSource implements
		FactoryBean<Ini.Section> {
	private Log log = LogFactory.getLog(ChainDefinitionSectionMetaSource.class);
	private String filterChainDefinitions;
	@Autowired
	private ModuleService resourcestoService;
	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	@Override
	public Section getObject() throws BeansException {
		Iterable<Module> list = resourcestoService.getAll();
		Ini ini = new Ini();
		ini.load(filterChainDefinitions);
		Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		//加载操作权限，因为暂时没用到操作权限，先注释
//		for (Iterator<Module> it = list.iterator(); it.hasNext();) {
//			Module resource = it.next();
//			if (StringUtils.isNotEmpty(resource.getCurl())
//					&& StringUtils.isNotEmpty(resource.getPermission())) {
//				String per = resource.getPermission();
//				String format =  per.contains(":")?MessageFormat.format(PREMISSION_STRING,resource.getPermission()):per;
//				section.put(resource.getUrl(), format);
//			}
//		}
		section.put("/**", "authc");
		log.debug(section.values());
		return section;
	}

	/**
	 * 通过filterChainDefinitions对默认的url过滤定义
	 * 
	 * @param filterChainDefinitions
	 *            默认的url过滤定义
	 */
	@Override
	public Class<?> getObjectType() {
		return this.getClass();
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}
	
	@Autowired
	public ModuleService getResourcestoService() {
		return resourcestoService;
	}

	public void setResourcestoService(ModuleService resourcestoService) {
		this.resourcestoService = resourcestoService;
	}
	
	


}
