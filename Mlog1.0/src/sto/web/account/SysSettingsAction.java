
package sto.web.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import other.AuthProfile;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.web.BaseController;
import sto.model.account.SysSettings;
import sto.model.account.Unit;
import sto.model.account.UnitSettings;
import sto.service.account.SysSettingsService;
import sto.service.account.UnitService;
import sto.service.account.UnitSettingsService;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/system")
@SuppressWarnings("unchecked")
public class SysSettingsAction extends BaseController{
	@Resource  
    protected SysSettingsService sysSettingsService;
	@Resource  
    protected UnitSettingsService unitSettingsService;
	@Resource  
    protected UnitService unitService;
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/sysSettingsList";
	}
	
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJsonForList(Model model,HttpServletRequest request,HttpServletResponse response) {
		
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject()
				.getPrincipal();
		String hql = "from SysSettings order by id ";

		Map<String, Object> m = new HashMap<String, Object>();
		Page<SysSettings> page = new Page<SysSettings>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		page.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Page<SysSettings> resultPage = sysSettingsService.find(page, hql, null);
		m.put("rows", resultPage.getResult());
		m.put("total", resultPage.getTotalCount());
		return m;
	}
	
	@RequestMapping(value="/save.action")
	public String save() {
		return "account/sysSettingsSave";
	}
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map saveDo(SysSettings r,HttpServletRequest request) {
		sysSettingsService.save(r);
		if(r.getIscommon() == 0){
			List list = unitSettingsService.find(" from UnitSettings where skey=:p1 ", new Parameter(r.getSkey()));
			if(list == null || list.size()<=0){
				List<Unit> ulist = unitService.findBySql("select * from mlog_unit a where a.isregistered=1 and (a.acceptno is not null or a.acceptno<>'')", null, Unit.class);
				for(Unit u : ulist){
					UnitSettings us = new UnitSettings();
					us.setDivid(u.getDivid());
					us.setSkey(r.getSkey());
					us.setValue(r.getValue());
					us.setName(r.getName());
					unitSettingsService.save(us);
				}
			}else {
				//对单位配置项不做删除
			}
		}
		return suc();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",sysSettingsService.get(Integer.parseInt(request.getParameter("id"))));
		return "account/sysSettingsUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map updateDo(SysSettings o,HttpServletRequest request) {
		SysSettings ss = sysSettingsService.get(o.getId());
		ss.setName(o.getName());
		ss.setSkey(o.getSkey());
		ss.setValue(o.getValue());
		ss.setIscommon(o.getIscommon());
		ss.setIsvisible(o.getIsvisible());
		sysSettingsService.update(ss);
		return suc();
	}
}
