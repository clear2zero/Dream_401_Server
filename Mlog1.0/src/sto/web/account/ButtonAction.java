
package sto.web.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import other.AuthProfile;
import sto.common.util.Page;
import sto.common.util.Util;
import sto.common.web.BaseController;
import sto.model.account.Button;
import sto.model.account.Role;
import sto.service.account.ButtonService;

/**
 * 角色管理
 * 功能：增、删、改、查、分配权限
 * 
 */
@Controller
@RequestMapping(value = "/button")
@SuppressWarnings("unchecked")
public class ButtonAction extends BaseController{

	
	@Resource  
    protected ButtonService buttonService;
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/buttonList";
	}
	/**
	 * 列表返回json
	 */
	
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJson(Model model, HttpServletRequest request) {

		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject()
				.getPrincipal();
		String hql = "from Button order by orderid ";

		Map<String, Object> m = new HashMap<String, Object>();
		Page<Role> p = new Page<Role>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Page<Role> resultPage = buttonService.find(p, hql, null);
		m.put("rows", resultPage.getResult());
		m.put("total", resultPage.getTotalCount());
		return m;
	}
	
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save.action")
	public String save() {
		return "account/buttonSave";
	}
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map saveDo(Button r,HttpServletRequest request) {
		buttonService.save(r);
		return suc();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",buttonService.get(Integer.parseInt(request.getParameter("id"))));
		return "account/buttonUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	
	public Map updateDo(Button o,HttpServletRequest request) {
		Button oDB = buttonService.get(o.getId());
		oDB.setEnname(o.getEnname());
		oDB.setName(o.getName());
		oDB.setOrderid(o.getOrderid());
		oDB.setRemark(o.getRemark());
		oDB.setStatus(o.getStatus());
		oDB.setHandler(o.getHandler());
		buttonService.update(oDB);
		
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
			buttonService.delete("delete from Button where id in('"+ids+"')", null);
		}
		return suc();
	}
	
	@RequestMapping("/getButtonsContainCheckstatus.action")
	@ResponseBody
	public Map getButtonsContainCheckstatus(Model model, HttpServletRequest request) {
		Map<String, Object> m = new HashMap<String, Object>();
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("mid", request.getParameter("mid"));
		List<Map<String,Object>>  list = buttonService.getButtonsContainCheckstatus(p,paramap);
		m.put("rows", list);
		m.put("total", p.getTotalCount());
		return m;
	}	
}
