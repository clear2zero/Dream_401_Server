package sto.service.account;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.dao.account.ModuleButtonDao;
import sto.dao.account.ModuleDao;
import sto.dao.account.RoleDao;
import sto.dao.account.RoleModuleButtonDao;
import sto.dao.account.RoleModuleDao;
import sto.model.account.Module;
import sto.model.account.ModuleButton;
import sto.model.account.Role;
import sto.model.account.RoleModule;
import sto.model.account.RoleModuleButton;

/**
 * @ClassName: RoleService
 * @Description: service
 * @author chenxiaojia
 * @date 2014-7-25 11:07:12
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class RoleService extends BaseServiceImpl<Role>{
	@Autowired
	public RoleDao roleDao;
	@Autowired
	public RoleModuleDao roleModuleDao;
	@Autowired
	public ModuleDao moduleDao;
	@Autowired
	RoleModuleButtonDao roleModuleButtonDao;
	@Autowired
	ModuleButtonDao moduleButtonDao;
	@Resource
	ModuleButtonService moduleButtonService;
	
	public List<List> getPermissions(String roleId){
		List<Object[]> list =  roleDao.getPermission(roleId);
		List<String> perL = new ArrayList();
		List<String> urlL = new ArrayList();
		List<List> blist = new ArrayList();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				Object[] obj = list.get(i);
				if(obj[0]!=null && StringUtils.isNotBlank(obj[0].toString() )){
					if(obj[0].toString().equals("perms")){
						perL.add(obj[1].toString());
					}else{
						perL.add(obj[0].toString());
					}
				}
				if(obj[1]!=null && StringUtils.isNotBlank(obj[1].toString() )){
					urlL.add(obj[1].toString());
				}
			}
		}
		blist.add(perL);
		blist.add(urlL);
		return blist;
	}
	
	/**
	 * 返回权限树
	 * roleId 不使用，赋值-1
	 * @return
	 */
	public List<Module> roleMenuJson(Integer roleId,int type){
		
		//获取本角色现有的菜单权限
		List<RoleModule> roleModuleList = roleModuleDao.find("from RoleModule where role.id=" + roleId);
		
		// 获取所有菜单
		List<Module> moduleList = moduleDao.find("from Module where status=:p1 and superid=:p2", new Parameter(1,0));
		
		// 遍历菜单，若该角色拥有此菜单，设置选中状态
		setCheckStatus(moduleList, roleModuleList);
		return moduleList;
	}
	
	/**
	 * 遍历菜单表，设置此角色拥有权限的菜单为选中状态
	 * @param moduleList 菜单列表
	 * @param roleModuleList 权限表
	 */
	public void setCheckStatus(List<Module> moduleList, List<RoleModule> roleModuleList) {
		for (Module module : moduleList) {
			for (RoleModule roleModule : roleModuleList) {
				if (roleModule.getModule().getId() == module.getId()) {
					module.setChecked(true);
				}
			}
			if (module.getChildren().size() > 0) {
				setCheckStatus(module.getChildren(), roleModuleList);
			}
		}
	}
	
	public Map<String,Object> getRoleModulesButtons(Integer id){
		List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
		List<RoleModule> roleModuleList = roleModuleDao.find("from RoleModule where role.id=" + id);
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		//遍历每个资源
		for(RoleModule rm : roleModuleList){
			Module m = rm.getModule();
			Map<String,Object> module = new HashMap<String,Object>();
			Map<String, Object> attributesMap = new HashMap<String, Object>();
			if ("0".equals(m.getIsend())) { //如果不是末节点,设置状态关闭
				module.put("state", "closed");
			}
			module.put("id", m.getId());
			module.put("name", m.getName());
			module.put("ename", m.getEnname());
			module.put("id", m.getId());
			module.put("_parentId", m.getSuperid());
			
			//List<Button> mbuttons = new ArrayList<Button>();
			Map<String,Object> parammap = new HashMap<String,Object>();
			parammap.put("mid", m.getId());
			parammap.put("rid", id);
			List<Map<String,Object>> mbuttons = moduleButtonService.getModuleButtonsAuth(parammap);
			attributesMap.put("mbuttons", mbuttons);
			module.put("attributes", attributesMap);
			//用来存放一条资源记录
			//Map<String,Object> item = new HashMap<String,Object>();
			//item.put("module", module);
			//item.put("buttons", buttons);
			rows.add(module);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", rows);
		map.put("total", rows.size());
		return map;
	}
	@Transactional(readOnly = false)
	public void saveResActions(Integer roleId, String resActions) {
		List<RoleModuleButton> roleModuleButtonList = roleModuleButtonDao.find("from RoleModuleButton where role.id=" + roleId);
		for(RoleModuleButton rmb:roleModuleButtonList){
			roleModuleButtonDao.delete(rmb);
		}
		if(resActions!=null && !"".equals(resActions)){
			String actions[] = resActions.split(";");
			for(int i=0;i<actions.length;i++){
				String[] ids = actions[i].split(",");
				for(int j=0;j<ids.length;j++){
					String moduleid = ids[j].substring(0,ids[j].indexOf(":"));
					String buttonid = ids[j].substring(ids[j].indexOf(":")+1,ids[j].length());
					Parameter parameter = new Parameter();
					parameter.put("p1", roleId);
					parameter.put("p2", Integer.parseInt(moduleid));
					parameter.put("p3", Integer.parseInt(buttonid));
					roleModuleButtonDao.updateBySql(" insert into platform_t_role_module_button(roleid,moduleid,buttonid) value(:p1,:p2,:p3)", parameter);
				}
				
			}
		}
	}
	
}
