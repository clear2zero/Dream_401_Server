package sto.web.account;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import other.AuthProfile;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.web.BaseController;
import sto.service.account.DataRulesService;
@Controller
@RequestMapping(value = "/auth")
@SuppressWarnings("unchecked")
public class AuthController extends BaseController{
	@Resource  
    protected DataRulesService dataRulesService;
	
	@RequestMapping("/listJson.action")
	@ResponseBody
	public List<Map<String,Object>> listJsonForList(Model model,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String divid = request.getParameter("divid");
		if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
			divid = auth.getUser().getUnit().getDivid();
		}
		if(StringUtils.isBlank(divid)){
			return null;
		}
		Parameter parameter = new Parameter();
		parameter.put("divid", divid);
		return dataRulesService.getAuthContactsTree(parameter);
	}
}
