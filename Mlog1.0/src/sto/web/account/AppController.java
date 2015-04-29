
package sto.web.account;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import other.AuthProfile;
import sto.common.SimpleUpload;
import sto.common.util.Page;
import sto.common.web.BaseController;
import sto.model.account.App;
import sto.model.account.Module;
import sto.model.account.SysSettings;
import sto.service.account.AppService;
import sto.service.account.SysSettingsService;

/**
 * 用户管理
 * 功能：列表、分配角色
 * 
 */
@Controller
@RequestMapping(value = "/app")
@SuppressWarnings("unchecked")
public class AppController extends BaseController{
	@Resource
	SysSettingsService sysSettingsService;
	@Resource
	AppService appService;
	
	@RequestMapping("/add.action")
	public String add(Model model, HttpServletRequest request) {
		return "account/appSave";
	}
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public void saveDo(App r,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		MultipartFile multipartFile = multipartRequest.getFile("apk");
		String dowloadurl = "";
		
		if(multipartFile != null && !multipartFile.isEmpty()){
			SysSettings sysSettings = sysSettingsService.findUniqueBy("skey", "apkname");
			String filename = multipartFile.getOriginalFilename();
			int los = filename.lastIndexOf(".");
			String uploadFileNamePre = filename.substring(0,los);
			String uploadFileNameSuf = filename.substring(los,filename.length());
			String apkname = sysSettings.getValue();
			String basepath = request.getSession().getServletContext().getRealPath("/");
			String uploadpath = File.separator+"download"+File.separator;
			String tempfilename = uploadFileNamePre+uploadFileNameSuf;
			String publishname = apkname+uploadFileNameSuf;
			dowloadurl = uploadpath+tempfilename;
			
			try {
				boolean isSuccess1 = SimpleUpload.uploadByteFile(multipartFile.getInputStream(), basepath+uploadpath,tempfilename);
				boolean isSuccess2 = SimpleUpload.uploadByteFile(multipartFile.getInputStream(), basepath+uploadpath,publishname);
				if(isSuccess1 && isSuccess2){
					System.out.println("发布成功");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String apkversion = multipartRequest.getParameter("apkversion");
		//String downloadurl = multipartRequest.getParameter("downloadurl");
		String description = multipartRequest.getParameter("description");
		int isforceupdate = Integer.parseInt(multipartRequest.getParameter("isforceupdate"));
		int status = Integer.parseInt(multipartRequest.getParameter("status"));
		List<App> list = appService.find(" from App where apkversion='"+apkversion+"'", null);
		App	app = null;
		if(list != null && list.size()>0){
			app = list.get(0);
			app.setApkversion(apkversion);
			app.setDownloadurl(dowloadurl);
			app.setDescription(description);
			app.setIsforceupdate(isforceupdate);
			app.setStatus(status);
			app.setPublisher(curruser.getUser().getId());
			app.setPublishtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}else {
			app = new App();
			app.setApkversion(apkversion);
			app.setDownloadurl(dowloadurl);
			app.setDescription(description);
			app.setIsforceupdate(isforceupdate);
			app.setStatus(status);
			app.setPublisher(curruser.getUser().getId());
			app.setPublishtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		appService.save(app);
		response.setContentType("text/html;charset=utf-8"); 
		try {
			response.getWriter().write("{'success':'true','msg':'保存成功'}");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping("/list.action")
	public String list(Model model, HttpServletRequest request) {
		return "account/appList";
	}
	
	@RequestMapping("/getAppList.action")
	@ResponseBody
	public Map getAppList(Model model, HttpServletRequest request) {
		Map<String, Object> m = new HashMap<String, Object>();
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("apkversion", request.getParameter("apkversion"));
		List<Map<String,Object>>  list = appService.getAppList(p,paramap);
		m.put("rows", list);
		m.put("total", p.getTotalCount());
		return m;
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/update.action")
	public String update(HttpServletRequest request,Model model) {
		model.addAttribute("o",appService.get(Integer.parseInt(request.getParameter("id"))));
		return "account/appUpdate";
	}
	
	/**
	 * 修改保存
	 */
	@RequestMapping(value="/updateDo.action")
	@ResponseBody
	public Map updateDo(Module o,HttpServletRequest request) {
		String description = request.getParameter("description");
		int isforceupdate = Integer.parseInt(request.getParameter("isforceupdate"));
		int status = Integer.parseInt(request.getParameter("status"));
		App app = appService.get(o.getId());
		app.setDescription(description);
		app.setIsforceupdate(isforceupdate);
		app.setStatus(status);
		appService.update(app);
		return suc();
	}
}
