
package sto.web.account;

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
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.util.Util;
import sto.common.web.BaseController;
import sto.model.account.OperateLog;
import sto.model.account.Role;
import sto.service.account.OperateLogService;
import sto.service.account.RoleService;
import sto.utils.IdGen;

/**
 * 操作日志
 * 
 * 
 */
@Controller
@RequestMapping(value = "/operatelog")
@SuppressWarnings("unchecked")
public class OperateLogAction extends BaseController{

	
	@Resource  
    protected OperateLogService operateLogService;
	
	/**
	 * 列表页面
	 */
	@RequestMapping("/list.action")
	public String list() {
		return "account/operateLogList";
	}
	/**
	 * 列表返回json
	 */
	
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJson(Model model, HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		Page<OperateLog> p = new Page<OperateLog>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		String sql = "";
		Map<String, Object> m = new HashMap<String, Object>();
		if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
			Parameter parameter = new Parameter();
			sql = "select a.* from platform_t_operate_log a "
					+" left join platform_t_user b on a.operatorid=b.id"
					+" where b.divid=:p1 ";
			if(!StringUtils.isBlank(request.getParameter("certcn"))){
				sql += " and a.certcn='"+request.getParameter("certcn")+"'";
			}
			sql +=" order by createtime desc ";
			parameter.put("p1", auth.getUser().getUnit().getDivid());
			Page<OperateLog> resultPage = operateLogService.findBySql(p, sql, parameter, OperateLog.class);
			m.put("rows", resultPage.getResult()==null ? "":resultPage.getResult());
			m.put("total", resultPage.getTotalCount());
			return m;
		}else {
			sql = "select * from platform_t_operate_log a where 1=1 ";
			if(!StringUtils.isBlank(request.getParameter("certcn"))){
				sql += " and a.certcn='"+request.getParameter("certcn")+"'";
			}
			sql += " order by createtime desc ";
			Page<OperateLog> resultPage = operateLogService.findBySql(p, sql, null,OperateLog.class);
			m.put("rows", resultPage.getResult()==null ? "":resultPage.getResult());
			m.put("total", resultPage.getTotalCount());
			return m;
		}
	}
	
}
