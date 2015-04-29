
package sto.web.account;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import other.AuthProfile;
import sto.common.HttpOnline;
import sto.common.HttpOnline.OnlineIType;
import sto.common.Md5Encrypt;
import sto.common.MlogPM;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.util.RoleType;
import sto.common.util.VerifyCodeUtil;
import sto.common.web.BaseController;
import sto.form.RegUnitForm;
import sto.model.account.SysSettings;
import sto.model.account.Unit;
import sto.model.account.UnitSettings;
import sto.model.account.User;
import sto.service.account.DeptService;
import sto.service.account.RoleService;
import sto.service.account.SysSettingsService;
import sto.service.account.UnitService;
import sto.service.account.UnitSettingsService;
import sto.service.account.UserService;

import com.alibaba.fastjson.JSONObject;

/**
 * 角色管理
 * 功能：增、删、改、查、分配权限
 * 
 */
@Controller
@RequestMapping(value = "/unit")
@SuppressWarnings("unchecked")
public class UnitAction extends BaseController{
	@Resource
	UnitService unitService;
	@Resource
	DeptService deptService;
	@Resource
	RoleService roleService;
	@Resource  
	UserService userService;
	@Resource  
    UnitSettingsService unitSettingsService;
	@Resource  
    SysSettingsService sysSettingsService;
	
