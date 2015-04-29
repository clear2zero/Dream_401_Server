
package sto.web.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import other.AuthProfile;
import other.CacheKey;
import other.CacheName;
import sto.common.annotation.CacheInfo;
import sto.common.web.BaseController;
import sto.model.account.Module;
import sto.model.account.ModuleButton;
import sto.service.account.ModuleButtonService;
import sto.service.account.ModuleService;
import sto.service.account.RoleModuleService;
import sto.utils.CacheUtils;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/module")
@SuppressWarnings("unchecked")
public class ModuleAction extends BaseController{

	
	@Resource  
    protected ModuleService moduleService;
	@Resource
	RoleModuleService roleModuleService;
	@Resource
	ModuleButtonService moduleButtonService;
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/menuList";
	}
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJsonForMenu.action")
	@ResponseBody
	public List<Map<String, Object>> listJsonForMenu(Model model,HttpServletRequest request,HttpServletResponse response) {
		String id = request.getParameter("id");
		if(StringUtils.isBlank(id)){
			id = "0";
		}
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String sql = "select m.* from platform_t_module m,"
				+ "platform_t_role_module r where m.superid='" + id + "' and r.moduleid=m.id and r.roleid='" + curruser.getRole().getId() + "' order by orderid";
		List<Module> moduleList = moduleService.findBySql(sql, null, Module.class);

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < moduleList.size(); i++) {
			Module r = (Module) moduleList.get(i);
			Map<String, Object> menuMap = new HashMap<String, Object>();
			Map<String, String> attributesMap = new HashMap<String, String>();
			attributesMap.put("url", r.getCurl());
			attributesMap.put("enname", r.getEnname());
			attributesMap.put("superid", String.valueOf(r.getSuperid()));
			attributesMap.put("isend", r.getIsend());
			if(r.getChildren() != null && r.getChildren().size()>0){
				menuMap.put("state", "closed");
				attributesMap.put("isend", "0");
			}else {
				attributesMap.put("isend", "1");
			}
			/*if ("0".equals(r.getIsend())) { //如果不是末节点,设置状态关闭
				menuMap.put("state", "closed");
			}*/
			menuMap.put("id", r.getId());
			menuMap.put("text", r.getName());
			menuMap.put("iconCls", r.getIcon());
			menuMap.put("attributes", attributesMap);
			retList.add(menuMap);
		}
		return retList;
		
	}
	
	@RequestMapping("/leftTree.action")
	@ResponseBody
	public List<Module> leftTree(Model model,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		if(CacheUtils.get(CacheKey.MENU, auth.getRole().getEnname()) != null){
			return (List<Module>)CacheUtils.get(CacheKey.MENU, auth.getRole().getEnname());
		}else {
			String sql = "select m.* from platform_t_module m,"
					+ "platform_t_role_module r where m.superid=1 and r.moduleid=m.id and r.roleid='" + auth.getRole().getId() + "' order by orderid";
			List<Module> list = moduleService.findBySql(sql, null, Module.class);
			CacheUtils.put(CacheKey.MENU, auth.getRole().getEnname(),list);
			return list;
		}
		
	}
	@RequestMapping("/leftTree1.action")
	@ResponseBody
	public List<Map<String,Object>> leftTree1(Model model,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String sql = "select m.id,m.orderid,m.curl,m.superid,m.enname,m.name,m.icon from platform_t_module m,"
				+ "platform_t_role_module r where m.superid<>0 and r.moduleid=m.id and r.roleid='" + auth.getRole().getId() + "' order by orderid";
		List<Object[]> list1 = moduleService.findBySql(sql, null, null);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(list1 != null){
			Map<String,Object> map = null;
			for(Object[] obj: list1){
				map = new HashMap<String,Object>();
				/*map.put("id", String.valueOf(obj[0]));
				map.put("orderid", String.valueOf(obj[1]));
				map.put("curl", String.valueOf(obj[2]));
				map.put("pId", String.valueOf(obj[3]));
				map.put("enname", String.valueOf(obj[4]));
				map.put("name", String.valueOf(obj[5]));
				map.put("icon", String.valueOf(obj[6] == null ? "" :obj[6]));*/
				map.put("id", obj[0]);
				map.put("orderid", obj[1]);
				map.put("curl", obj[2]);
				map.put("pId", obj[3]);
				map.put("enname", obj[4]);
				map.put("name", obj[5]);
				map.put("icon", obj[6]);
				list.add(map);
			}
		}
		return list;
	}
	@RequestMapping("/listJsonForMenuTree.action")
	@ResponseBody
	public List<Map<String, Object>> listJsonForMenuTree(Model model,HttpServletRequest request,HttpServletResponse response) {
		String id = request.getParameter("id");
		if(StringUtils.isBlank(id)){
			id = "0";
		}
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String sql = "select m.* from platform_t_module m where m.superid='" + id + "' order by orderid";
		List<Module> moduleList = moduleService.findBySql(sql, null, Module.class);

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < moduleList.size(); i++) {
			Module r = (Module) moduleList.get(i);
			Map<String, Object> menuMap = new HashMap<String, Object>();
			Map<String, String> attributesMap = new HashMap<String, String>();
			attributesMap.put("url", r.getCurl());
			attributesMap.put("enname", r.getEnname());
			attributesMap.put("superid", String.valueOf(r.getSuperid()));
			attributesMap.put("isend", r.getIsend());
			if(r.getChildren() != null && r.getChildren().size()>0){
				menuMap.put("state", "closed");
			}
			menuMap.put("id", r.getId());
			menuMap.put("text", r.getName());
			menuMap.put("iconCls", r.getIcon());
			menuMap.put("attributes", attributesMap);
			retList.add(menuMap);
		}
		return retList;
		
	}
	
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJson.action")
	@ResponseBody
	public List<Module> listJson(Model model,HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String id = request.getParameter("id");
		if(StringUtils.isBlank(id)){
			id = "1";
		}
		List<Module> moduleList = moduleService.findBy("superid",Integer.parseInt(id));
		
		for (Module module : moduleList) {
			if(module.getChildren() != null && module.getChildren().size()>0){
				module.setState("closed");
			}/*
			if ("0".equals(module.getIsend())) { //如果不是末节点,设置状态关闭
				module.setState("closed");
			}*/
		}
		return moduleList;
		
	}
	
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save.action")
	public String save() {
		return "account/menuSave";
	}
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map saveDo(Module r,HttpServletRequest request) {
		/*if(r.getSuperid() != null){
			Module superModule = moduleService.get(r.getSuperid());
			superModule.setIsend("0");
			moduleService.save(superModule);
		}*/
		r.setSuperid(r.getSuperid());
		//r.setIsend("1");
		moduleService.save(r);
		return suc();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",moduleService.get(Integer.parseInt(request.getParameter("id"))));
		return "account/menuUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map updateDo(Module o,HttpServletRequest request) {
		
		Module oDB = moduleService.get(o.getId());
		oDB.setName(o.getName());
		oDB.setCurl(o.getCurl());
		oDB.setEnname(o.getEnname());
		oDB.setStatus(o.getStatus());
		oDB.setOrderid(o.getOrderid());
		oDB.setSuperid(o.getSuperid());
		oDB.setIcon(o.getIcon());
		moduleService.update(oDB);
		return suc();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete.action")
	@ResponseBody
	public Map delete(String ids) {
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		ids = ids.replaceAll(",", "','");
		if(StringUtils.isNotBlank(ids)) {
			roleModuleService.delete("delete from RoleModule where moduleid in('"+ids+"') and roleid="+curruser.getRole().getId(), null);
			moduleService.delete("delete from Module where id in ('" + ids + "')", null);
		}
		return suc();
	}
	
	/**
	 * 分配资源
	 */
	@RequestMapping(value="/menuButton.action")
	public String menuButton(Model o,HttpServletRequest request) {
		o.addAttribute("mid", request.getParameter("id"));
		return "account/menuButton";
	}
	
	/**
	 * 分配资源
	 */
	@RequestMapping(value="/menuButtonDo.action")
	@ResponseBody
	public Map menuButtonDo(Model o,HttpServletRequest request) {
		String bids = request.getParameter("bids");
		int mid = Integer.parseInt(request.getParameter("mid"));
		moduleButtonService.save(mid, bids);
		return suc();
	}
	
}
