
package sto.web.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import other.AuthProfile;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.web.BaseController;
import sto.model.account.SysSettings;
import sto.model.account.UnitSettings;
import sto.service.account.SysSettingsService;
import sto.service.account.UnitSettingsService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/unitsettings")
@SuppressWarnings("unchecked")
public class UnitSettingsAction extends BaseController{
	@Resource  
    UnitSettingsService unitSettingsService;
	@Resource  
    SysSettingsService sysSettingsService;
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/unitSettingsList";
	}
	
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJsonForList(Model model,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String divid = request.getParameter("divid");
		String flag = request.getParameter("flag");
		Parameter parameter = new Parameter();
		parameter.put("p1", divid);
		String sql = "select a.* from mlog_unit_settings a,mlog_system_settings b where a.skey=b.skey and b.iscommon=0 ";
		if(!auth.getRole().getEnname().equals(RoleType.ADMIN.getName())){
			sql += " and b.isvisible=1 ";
		}
		sql += "and a.divid=:p1 order by a.id ";
		Page<UnitSettings> page = new Page<UnitSettings>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		page.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Page<UnitSettings> resultPage = unitSettingsService.findBySql(page, sql, parameter,UnitSettings.class);
		List<SysSettings> list = sysSettingsService.find("from SysSettings where iscommon=0 ", null);
		if(flag.equals("0")){//flag=0查询单位设置，若为空，则根据系统默认值初始化
			if(resultPage.getTotalCount()<=0){
				for(SysSettings ss:list){
					UnitSettings us = new UnitSettings();
					us.setDivid(divid);
					us.setName(ss.getName());
					us.setSkey(ss.getSkey());
					us.setValue(ss.getValue());
					unitSettingsService.save(us);
				}
				resultPage = unitSettingsService.findBySql(page, sql, parameter,UnitSettings.class);
			}
		}else if(flag.equals("1")){//flag=1,若系统设置添加新的单位配置项，根据系统默认值增量更新
			List<UnitSettings> ulist = resultPage.getResult();
			List<String> keys = new ArrayList<String>();
			for(UnitSettings us : ulist){
				keys.add(us.getSkey());
			}
			Map<String,String[]> smap = new HashMap<String,String[]>();
			for(SysSettings ss:list){
				smap.put(ss.getSkey(), new String[]{ss.getName(),ss.getValue()});
			}
			Iterator<String> it = smap.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				if(!keys.contains(key)){
					UnitSettings us = new UnitSettings();
					us.setDivid(divid);
					us.setName(smap.get(key)[0]);
					us.setSkey(key);
					us.setValue(smap.get(key)[1]);
					unitSettingsService.save(us);
				}
			}
			resultPage = unitSettingsService.findBySql(page, sql, parameter,UnitSettings.class);
		}else if(flag.equals("2")){//flag=2,恢复系统默认值设置，包括更新系统项
			unitSettingsService.restoreDefaultSettings(divid);
			resultPage = unitSettingsService.findBySql(page, sql, parameter,UnitSettings.class);
		}
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("rows", resultPage.getResult()==null ? "":resultPage.getResult());
		m.put("total", resultPage.getTotalCount());
		return m;
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map updateDo(UnitSettings o,HttpServletRequest request) {
		String params = request.getParameter("params");
		JSONArray arr = JSON.parseArray(params);
		for(Object obj :arr){
			String id = ((JSONObject)obj).getString("id");
			String value = ((JSONObject)obj).getString("value");
			UnitSettings us = unitSettingsService.get(Integer.parseInt(id));
			us.setValue(value);
			unitSettingsService.save(us);
		}
		return suc();
	}
}
