
package sto.web.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sto.common.util.Parameter;
import sto.common.web.BaseController;
import sto.model.account.District;
import sto.service.account.DistrictService;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/district")
@SuppressWarnings("unchecked")
public class DistrictAction extends BaseController{

	
	@Resource  
    protected DistrictService districtService;
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/districtList";
	}
	
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJson.action")
	@ResponseBody
	public List<District> listJsonForList(Model model,HttpServletRequest request,HttpServletResponse response) {
		String id = request.getParameter("id");
		if(StringUtils.isBlank(id)){
			id = "0";
		}
		List<District> districtList = districtService.findBy("parent.id", id);
		for (District district : districtList) {
			if ("0".equals(district.getIsend())) { //如果不是末节点,设置状态关闭
				district.setState("closed");
			}
		}
		return districtList;
	}
	
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save.action")
	public String save() {
		return "account/districtSave";
	}
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map saveDo(District r,HttpServletRequest request) {
		r.setParent(districtService.get(r.getParent().getId()));
		districtService.save(r);
		return suc();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",districtService.get(request.getParameter("id")));
		return "account/districtUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map<String, Object> updateDo(District o,HttpServletRequest request) {
		
		District oDB = districtService.get(o.getId());
		oDB.setDistrictid(o.getDistrictid());
		oDB.setPostcode(o.getPostcode());
		oDB.setStatus(o.getStatus());
		oDB.setDistrictname(o.getDistrictname());
		oDB.setParent(districtService.get(o.getParent().getId()));
		districtService.update(oDB);
		return suc();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete.action")
	@ResponseBody
	public Map delete(String ids) {
		ids = ids.replaceAll(",", "','");
		if(StringUtils.isNotBlank(ids)) {
			districtService.delete("delete from District where id in ('" + ids + "')", null);
		}
		return suc();
	}
	
}
