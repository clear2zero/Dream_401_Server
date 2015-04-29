
package sto.web.account;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sto.common.web.BaseController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/common")
@SuppressWarnings("unchecked")
public class CommonController extends BaseController{
	@RequestMapping("/getServerDateStr.action")
	@ResponseBody
	public JSONArray getServerDateStr(HttpServletRequest request) {
		JSONArray jsonarr = new JSONArray();
		Calendar ca = Calendar.getInstance();
		jsonarr.add(ca.get(Calendar.YEAR));
		jsonarr.add(ca.get(Calendar.MONTH));
		jsonarr.add(ca.get(Calendar.DATE));
		jsonarr.add(ca.get(Calendar.HOUR_OF_DAY));
		jsonarr.add(ca.get(Calendar.MINUTE));
		jsonarr.add(ca.get(Calendar.SECOND));
		return jsonarr;
	} 
}
