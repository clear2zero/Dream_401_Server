package sto.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BaseController {
	public static final String ERR = "err";//错误页面
	public static final String SUC = "suc";
	public  Map suc(){  
		Map m = new HashMap();
		m.put("success", true);
		m.put("msg", "操作成功");
		return m;
    }
	public  Map suc(String msg){  
		Map m = new HashMap();
		m.put("success", true);
		m.put("msg", msg);
		return m;
    }
	public  Map err(String tip){  
		Map m = new HashMap();
		m.put("success", false);
		m.put("msg", tip);
		return m;
    }
	
	public  String tip(Model model,Object o,String tip){  
		model.addAttribute("o",o);
		model.addAttribute("tip",tip);
		return ERR;
    }
	
	public  String tip2(HttpServletRequest request,HttpServletResponse response,Model model,Object o,String tip,String R){  
		if("ajax".equals(request.getParameter("f"))){
			PrintWriter out;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}  
	        out.print(tip);
	        return null;
		}else {
			 tip(model, model, tip);
			 return R;
		}
    }
	
	public void outPrint(HttpServletResponse response, String msg) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(msg);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeJson(HttpServletRequest request,HttpServletResponse response,ContentType ct,int resultCode,JSONObject json){
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType(ct.getName());
			response.setHeader("Result-Code", String.valueOf(resultCode));
			String jsonstr = URLEncoder.encode(JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect),"UTF-8");
			response.setContentLength(jsonstr.getBytes().length);
			if(resultCode != 0){
				response.setHeader("errorMessage", URLEncoder.encode(json.getString("msg"),"UTF-8"));// 对响应头信息中文编码
			}else {
				response.getWriter().write(URLEncoder.encode(JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect),"UTF-8"));
			}
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeStream(HttpServletRequest request,HttpServletResponse response,ContentType ct,int resultCode,String filename){
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType(ct.getName());
			response.setHeader("Result-Code", String.valueOf(resultCode));
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename,"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public enum ContentType{
		HTML {public String getName(){return "text/html;charset=UTF-8";}},
		JSON {public String getName(){return "application/json;charset=UTF-8";}},
		PLAIN {public String getName(){return "text/plain;charset=UTF-8";}},
		XML {public String getName(){return "text/xml;charset=UTF-8";}},
		FILE{public String getName(){return "application/x-msdownload";}},
		IMAGE{public String getName(){return "image/jpeg";}};
	    public abstract String getName();
	}
	public static void main(String[] args){
		System.out.println(ContentType.JSON.getName());
	}
}
