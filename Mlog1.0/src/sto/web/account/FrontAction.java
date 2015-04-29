package sto.web.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sto.common.web.BaseController;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/front")
public class FrontAction extends BaseController{
	@RequestMapping(value = "/main.action")
	public String login(Model model){
		return "front/index";
	}
	
	@RequestMapping(value = "/help.action")
	public String help(Model model){
		return "front/help";
	}
}
