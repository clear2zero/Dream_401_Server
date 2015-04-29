/**   
 * @Title: LoginAction.java 
 * @Package sto.web.account 
 * @author chenxiaojia  
 * @date 2014-7-24 下午3:29:28 
 * @version V1.0   
 */
package sto.web.account;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sto.common.util.Parameter;
import sto.common.web.BaseAction;
import sto.model.account.Unit;
import sto.service.account.UnitService;

/**
 * @ClassName: LoginAction
 * @Description:
 * @author chenxiaojia
 * @date 2014-7-24 下午3:29:28
 * 
 */
@Controller
public class LoginAction extends BaseAction {
	Log log = LogFactory.getLog(LoginAction.class);
	@Resource
	UnitService unitService;
	
	@RequestMapping(value = "/account/login.action",method = RequestMethod.GET)
	public String login(HttpServletRequest request,
			Model model,RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		//String str = request.getRequestURL().toString();
		//request.getSession().setAttribute("ctx", str);
		if (SecurityUtils.getSubject().isAuthenticated()) {
			return "redirect:/account/loginEnd.action";
		}else {
			String certcn = request.getParameter("certcn");
			//certcn = "ceshi8444323";
			if(!StringUtils.isBlank(certcn)){
				request.setAttribute("certcn", certcn);
				return "front/autoLogin";
			}
			return "front/index1";
		}
		
	}
	@RequestMapping(value = "/account/login.action",method = RequestMethod.POST)
	public String fail(
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName,
			HttpServletRequest request,
			Model model,RedirectAttributes redirectAttributes) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				userName);
		Object error = request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		redirectAttributes.addFlashAttribute("loginInvalid", error!=null?error.toString():"错误");
		return "redirect:/account/login.action";
	}
}
