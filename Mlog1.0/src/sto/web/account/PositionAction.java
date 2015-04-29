
package sto.web.account;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sto.common.web.BaseController;

/**
 * 菜单管理
 * 功能：增、删、改、列表
 * 
 */
@Controller
@RequestMapping(value = "/position")
@SuppressWarnings("unchecked")
public class PositionAction extends BaseController{
	
	/*@RequestMapping("/getMap.action")
	public String getMap(Model model,HttpServletRequest request){
		String id = request.getParameter("id");
		long uid=Long.valueOf(id);
		String time = request.getParameter("time");
		List list=service.getSelfRecord(uid,time);
		Map map=new LinkedHashMap();
		String lgt="";
		String lat="";
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[]) list.get(i);
			lgt=(String) obj[0];
			lat=(String) obj[1];
			String address= (String)obj[2];
			String t=(String)obj[3];
			String phone = (String)obj[4];
			map.put(lgt,lat+"&"+address+"&"+t+"&"+phone);
		}
		if(map!=null&&map.size()>0){
			this.getRequest().setAttribute("map", map);
			this.getRequest().setAttribute("lgt", lgt);
			this.getRequest().setAttribute("lat", lat);
			form.setPath("/map.jsp");
			return "success";
		}else{
			return "failure";
		}
		
	}*/
}
