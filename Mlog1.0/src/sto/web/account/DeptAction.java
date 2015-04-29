
package sto.web.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import other.AuthProfile;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.web.BaseController;
import sto.model.account.Dept;
import sto.model.account.UnitSettings;
import sto.service.account.DeptService;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/dept")
@SuppressWarnings("unchecked")
public class DeptAction extends BaseController{

	
	@Resource  
    protected DeptService deptService;
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/deptList";
	}
	
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJson.action")
	@ResponseBody
	public List<Dept> listJsonForList(Model model,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String id = request.getParameter("id");
		String divid = request.getParameter("divid");
		List<Dept> deptList = null;
		String hql = "";
		Parameter parameter = new Parameter();
		if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
			parameter.put("p1", auth.getUser().getUnit().getDivid());
			hql = " select * from mlog_dept a where a.parentid is null and a.divid=:p1 ";
			deptList = deptService.findBySql(hql, parameter, Dept.class);
		}/*else if(RoleType.NORMAL.getName().equals(auth.getRole().getEnname())){
			parameter.put("p1", auth.getUser().getUnit().getDivid());
			parameter.put("p2", auth.getUser().getDept().getId());
			hql = " select * from mlog_dept a where a.divid=:p1 and a.id=:p2 ";
			deptList = deptService.findBySql(hql, parameter, Dept.class);
		}*/else {
			if(!StringUtils.isBlank(divid)){
				parameter.put("p1", divid);
				hql = " select * from mlog_dept a where a.parentid is null and a.divid=:p1 ";
				deptList = deptService.findBySql(hql, parameter, Dept.class);
			}else {
				//deptList = deptService.findBySql("select * from mlog_dept a where a.parentid is null",null,Dept.class);
				deptList = new ArrayList<Dept>();
			}
			
		}
		for (Dept dept : deptList) {
			if (dept.getChildren().size() > 0) { //如果不是末节点,设置状态关闭
				dept.setState("closed");
			}
		}
		return deptList;
	}
	
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save.action")
	public String save() {
		return "account/deptSave";
	}
	
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public Map saveDo(Dept r,HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		Parameter parameter = new Parameter();
		String sql = " select count(1) from mlog_dept a where 1=1 ";
		if(RoleType.UNITADMIN.getName().equals(auth.getRole().getEnname())){
			parameter.put("p1", auth.getUser().getUnit().getDivid());
			sql += " and a.divid=:p1 ";
		}else {
			return err("无权限,当前用户非单位管理员");
		}
		sql += " and (a.deptid=:p2 or a.deptname=:p3)";
		parameter.put("p2", r.getDeptid());
		parameter.put("p3", r.getDeptname());
		int count = Integer.parseInt(String.valueOf(deptService.findBySql(sql, parameter, null).get(0)));
		if(count > 0){
			return err("已存在该部门编码或名称");
		}
		Dept parent = null;
		if(r.getParent().getId() == null){
			r.setLevel(1);
			r.setParent(parent);
		}else {
			parent = deptService.get(r.getParent().getId());
			parent.setIsleaf(0);
			deptService.update(parent);
			r.setLevel(parent.getLevel()+1);
			r.setParent(parent);
		}
		
		r.setIsleaf(1);
		r.setIsdelete(0);
		r.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		r.setDivid(auth.getUser().getUnit().getDivid());
		deptService.save(r);
		
		return suc();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",deptService.get(Integer.parseInt(request.getParameter("id"))));
		return "account/deptUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map<String, Object> updateDo(Dept o,HttpServletRequest request) {
		Dept dept = deptService.get(o.getId());
		Dept parent = dept.getParent();
		if(parent != null ){//
			parent.getChildren().remove(dept);
			if(parent.getChildren().size()>0){
				parent.setIsleaf(1);
			}
			deptService.save(parent);
		}
		Dept targetparent = null;
		if(o.getParent().getId() == null){
			dept.setLevel(1);
		}else {
			targetparent = deptService.get(o.getParent().getId());
			targetparent.setIsleaf(0);
			deptService.save(targetparent);
			dept.setLevel(targetparent.getLevel()+1);
		}
		
		dept.setParent(targetparent);
		dept.setId(o.getId());
		dept.setDeptname(o.getDeptname());
		deptService.update(dept);
		return suc();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete.action")
	@ResponseBody
	public Map delete(String ids) {
		/*ids = ids.replaceAll(",", "','");
		if(StringUtils.isNotBlank(ids)) {
			deptService.delete("delete from Dept where id in ('" + ids + "')", null);
		}*/
		Dept dept = deptService.get(Integer.parseInt(ids));
		if(deptService.isDeptHasUsers(dept)){
			return err("该部门或子部门下存在用户");
		}
		/*Dept parent = dept.getParent();
		if(parent != null ){
			parent.getChildren().remove(dept);
			if(parent.getChildren().size()<1){
				parent.setIsleaf(0);
			}
			deptService.save(parent);
		}
		dept.setParent(null);*/
		deptService.delete(dept);
		return suc();
	}	
	
	@RequestMapping(value="/batchSaveDo.action")
	@ResponseBody
	public Map batchSaveDo(UnitSettings o,HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String params = request.getParameter("params");
		JSONArray arr = JSON.parseArray(params);
		for(Object obj :arr){
			String deptid = ((JSONObject)obj).getString("deptid");
			String deptname = ((JSONObject)obj).getString("deptname");
			int orderid = Integer.parseInt(((JSONObject)obj).getString("orderid"));
			Dept dept = new Dept();
			dept.setDeptid(deptid);
			dept.setDeptname(deptname);
			dept.setIsleaf(1);
			dept.setIsdelete(0);
			dept.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			dept.setDivid(auth.getUser().getUnit().getDivid());
			dept.setParent(null);
			dept.setOrderid(orderid);
			deptService.save(dept);
		}
		return suc();
	}
	
	
}