	@RequestMapping("/list.action")
	public String list() {
		return "account/unitList";
	}
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJson(Model model, HttpServletRequest request) {

		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String hql = "";
		if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
			hql = "from Unit where isregistered=1 and divid='"+auth.getUser().getUnit().getDivid()+"'";
		}else {
			hql = "from Unit where isregistered=1 order by divid ";
		}
		Page<Unit> page = new Page<Unit>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		page.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Page<Unit> resultPage = unitService.find(page, hql, null);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("rows", resultPage.getResult()==null ? "":resultPage.getResult());
		m.put("total", resultPage.getTotalCount());
		return m;
	}
	@RequestMapping("/listJsonNoPage.action")
	@ResponseBody
	public List listJsonNoPage(Model model, HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String hql = "";
		
		if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
			hql = "from Unit where isregistered=1 and divid='"+auth.getUser().getUnit().getDivid()+"'";
		}else {
			hql = "from Unit where isregistered=1 order by divid ";
		}
		return unitService.find(hql, null);
	}
	
	@RequestMapping("/register.action")
	public String register() {
		return "account/unitReg";
	}
	
	@RequestMapping("/registerDo.action")
	@ResponseBody
	public Map registerDo(Unit unit,HttpServletRequest request) {
		List list = unitService.find("from Unit where 1=1 ", null);
		if(list != null && list.size()>0){
			return err("已存在注册单位，单位只需注册一次!");
		}
		unit.setProjectid(MlogPM.get("online.projectid"));
		unit.setParentid("0");//设置为顶级单位
		unit.setLicence(null == MlogPM.get("online.unit.licence") ? 0 : Integer.parseInt(MlogPM.get("online.unit.licence")));
		Map<String,String> map = new HashMap<String,String>();
		map.put("projectid", unit.getProjectid());
		map.put("parentid", unit.getParentid());
		map.put("divid", unit.getDivid());
		map.put("divname", unit.getDivname());
		map.put("adminuser", unit.getAdminuser());
		JSONObject json = HttpOnline.invoke(OnlineIType.INSERTPROJECT, map);
		if(json.getBooleanValue("success") == false){
			return err(json.getString("msg"));
		}else {
			unitService.save(unit);
			return suc();
		}
	}
	
	@RequestMapping("/unitUpdate.action")
	public String unitUpdate(Model model,HttpServletRequest request) {
		model.addAttribute("o",unitService.findUniqueBy("id",Integer.parseInt(request.getParameter("id"))));
		return "account/unitUpdate";
	}
	
	@RequestMapping("/unitUpdateDo.action")
	@ResponseBody
	public Map unitUpdateDo(Unit u,HttpServletRequest request) {
		String id = request.getParameter("id");
		Unit unit = unitService.findUniqueBy("id", Integer.parseInt(id));
		unit.setCorporation(u.getCorporation());
		unit.setLinkman(u.getLinkman());
		unit.setAddr(u.getAddr());
		unit.setTel(u.getTel());
		//unit.setAdminuser(u.getAdminuser());
		unitService.save(unit);
		return suc();
	}
	@RequestMapping("/unitQuery.action")
	public String unitQuery(Model model, HttpServletRequest request) {
		String hql = "from Unit where 1=1 ";
		List<Unit> list= unitService.find(hql, null);
		if(list != null && list.size()>0){
			model.addAttribute("o",list.get(0));
		}else {
			model.addAttribute("o",new Unit());
		}
		return "account/unitinfo";
	}
	
	@RequestMapping(value = "/frontRegister.action")
	public String register(Model model,HttpServletRequest request) {
		return "front/unitRegister1";
	}
	
	@RequestMapping(value = "/frontRegisterError.action")
	public String frontRegisterError(Model model,HttpServletRequest request) {
		return "front/registerError";
	}
	
	@RequestMapping(value = "/frontRegisterDo.action")
	public String registerDo(Model model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		//单位信息
		String divname = request.getParameter("divname");
		String addr = request.getParameter("addr");
		String linkman = request.getParameter("linkman");
		String corporation = request.getParameter("corporation");
		//单位管理员信息
		String name = request.getParameter("name");
		String identitycard = request.getParameter("identitycard");
		String mobilephone = request.getParameter("mobilephone");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verifycode = request.getParameter("verifycode");
		String rand=(String) request.getSession().getAttribute("rand");
		if(StringUtils.isBlank(verifycode) || !verifycode.equals(rand)){
			redirectAttributes.addFlashAttribute("exception", "验证码错误");
			return "redirect:/unit/frontRegisterError.action";
		}
		
		RegUnitForm form = new RegUnitForm();
		form.setDivname(divname);
		form.setAddr(addr);
		form.setLinkman(linkman);
		form.setCorporation(corporation);
		form.setName(name);
		form.setIdentitycard(identitycard);
		form.setMobilephone(mobilephone);
		form.setUsername(username);
		form.setPassword(password);
		//注册单位
		try {
			JSONObject json = unitService.initUnit1(form);
			if(!json.getBooleanValue("success")){
				redirectAttributes.addFlashAttribute("exception", json.getString("msg"));
				return "redirect:/unit/frontRegisterError.action";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("exception", e.getMessage());
			return "redirect:/unit/frontRegisterError.action";
		}
		
		Unit _unit = (Unit)unitService.find("from Unit where divname=:p1", new Parameter(divname)).get(0);
		User _user = (User) userService.find("from User where username=:p1 and divid=:p2", new Parameter(username,_unit.getDivid())).get(0);
		
		if(_unit.getAcceptno() == null || _unit.getAcceptno().equals("")){//判断是否已注册证书，未注册则通过在线为单位管理员注册个人软证书
			Map<String,String> map = new HashMap<String,String>();
			map.put("projectid", MlogPM.get("online.projectid"));
			map.put("businesstype", "04");
			map.put("username", name);
			map.put("operatorname", name);
			map.put("operatorphone", mobilephone);
			map.put("identitycard", identitycard);
			map.put("divid", "个人");
			map.put("serialnumber", "");
			JSONObject json = HttpOnline.invoke(OnlineIType.CERTAPPLY, map);
			if(json.getBooleanValue("success") == false){
				redirectAttributes.addFlashAttribute("exception", json.getString("msg"));
				return "redirect:/unit/frontRegisterError.action";
			}else {
				_unit.setAcceptno(json.getJSONObject("data").getString("acceptNo"));
				unitService.save(_unit);
			}
		}
			
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("acceptno", _unit.getAcceptno());
		map1.put("columnnames", "certcn");
		JSONObject json1 = HttpOnline.invoke(OnlineIType.GETAPPLYDATA, map1);
		if(json1.getBooleanValue("success") == false){
			redirectAttributes.addFlashAttribute("exception", json1.getString("msg"));
			return "redirect:/unit/frontRegisterError.action";
		}else{
			String certcn = ((JSONObject)json1.getJSONObject("data").getJSONArray("applyData").get(0)).getString("columnData");
			_user.setHcertcn(certcn);
			userService.save(_user);
			_unit.setAdminuser(certcn);			
			unitService.save(_unit);
			if(_unit.getIsregistered() == null || _unit.getIsregistered() == 0){//判断单位是否已注册，未注册则通过在线注册projectid项目下单位
				Map<String,String> map2 = new HashMap<String,String>();
				map2.put("projectid", _unit.getProjectid());
				map2.put("parentid", _unit.getParentid());
				map2.put("divid", _unit.getDivid());
				map2.put("divname", _unit.getDivname());
				map2.put("adminuser", _unit.getAdminuser());
				//map2.put("ischeck", "0");//无需审核
				//map2.put("lisencenum", _unit.getLicence() == null || _unit.getLicence() == 0 ? "20" : String.valueOf(_unit.getLicence()));
				//JSONObject json2 = HttpOnline.invoke(OnlineIType.INITUNIT, map2);
				JSONObject json2 = HttpOnline.invoke(OnlineIType.INSERTPROJECT, map2);
				if(json2.getBooleanValue("success") == false){
					_unit.setIsregistered(0);
					unitService.save(_unit);
					redirectAttributes.addFlashAttribute("exception", json2.getString("msg"));
					return "redirect:/unit/frontRegisterError.action";
				}else {
					_unit.setIsregistered(1);
					_unit.setInitstep(0);
					unitService.save(_unit);
					//单位注册时初始化单位设置参数
					List<UnitSettings> uslist = unitSettingsService.find("from UnitSettings where divid=:p1 ", new Parameter(_unit.getDivid()));
					List<SysSettings> list = sysSettingsService.find("from SysSettings where iscommon=0 ", null);
					if(uslist == null || uslist.size() <=0){
						for(SysSettings ss:list){
							UnitSettings us = new UnitSettings();
							us.setDivid(_unit.getDivid());
							us.setName(ss.getName());
							us.setSkey(ss.getSkey());
							us.setValue(ss.getValue());
							unitSettingsService.save(us);
						}
					}
					
					String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
					String onlineurl = MlogPM.get("online.url")+OnlineIType.CERTINSTALLSOFT.getName();
					redirectAttributes.addFlashAttribute("acceptno",_unit.getAcceptno());
					redirectAttributes.addFlashAttribute("url", basepath+"/account/login.action?");
					return "redirect:"+onlineurl+"?acceptno="+_unit.getAcceptno()+"&url="+basepath+"/account/login.action?";
				}
			}else {
				String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
				String onlineurl = MlogPM.get("online.url")+OnlineIType.CERTINSTALLSOFT.getName();
				redirectAttributes.addFlashAttribute("acceptno",_unit.getAcceptno());
				redirectAttributes.addFlashAttribute("url", basepath+"/account/login.action?");
				return "redirect:"+onlineurl+"?acceptno="+_unit.getAcceptno()+"&url="+basepath+"/account/login.action?";
			}
		}
	}
	
	@RequestMapping(value = "/frontUnitLoginDo.action")
	public String unitLoginDo(Model model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String acceptno = request.getParameter("acceptno");
		String divname = request.getParameter("divname");
		String rand=(String) request.getSession().getAttribute("rand");
		String code = request.getParameter("verifycode");
		if(StringUtils.isBlank(code) || !code.equals(rand)){
			model.addAttribute("exception", "验证码错误");
			return "front/frontRegisterError";
		}
		Unit unit = (Unit)unitService.find("from Unit where divname=:p1", new Parameter(divname)).get(0);
		if(unit.getIsregistered() == 1){
			String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			String onlineurl = MlogPM.get("online.url")+OnlineIType.CERTINSTALLSOFT.getName();
			redirectAttributes.addFlashAttribute("acceptno", acceptno);
			redirectAttributes.addFlashAttribute("url", basepath+"/account/login.action?");
			return "redirect:"+onlineurl+"?acceptno="+unit.getAcceptno()+"&url="+basepath+"/account/login.action?";
		}else {
			model.addAttribute("exception", "单位注册申请存在问题，请联系管理员");
			return "front/frontRegisterError";
		}
	}
	@RequestMapping("/frontQueryUnit.action")
	@ResponseBody
	public JSONObject frontQueryUnit(Model model, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rand=(String) request.getSession().getAttribute("rand");
		String code = request.getParameter("verifycode");
		if(StringUtils.isBlank(code) || !code.equals(rand)){
			json.put("success", false);
			json.put("error", "验证码错误");
		}else{
			User user = new User();
			user.setUsername(username);
			user.setPassword(Md5Encrypt.md5(password));
			User u = userService.getUnitManagerByNameAndPassWord(user);
			
			if(u == null){
				json.put("success", false);
				json.put("error", "无此用户");
			}else {
				Unit unit = u.getUnit();
				json.put("success", true);
				JSONObject json1 = new JSONObject();
				json1.put("divname", unit.getDivname());
				json1.put("addr", unit.getAddr());
				json1.put("tel", unit.getTel());
				json1.put("corporation", unit.getCorporation());
				json1.put("acceptno", unit.getAcceptno());
				json1.put("name", u.getName());
				json1.put("mobilephone", u.getMobilephone());
				json1.put("identitycard", u.getIdentitycard());
				json1.put("ok", "");
				json.put("data", json1);
			}
		}
		return json;
	}
	@RequestMapping("/frontRand.action")
	public void rand(HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		response.setContentType(ContentType.IMAGE.getName());
		VerifyCodeUtil vc=VerifyCodeUtil.Instance();
		request.getSession().setAttribute("rand", vc.getString());
		String rand=(String) request.getSession().getAttribute("rand");	
		System.out.print(rand);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(vc.getImage());
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] bt = new byte[1024*8];
			int bytesRead = 0;
			while(-1 != (bytesRead = bis.read(bt, 0, bt.length))){
				bos.write(bt);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bos != null){
				try {
					bos.close();
					bos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis != null){
				try {
					bis.close();
					bis = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping("/frontCheckcode.action")
	@ResponseBody
	public JSONObject checkcode(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String rand=(String) request.getSession().getAttribute("rand");
		String code = request.getParameter("verifycode");
		JSONObject json1 = new JSONObject();
		if(StringUtils.isBlank(code) || !code.equals(rand)){
			json1.put("error", "验证码错误");
		}else {
			String username = request.getParameter("username");
			String password = StringUtils.trimToEmpty(request.getParameter("password"));
			User user = new User();
			user.setUsername(username);
			user.setPassword(Md5Encrypt.md5(password));
			User u = userService.getUnitManagerByNameAndPassWord(user);
			if(u == null){
				json1.put("error", "请检查用户名或密码是否正确");
			}else {
				Unit unit = u.getUnit();
				json1.put("ok", "验证成功！");
				json1.put("success", true);
				json1.put("divname", unit.getDivname());
				json1.put("acceptno", unit.getAcceptno());
			}
		}
		json.put("data", json1);
		return json;
	}
	
	@RequestMapping("/frontCheckUsername.action")
	@ResponseBody
	public JSONObject checkUsername(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String username = request.getParameter("username");
		if(!StringUtils.isBlank(username)){
			if(userService.checkUsernameIsExist(username, null)){
				json.put("error", "登录名已存在");
			}else {
				json.put("ok", "");
			}
		}else {
			json.put("ok", "");
		}
		return json;
	}
	
	@RequestMapping("/frontCheckDivname.action")
	@ResponseBody
	public JSONObject checkDivname(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String divname = request.getParameter("divname");
		List list = unitService.find("from Unit where divname=:p1", new Parameter(divname));
		if(list == null || list.size() <= 0){
			json.put("ok", "");
		}else {
			json.put("error", "该企业已注册");
		}
		return json;
	}
	
	/**
	 * 只能调用isneedinit返回true调用此方法
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/fastinit.action")
	public String fastinit(Model model, HttpServletRequest request){
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		if(auth.getUser().getUnit().getInitstep() != null){
			model.addAttribute("step", auth.getUser().getUnit().getInitstep());
			request.setAttribute("step", auth.getUser().getUnit().getInitstep());
			request.setAttribute("divid", auth.getUser().getUnit().getDivid());
		}else {
			model.addAttribute("step", 0);
			request.setAttribute("step", 0);
			request.setAttribute("divid", auth.getUser().getUnit().getDivid());
		}
		return "account/fastinit";
	}
	
	@RequestMapping("/isneedinit.action")
	@ResponseBody
	public JSONObject isneedinit(HttpServletRequest request){
		JSONObject json = new JSONObject();
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		if(auth.getRole().getEnname().equals(RoleType.UNITADMIN.getName())){
			if(auth.getUser().getUnit().getInitstep() == null || auth.getUser().getUnit().getInitstep() != 3){
				json.put("success", true);
			}else {
				json.put("success", false);
			}
		}else {
			json.put("success", false);
		}
		return json;
	}
	@RequestMapping("/updateInitStatus.action")
	@ResponseBody
	public Map updateInitStatus(HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		int step = Integer.parseInt(request.getParameter("step"));
		Unit unit = auth.getUser().getUnit();
		unit.setInitstep(step);
		unitService.save(unit);
		return suc();
	}
}
