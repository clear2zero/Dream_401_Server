
package sto.web.account;

import java.io.IOException;
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
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.web.BaseController;
import sto.model.account.Role;
import sto.model.account.User;
import sto.service.account.RoleService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 角色管理
 * 功能：增、删、改、查、分配权限
 * 
 */
@Controller
@RequestMapping(value = "/role")
@SuppressWarnings("unchecked")
public class RoleAction extends BaseController{

	
	@Resource  
    protected RoleService roleService;
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/roleList";
	}
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJson(Model model, HttpServletRequest request) {

		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject()
				.getPrincipal();
		String hql = "from Role order by orderid";

		Map<String, Object> m = new HashMap<String, Object>();
		Page<Role> p = new Page<Role>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Page<Role> resultPage = roleService.find(p, hql, null);
		m.put("rows", resultPage.getResult());
		m.put("total", resultPage.getTotalCount());
		return m;
	}
	@RequestMapping("/listJsonNoPage.action")
	@ResponseBody
	public List<Role> listJsonNoPage(Model model, HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String hql = "";
		
		if(RoleType.UNITADMIN.getName().equals(auth.getRole().getEnname())){
			hql = "from Role where status='1' and enname in ('"+RoleType.UNIT_USERGROUP.getName().replace(",", "','")+"') order by orderid ";
		}else if(RoleType.NORMAL.getName().equals(auth.getRole().getEnname())){
			hql = "from Role where status='1' and enname='"+RoleType.NORMAL.getName()+"' order by orderid ";
		}else {
			hql = "from Role where status='1' order by orderid ";
		}
		List<Role> result = roleService.find(hql, null);
		return result;
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save.action")
	public String save() {
		return "account/roleSave";
	}
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map saveDo(Role r,HttpServletRequest request) {
		roleService.save(r);
		return suc();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",roleService.get(Integer.parseInt(request.getParameter("id"))));
		return "account/roleUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map updateDo(Role o,HttpServletRequest request) {
		Role oDB = roleService.get(o.getId());
		oDB.setEnname(o.getEnname());
		oDB.setName(o.getName());
		oDB.setOrderid(o.getOrderid());
		oDB.setRemark(o.getRemark());
		oDB.setStatus(o.getStatus());
		roleService.update(oDB);
		
		return suc();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete.action")
	@ResponseBody
	public Map<String, Object> delete(String ids) {
		if(StringUtils.isNotBlank(ids)) {
			ids = ids.replaceAll(",", "','");
			if(roleService.find("from RoleUser where  role.id in('"+ids+"')", null).size() > 0) {
				return err("角色已使用，不允许删除");
			}
			roleService.delete("delete from Role where id in('"+ids+"')", null);
		}
		return suc();
	}
	
	/**
	 * 分配菜单页面
	 */
	@RequestMapping(value="/roleMenu.action")
	public String roleMenu() {
		return "account/roleMenu";
	}
	
	
	/**
	 * 分配菜单页面返回json
	 */
	@RequestMapping(value="/roleMenuJson.action")
	@ResponseBody
	public List roleMenuJson(HttpServletRequest request,HttpServletResponse response) {
		
		return roleService.roleMenuJson(Integer.parseInt(request.getParameter("id")),0);
	}
	
	/**
	 * 分配菜单保存
	 */
	@RequestMapping(value="/roleMenuDo.action")
	@ResponseBody
	public Map roleMenuDo(HttpServletRequest request) {
		
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id) ){
			roleService.update("delete from RoleModule where roleid=:p1", new Parameter(id)); 
		}
		
		String rids = request.getParameter("rids");
		if(StringUtils.isNotBlank(rids)){
			String[] ridsStr = rids.split(",");
			for(int i=0;i<ridsStr.length;i++){
				 if(!"0".equals(ridsStr[i])){
					 roleService.updateBySql("insert into platform_t_role_module(roleid,moduleid) values('" + id + "','" + ridsStr[i]+"')", null);
				 }
			}
		}
		return suc();
	}
	
	@RequestMapping(value="/test.action")	
	public String test(HttpServletRequest request,Model model) {
		model.addAttribute("tt", "hello,world");
		return "account/roletest";
		
	}
	
	@RequestMapping(value="/roleButton.action")
	public String roleButton(HttpServletRequest request,Model model) {
		return "account/roleButton";
	}
	@RequestMapping(value="/getRoleModulesButtons.action")
	@ResponseBody
	public void getRoleModulesButtons(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String json = JSON.toJSONStringWithDateFormat(roleService.getRoleModulesButtons(Integer.parseInt(request.getParameter("id"))), "yyyy-MM-dd HH:mm:ss");
		response.setCharacterEncoding("utf-8");
		//response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(json);
		response.getWriter().flush();
		response.getWriter().close();
	}
	@RequestMapping(value="/saveRoleModuleButton.action")
	@ResponseBody
	public Map saveRoleModuleButton(HttpServletRequest request,Model model) {
		int roleid = Integer.parseInt(request.getParameter("id"));
		String roleActions = request.getParameter("roleActions");
		roleService.saveResActions(roleid,roleActions);
		return suc();
	}
	@RequestMapping(value="/isUnitgroup.action")
	@ResponseBody
	public JSONObject isUnitgroup(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String id = request.getParameter("id");
		Role role = roleService.get(Integer.parseInt(id));
		String[] unitgroup = RoleType.UNIT_USERGROUP.getName().split(",");
		json.put("success", false);
		for(int i=0;i<unitgroup.length;i++){
			if(role.getEnname().equals(unitgroup[i])){
				json.put("success", true);
				break;
			}
		}
		return json;
	} 
}
