package sto.web.interfaces;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import other.CertParse;
import sto.common.Md5Encrypt;
import sto.common.SimpleUpload;
import sto.common.util.DateUtil;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.util.StringUtils;
import sto.common.web.BaseController;
import sto.form.AttendForm1;
import sto.form.AttendForm2;
import sto.form.AttendForm3;
import sto.form.JournalForm;
import sto.form.MsgForm;
import sto.form.PositionForm;
import sto.model.account.App;
import sto.model.account.Attend;
import sto.model.account.BugReport;
import sto.model.account.Dept;
import sto.model.account.Journal;
import sto.model.account.JournalReply;
import sto.model.account.Msg;
import sto.model.account.MsgReceiver;
import sto.model.account.MsgReply;
import sto.model.account.Position;
import sto.model.account.SysSettings;
import sto.model.account.Unit;
import sto.model.account.UnitSettings;
import sto.model.account.User;
import sto.model.account.UserSettings;
import sto.service.account.AppService;
import sto.service.account.AttendService;
import sto.service.account.BugReportService;
import sto.service.account.DataRulesService;
import sto.service.account.DeptService;
import sto.service.account.JournalReplyService;
import sto.service.account.JournalService;
import sto.service.account.MsgReceiverService;
import sto.service.account.MsgReplyService;
import sto.service.account.MsgService;
import sto.service.account.PositionService;
import sto.service.account.SysSettingsService;
import sto.service.account.UnitService;
import sto.service.account.UnitSettingsService;
import sto.service.account.UserService;
import sto.service.account.UserSettingsService;
import sto.utils.IdGen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hebca.pki.Cert;
import com.koal.svs.client.st.THostInfoSt;

@Controller
@RequestMapping(value = "/interface")
public class MobileController extends BaseController{
	private static Logger log = Logger.getLogger(MobileController.class);
	
	private static ContentType contentType = ContentType.JSON;
	@Resource(name = "hostInfo")
	THostInfoSt hostInfo;
	@Resource
	UserService userService;
	@Resource
	AttendService attendService;
	@Resource
	MsgService msgService;
	@Resource
	MsgReceiverService msgReceiverService;
	@Resource
	MsgReplyService msgReplyService;
	@Resource
	JournalService journalService;
	@Resource
	JournalReplyService journalReplyService;
	@Resource
	PositionService positionService;
	@Resource
	DeptService deptService;
	@Resource
	SysSettingsService sysSettingsService;
	@Resource
	UnitSettingsService unitSettingsService;
	@Resource
	DataRulesService dataRulesService;
	@Resource
	UnitService unitService;
	@Resource
	AppService appService;
	@Resource
	BugReportService bugReportService;
	@Resource
	UserSettingsService userSettingsService;
	
	@RequestMapping("/getUser.action")
	public void getUser(HttpServletRequest request,HttpServletResponse response){
		log.debug("getUser.action");
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		String username = request.getParameter("username");
		String password = Md5Encrypt.md5(StringUtils.trimToEmpty(request.getParameter("password")));
		String divid = request.getParameter("divid");
		log.debug(username+":"+divid);
		try {
			List<Unit> list = unitService.find("from Unit where 1=1 and isregistered=1 and divid=:p1", new Parameter(divid));
			if(list != null && list.size()>0){
				json1.put("projectid", list.get(0).getProjectid());
				json1.put("divname", list.get(0).getDivname());
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				Unit unit = new Unit();
				unit.setDivid(divid);
				user.setUnit(unit);
				// flag 1成功 2用户名不存在 3密码不正确 4用户名不能为空 5 密码不能为空
				int flag = userService.checkUserNameAndPassWordByDivid(user);
				if (flag == 1) {
					User u = userService.getUserByNameAndPassWordByDivid(user);
					json1.put("userid", u.getId());
					json1.put("name", StringUtils.trimToEmpty(u.getName()));
					json1.put("mobilephone", StringUtils.trimToEmpty(u.getMobilephone()));
					json1.put("identitycard", StringUtils.trimToEmpty(u.getIdentitycard()));
					json1.put("certcn", StringUtils.trimToEmpty(u.getScertcn()));
					
					json.put("success", true);
					json.put("data", json1);
					resultCode = 0;
				} else if (flag == 2) {
					json.put("success", false);
					json.put("msg", "该单位无此用户");
					resultCode = 0;
				} else if (flag == 3) {
					json.put("success", false);
					json.put("msg", "账户已冻结");
					resultCode = 0;
				} else if (flag == 4) {
					json.put("success", false);
					json.put("msg", "密码错误");
					resultCode = 0;
				}
			}else {
				json.put("success", false);
				json.put("msg", "该单位不存在或单位已冻结");
				resultCode = 0;
			}
			
			super.writeJson(request,response,contentType, resultCode, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/login.action")
	public void login(HttpServletRequest request,HttpServletResponse response){
		log.debug("login.action");
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String divid = request.getParameter("divid");
			String imei = request.getParameter("imei");
			String deviceid = request.getParameter("deviceid");
			String packageversion = request.getParameter("packageversion");
			log.debug(username+":"+divid+":imei:"+imei);
			System.out.println(username+":"+divid+":imei:"+imei+";deviceid:"+deviceid);
			// 进行证书验证
			String error = "";
			String cert = request.getParameter("cert");
			String random = request.getParameter("random");
			String randomsign = request.getParameter("randomsign");

			if (cert == null) {
				cert = "";
			}
			if (random == null) {
				random = "";
			}
			if (randomsign == null) {
				randomsign = "";
			}
			if (cert != null && random != null && randomsign != null) {
				int result = -1;
				String uniqueFlag = ""; // 证书唯一标示
				try {
					System.out.println("解析证书"+cert );
					byte[] arrayOriginData = random.getBytes();
					int nOriginDataLen = arrayOriginData.length;
					//SvsClientHelper svsclient = SvsClientHelper.getInstance();
					// 与svs服务器建立连接
					//svsclient.initialize(hostInfo.getSvrIP(), hostInfo.getPort(), 1000, false, 5000);
					//result = svsclient.verifyCertSign(-1, 0, arrayOriginData,
					//		nOriginDataLen, cert, randomsign, 1, hostInfo);
					// 进行证书验证，包含三方面验证：（1）用户身份验证 （2）证书状态验证 （3）证书有效期验证
					System.out.println("result证书监测"+result );
					result = 0;
					if (result == 0) {
						CertParse cp = new CertParse(new Cert(cert)); // 解析证书
						
						String CN = cp.getSubject(CertParse.DN_CN); // 获取证书CN号
						String gName = cp.getSubject(CertParse.DN_GIVENNAME); // 获取证书名称
						if (CN.length() > gName.length()) { // 通过判断这两个字段的长度来确定哪个是证书唯一标示。
							uniqueFlag = CN;
						} else {
							uniqueFlag = gName;
						}
						System.out.println("获取cn"+uniqueFlag );
						if(!StringUtils.isBlank(uniqueFlag)){
							// flag 1成功 2证书未绑定 3证书不存在
							Map<String,String> map = new HashMap<String,String>();
							map.put("scertcn", uniqueFlag);
							map.put("imei", imei);
							map.put("deviceid", deviceid);
							int flag = userService.checkScertcn(map);
							if (flag == 1 || flag == 4) {
								User u = userService.getUserByScertcn(uniqueFlag);
								if(u.getUnit() == null || u.getUnit().getDivid() == null || u.getUnit().getDivid().trim().equals("")){
									json.put("success", false);
									json.put("msg", "非单位用户");
									resultCode = 0;
								}else{
									json1.put("userid", u.getId());
									json1.put("name", StringUtils.trimToEmpty(u.getName()));
									json1.put("deptid", u.getDept().getDeptid());
									json1.put("deptname", StringUtils.trimToEmpty(u.getDept().getDeptname()));
									json1.put("clientrole", StringUtils.trimToEmpty(String.valueOf(u.getClientrole())));
									if(flag == 4){//未与任何设备绑定，则绑定该设备
										u.setImei(imei);
										u.setDeviceid(deviceid);
										//userService.save(u);
									}
									u.setPackageversion(packageversion);
									userService.save(u);
									json.put("success", true);
									json.put("data", json1);
									resultCode = 0;
								}
							} else if (flag == 2) {
								if(null != username && !username.trim().equals("")
										&& null != password && !password.trim().equals("")){
									User user = new User();
									user.setUsername(username);
									user.setPassword(Md5Encrypt.md5(password));
									Unit unit = new Unit();
									unit.setDivid(divid);
									user.setUnit(unit);
									// flag 1成功 2用户名不存在
									int flag1 = userService.checkUserNameAndPassWordByDivid(user);
									if (flag1 == 1) {
										User u = userService.getUserByNameAndPassWordByDivid(user);
										String certcn = StringUtils.trimToEmpty(u.getScertcn());
										if("".equals(certcn)){//用户未绑定软证书
											u.setScertcn(uniqueFlag);//绑定证书
											u.setImei(imei);
											u.setPackageversion(packageversion);//记录用户使用版本
											userService.save(u);
											json1.put("userid", u.getId());
											json1.put("name", StringUtils.trimToEmpty(u.getName()));
											json1.put("deptid", u.getDept().getDeptid());
											json1.put("deptname", StringUtils.trimToEmpty(u.getDept().getDeptname()));
											json1.put("clientrole", StringUtils.trimToEmpty(String.valueOf(u.getClientrole())));
											
											json.put("success", true);
											json.put("data", json1);
											resultCode = 0;
										}else {//用户已绑定软证书
											json.put("success", false);
											json.put("msg", "该用户已绑定其他证书，请尝试其他用户名，或联系管理员解除绑定");
											resultCode = 0;
										}
									}else if (flag1 == 2) {
										json.put("success", false);
										json.put("msg", "该单位无此用户");
										resultCode = 0;
									} else if (flag1 == 3) {
										json.put("success", false);
										json.put("msg", "账户已冻结");
										resultCode = 0;
									} else if (flag1 == 4) {
										json.put("success", false);
										json.put("msg", "密码错误");
										resultCode = 1;
									}
								}else {
									json1.put("userid", -1);//证书未绑定用户,userid返回-1
									json1.put("name", "");
									json1.put("deptid", -1);
									json1.put("deptname", "");
									json1.put("clientrole", "");
									
									json.put("success", true);//证书未绑定用户
									json.put("data", json1);
									resultCode = 0;
								}
							} else if (flag == 3) {
								json.put("success", false);
								json.put("msg", "证书不存在");
							} else if (flag == 5){
								json.put("success", false);
								json.put("msg", "该证书已绑定其他设备");
							}else if (flag == 6){
								json.put("success", false);
								json.put("msg", "该账户已冻结");
							} else {
								json.put("success", false);
								json.put("msg", "未知错误");
							}
						}
					} else {
						// 验证失败
						switch (result) {
						case -1:
							error = "(无法连接svs服务器)";
							break;
						case 2:
							error = "(证书已经过期，需要延期后才能使用)";
							break;
						case -6805:
							error = "(无效的证书文件)";
							break;
						case -6406:
							error = "(签名验证失败)";
							break;
						default:
							error = "(errorcode:" + result + ")";
						}
						System.out.println("errorcode验签异常"+result );
						json.put("success", false);
						json.put("msg", "验签错误码："+result+";错误信息："+error);
					}
				} catch (Exception e) {
					e.printStackTrace();
					json.put("success", false);
					json.put("msg", "验签异常："+e.getMessage());
				}					

			}else {
				json.put("success", false);
				json.put("msg", "获取证书异常");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.writeJson(request,response,contentType, resultCode, json);
	}
	@RequestMapping("/attend.action")
	public void attend(HttpServletRequest request,HttpServletResponse response){
		log.debug("attend.action");
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String lgt = request.getParameter("longitude");
		String lat = request.getParameter("latitude");
		String address = request.getParameter("address");
		int type = Integer.parseInt(request.getParameter("type"));
		log.debug(userid+":"+lgt+":"+lat);
		Attend ad = null;
		if(type == 1){//上班卡
			ad = new Attend();
			ad.setUserid(userid);
			ad.setOnlgt(lgt);
			ad.setOnlat(lat);
			ad.setOnaddr(address);
			ad.setOntime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			ad.setStatus(0);
			ad.setLasttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			User user = userService.get(userid);
			if(user.getUnit() == null){
				json1.put("frequency", 10*60*1000);
			}else {
				List<UnitSettings> list1 = unitSettingsService.find(" from UnitSettings where skey='frequency' and divid=:p1", new Parameter(user.getUnit().getDivid()));
	    		if(list1 != null && list1.size()>0){
	    			UnitSettings unitSettings = list1.get(0);
	    			json1.put("frequency", Integer.parseInt(unitSettings.getValue())*60*1000);
	    		}else{
	    			json1.put("frequency", 10*60*1000);
	    		}
			}
			
		}else {//下班卡
			ad = attendService.getLastAttend(userid);
			Date date = new Date();
			Date ondate = DateUtil.stringToDate(ad.getOntime(), "yyyy-MM-dd");//上班卡日期
			Date offdate = DateUtil.dateToDate(date, "yyyy-MM-dd");//下班卡日期
			if(ondate.before(offdate)){//隔天打卡
				//隔天打下班卡情况，添加最后一次打上班卡当天的下班卡记录
				ad.setOfflgt(lgt);
				ad.setOfflat(lat);
				ad.setOffaddr(address);
				ad.setOfftime(DateUtil.dateToString(ondate, "yyyy-MM-dd")+" 23:59:59");
				ad.setStatus(1);
				ad.setLasttime(DateUtil.dateToString(ondate, "yyyy-MM-dd")+" 23:59:59");
				
				//隔天打下班卡情况，添加打下班卡当天的上下班卡记录
				Attend extra_ad = new Attend();
				extra_ad.setUserid(userid);
				extra_ad.setOnlgt(lgt);
				extra_ad.setOnlat(lat);
				extra_ad.setOnaddr(address);
				extra_ad.setOntime(DateUtil.dateToString(offdate, "yyyy-MM-dd")+" 00:00:00");
				//extra_ad.setStatus(0);
				extra_ad.setOfflgt(lgt);
				extra_ad.setOfflat(lat);
				extra_ad.setOffaddr(address);
				extra_ad.setOfftime(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
				extra_ad.setStatus(1);
				
				extra_ad.setLasttime(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
				attendService.save(extra_ad);
			}else {
				ad.setOfflgt(lgt);
				ad.setOfflat(lat);
				ad.setOffaddr(address);
				ad.setOfftime(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
				ad.setStatus(1);
				ad.setLasttime(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
			}
		}
		
		attendService.save(ad);
		
		json.put("success", true);
		json.put("data", json1);
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getLastAttend.action")
	public void getLastAttend(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		String userid = request.getParameter("userid");
		Attend ad = attendService.getLastAttend(Integer.parseInt(userid));
		if(ad != null){
			if(ad.getStatus() == 0){
				json1.put("time", StringUtils.trimToEmpty(ad.getOntime()));
				json1.put("longitude", StringUtils.trimToEmpty(ad.getOnlgt()));
				json1.put("latitude", StringUtils.trimToEmpty(ad.getOnlat()));
				json1.put("address", StringUtils.trimToEmpty(ad.getOnaddr()));
				json1.put("type", 1);
			}else{
				json1.put("time", StringUtils.trimToEmpty(ad.getOfftime()));
				json1.put("longitude", StringUtils.trimToEmpty(ad.getOfflgt()));
				json1.put("latitude", StringUtils.trimToEmpty(ad.getOfflat()));
				json1.put("address", StringUtils.trimToEmpty(ad.getOffaddr()));
				json1.put("type", 2);
			}
		}
		json.put("success", true);
		json.put("data", json1);
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getAttendStatisticsList.action")
	public void getAttendStatisticsList(HttpServletRequest request,HttpServletResponse response){
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String getuserid = request.getParameter("getuserid");//接口要求只能传一个人
		String date = request.getParameter("date");
		String badonly = request.getParameter("badonly");
		String pagenum = request.getParameter("pagenum");
		String pagesize = request.getParameter("pagesize");
		
		JSONObject json1 = new JSONObject();
		getuserid = dataRulesService.filterAuthUser(1,userid,getuserid);
		AttendForm1 form1 = attendService.getBadAttendMonthStatistics1(userid, getuserid, date);
		json1.put("userid", form1.getUserid());
		json1.put("username", form1.getUsername());
		json1.put("baddays", form1.getBaddays());
		
		JSONArray arr1 = new JSONArray();
		Map<String,String> map = new HashMap<String,String>();
		map.put("getuserid", getuserid);
		map.put("date", date);
		map.put("badonly", badonly);
		map.put("pagenum", pagenum);
		map.put("pagesize", pagesize);
		//获取用户当月考勤异常天数
		JSONObject result = attendService.getIAttendListByUserid(map);
		List<AttendForm2> list = (List<AttendForm2>)result.get("rows");
		json1.put("total", result.getIntValue("total"));
		for(AttendForm2 form2 : list){
			JSONObject json2 = new JSONObject();
			json2.put("date", form2.getDate());
			json2.put("worktime", form2.getWorktime());
			json2.put("isbad", form2.getIsbad());
			
			JSONArray arr2 = new JSONArray();
			//获取用户某天的打卡记录
			List<AttendForm3> list1 = attendService.getAttendCardListByUserid(getuserid, form2.getDate());
			for(AttendForm3 form3 : list1){
				JSONObject json3 = new JSONObject();
				json3.put("type", form3.getType());
				json3.put("time", form3.getTime());
				json3.put("longitude", form3.getLongitude());
				json3.put("latitude", form3.getLatitude());
				json3.put("address", form3.getAddress());
				arr2.add(json3);
			}
			json2.put("attendlist", arr2);
			arr1.add(json2);
		}
		json1.put("records", arr1);		
		JSONObject json = new JSONObject();
		json.put("data", json1);		
		json.put("success", true);
		log.info("super类"+super.hashCode()+"response::"+response.hashCode()+"考勤记录：："+json);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/sendMsg.action")
	public void sendMsg(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		int resultCode = 0;
		//boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		resolver.setResolveLazily(true);
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		MultipartFile multipartFile = multipartRequest.getFile("image");
		System.out.println("=========================="+multipartRequest.getContentType());
		String image = "";
		if(multipartFile != null){
			SysSettings sysSettings = sysSettingsService.findUniqueBy("skey", "msgimages");
			String filename = multipartFile.getOriginalFilename();
			int los = filename.lastIndexOf(".");
			multipartFile.getName();
			String uploadFileNamePre = filename.substring(0,los);
			String uploadFileNameSuf = filename.substring(los,filename.length());
			String basepath = sysSettings.getValue();
			String uploadpath = "M"+new SimpleDateFormat("yyyyMMdd").format(new Date())+File.separator;
			String tempfilename = uploadFileNamePre+"_"+IdGen.uuid()+uploadFileNameSuf;
			image = uploadpath+tempfilename;
			try {
				boolean isSuccess = SimpleUpload.uploadByteFile(multipartFile.getInputStream(), basepath+uploadpath,tempfilename);
				if(isSuccess){
					System.out.println("msg图片上传成功");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		String userid = multipartRequest.getParameter("userid");
		String content = multipartRequest.getParameter("content");
		String receivers = multipartRequest.getParameter("receivers");
		String lgt = multipartRequest.getParameter("longitude");
		String lat = multipartRequest.getParameter("latitude");
		String addr = multipartRequest.getParameter("address");
		
		request.setAttribute("receivers", receivers);//用于给拦截器传递参数
		
		Msg msg = new Msg();
		msg.setPublisher(Integer.parseInt(userid));
		msg.setContent(content);
		msg.setImage(image);
		msg.setLgt(lgt);
		msg.setLat(lat);
		msg.setAddr(addr);
		msg.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		msgService.save(msg);
		
		MsgReceiver msgReceiver = null;
		String[] userids = receivers.split(",");
		if(!Arrays.asList(userids).contains(userid)){
			msgReceiver = new MsgReceiver();
			msgReceiver.setMsg(msg);
			msgReceiver.setReceiver(Integer.parseInt(userid));
			msgReceiverService.save(msgReceiver);
		}
		for(String receiver : userids){
			msgReceiver = new MsgReceiver();
			msgReceiver.setMsg(msg);
			msgReceiver.setReceiver(Integer.parseInt(receiver));
			msgReceiverService.save(msgReceiver);
		}
		JSONObject json1 = new JSONObject();
		json1.put("image", image);
		json.put("success", true);
		json.put("data", json1);
		log.info("发送消息：："+json);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getMsgList.action")
	public void getMsgList(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray arr = new JSONArray();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String getuserids = request.getParameter("getuserid");
		int pagenum = Integer.parseInt(request.getParameter("pagenum"));
		int pagesize = Integer.parseInt(request.getParameter("pagesize"));
		Page<Object[]> page = new Page<Object[]>(pagesize);
		page.setPageNo(pagenum);
		List<MsgForm> list = msgService.getMsgList(userid, getuserids,page);
		if(list.size()>0){			
			arr.addAll(list);
			json1.put("total", page.getTotalCount());
			json1.put("records", arr);
			
		}
		json.put("data", json1);
		json.put("success", true);
		log.info("获取消息：："+json);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/replyMsg.action")
	public void replyMsg(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		int msgid = Integer.parseInt(request.getParameter("replyid"));
		String replytostr = request.getParameter("replyto");
		int replyto = Integer.parseInt(StringUtils.isBlank(replytostr) ? "0" : replytostr);
		String content = request.getParameter("content");
		
		MsgReply msgReply = new MsgReply();
		msgReply.setMsg(msgService.get(msgid));
		msgReply.setRecontent(content);
		msgReply.setRedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		msgReply.setReplyer(userid);
		msgReply.setReplyto(replyto);
		msgReplyService.save(msgReply);
		
		json.put("success", true);
		json.put("data", new JSONObject());
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/writeJournal.action")
	public void writeJournal(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		int resultCode = 0;
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		MultipartFile multipartFile = multipartRequest.getFile("image");
		String image = "";
		if(multipartFile != null){
			SysSettings sysSettings = sysSettingsService.findUniqueBy("skey", "logimages");
			String filename = multipartFile.getOriginalFilename();
			int los = filename.lastIndexOf(".");
			String uploadFileNamePre = filename.substring(0,los);
			String uploadFileNameSuf = filename.substring(los,filename.length());
			String basepath = sysSettings.getValue();
			String uploadpath = "L"+new SimpleDateFormat("yyyyMMdd").format(new Date())+File.separator;
			String tempfilename = uploadFileNamePre+"_"+IdGen.uuid()+uploadFileNameSuf;
			image = uploadpath+tempfilename;
			try {
				boolean isSuccess = SimpleUpload.uploadByteFile(multipartFile.getInputStream(), basepath+uploadpath,tempfilename);
				if(isSuccess){
					System.out.println("日志图片上传成功");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int userid = Integer.parseInt(multipartRequest.getParameter("userid"));
		String content = multipartRequest.getParameter("content");
		//String image = multipartRequest.getParameter("image");
		String lgt = multipartRequest.getParameter("longitude");
		String lat = multipartRequest.getParameter("latitude");
		String addr = multipartRequest.getParameter("address");
		//String costtime = request.getParameter("costtime");
		
		request.setAttribute("userid", userid);//用于给拦截器传递参数
		
		Journal journal = new Journal();
		journal.setWriter(userid);
		journal.setContent(content);
		journal.setImage(image);
		journal.setLgt(lgt);
		journal.setLat(lat);
		journal.setAddr(addr);
		//journal.setCosttime(costtime);
		journal.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		journalService.save(journal);
		
		JSONObject json1 = new JSONObject();
		json1.put("image", image);
		
		json.put("success", true);
		json.put("data", json1);
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getJournalList.action")
	public void getJournalList(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray arr = new JSONArray();
		int resultCode = 0;
		int pagenum = Integer.parseInt(request.getParameter("pagenum"));
		int pagesize = Integer.parseInt(request.getParameter("pagesize"));
		Page<Object[]> page = new Page<Object[]>(pagesize);
		page.setPageNo(pagenum);
		Map<String,Object> parammap = new HashMap<String,Object>();
		parammap.put("userid", request.getParameter("userid"));
		parammap.put("getuserid", request.getParameter("getuserid"));
		parammap.put("date", request.getParameter("date"));
		List<JournalForm> list = journalService.getJournalList(page,parammap);
		if(list.size()>0){			
			arr.addAll(list);
			json1.put("total", page.getTotalCount());
			json1.put("records", arr);
		}
		json.put("data", json1);
		json.put("success", true);
		log.info("获取日志：："+json);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/replyJournal.action")
	public void replyJournal(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		int journalid = Integer.parseInt(request.getParameter("replyid"));
		String content = request.getParameter("content");
		String replytostr = request.getParameter("replyto");
		int replyto = Integer.parseInt(StringUtils.isBlank(replytostr) ? "0" : replytostr);
		
		JournalReply journalReply = new JournalReply();
		journalReply.setJournal(journalService.get(journalid));
		journalReply.setRecontent(content);
		journalReply.setRedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		journalReply.setReplyer(userid);
		journalReply.setReplyto(replyto);
		journalReplyService.save(journalReply);
		
		json.put("success", true);
		json.put("data", new JSONObject());
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/sendPosition.action")
	public void sendPosition(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String longitude = request.getParameter("longitude");
		String latitude = request.getParameter("latitude");
		String address = request.getParameter("address");
		String forcesend = request.getParameter("forcesend");
		Position p = new Position();
		p.setSender(userid);
		p.setLgt(longitude);
		p.setLat(latitude);
		p.setAddress(address);
		p.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if(forcesend == null || forcesend.equals("0")){//自动上传
			Attend ad = attendService.getLastAttend(userid);
			if(ad != null && ad.getStatus() == 0){//自动上传，仅仅上班时才保存定位信息
				json1.put("type", 1);
				positionService.save(p);
			}else {
				json1.put("type", 2);
			}
		}else {
			positionService.save(p);
		}
		json.put("success", true);
		json.put("data", json1);
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getLastPosition.action")
	public void getLastPosition(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray arr = new JSONArray();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String getuserids = request.getParameter("getuserid");
		List<PositionForm> list = positionService.getLastPosition(userid, getuserids);
		if(list.size()>0){			
			arr.addAll(list);
			json1.put("records", arr);
		}
		json.put("data", json1);
		json.put("success", true);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getTrack.action")
	public void getTrack(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray arr = new JSONArray();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String getuserids = request.getParameter("getuserid");
		String date = request.getParameter("date");
		List<PositionForm> list = positionService.getPositions(userid, date, getuserids);
		if(list.size()>0){			
			arr.addAll(list);
			json1.put("records", arr);
		}
		json.put("data", json1);
		json.put("success", true);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getContacts.action")
	public void getContacts(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String signcert = request.getParameter("signcert");
		String signdata = request.getParameter("signdata");
		String lastupdated = request.getParameter("lastupdated");
		
		User user = userService.get(userid);
		Parameter parameter = new Parameter();
		parameter.put("p1", user.getUnit().getDivid());
		List<Dept> list = deptService.findBySql("select * from mlog_dept a where a.parentid is null and a.divid=:p1",parameter,Dept.class);
		PropertyFilter filter = new PropertyFilter(){
			public boolean apply(Object source,String name,Object value){
				if(source instanceof Dept){
					if("deptid".equals(name)){
						return true;
					}
					if("deptname".equals(name)){
						return true;
					}
					if("depts".equals(name)){
						return true;
					}
					if("contacts".equals(name)){
						return true;
					}
				}else if(source instanceof User){
					if("userid".equals(name)){
						return true;
					}
					if("name".equals(name)){
						return true;
					}
					if("mobilephone".equals(name)){
						return true;
					}
				}
				
				return false;
			}
		};
		User user1 = userService.get(userid);
		UnitSettings unitSettings = (UnitSettings)unitSettingsService.find(" from UnitSettings where skey='lastupdated' and divid=:p1", new Parameter(user1.getUnit().getDivid())).get(0);
		String value = unitSettings.getValue();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");//get方式请求，url进行了utf-8编码空格转换为+
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date lastUpdateTime = null;
		Date sysLastUpdateTime = null;
		boolean isUpdated = true;
		try {
			if(lastupdated == null){
				isUpdated = true;
			}else {
				lastUpdateTime = sdf.parse(lastupdated);
				sysLastUpdateTime = sdf1.parse(value);
				isUpdated = lastUpdateTime.before(sysLastUpdateTime);
			}
		} catch (ParseException e) {
			json.put("success", false);
			json.put("msg", "获取通讯录最后更新时间异常");
			e.printStackTrace();
		}
		json.put("success", true);
		JSONObject json1 = new JSONObject();
		
		json1.put("modified", isUpdated);
		if(isUpdated){
			json1.put("updatedTime", value);
			json1.put("depts", JSON.parseArray(JSON.toJSONString(list,filter,SerializerFeature.DisableCircularReferenceDetect)));
		}
		json.put("data", json1);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getAuthContacts.action")
	public void getAuthContacts(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		int type = Integer.parseInt(request.getParameter("type"));
		String userids = dataRulesService.getDataAuth(userid, type);
		if(userids != null){	
			json1.put("userids", userids);
		}
		json.put("data", json1);
		json.put("success", true);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	@RequestMapping("/download.action")
	public void download(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		String filename = request.getParameter("filename");
		String basepath = "";
		if(filename.startsWith("M")){//消息
			SysSettings sysSettings = sysSettingsService.findUniqueBy("skey", "msgimages");
			basepath = sysSettings.getValue();
		}else if(filename.startsWith("L")){//日志
			SysSettings sysSettings = sysSettingsService.findUniqueBy("skey", "logimages");
			basepath = sysSettings.getValue();
		}else if(filename.startsWith("U")){//
			basepath = request.getRealPath("/usersettings");
		}
		if(!basepath.endsWith("\\")){
			basepath += File.separator;
		}
		String filepath = basepath+filename;
		File file = new File(filepath);
		response.setCharacterEncoding("utf-8");
		response.setContentType(ContentType.FILE.getName());
		//response.setContentLength((int)file.length());
		response.setHeader("Result-Code", String.valueOf(resultCode));
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
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
	
	@RequestMapping("/checkUpdate.action")
	public void checkUpdate(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		int resultCode = 0;
		//int userid = Integer.parseInt(request.getParameter("userid"));
		String apkversion = request.getParameter("apkversion");
		App app = appService.getLastVersion();
		int result = 0;
		if(app != null){
			String basepath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			String[] ver_a = apkversion.split("\\.");//客户端版本
			String[] ver_b = app.getApkversion().split("\\.");
			for(int i=0;i<ver_a.length;i++){
				int a = Integer.parseInt(ver_a[i]);
				int b = Integer.parseInt(ver_b[i]);
				if(a > b){//客户端版本高于服务端发布最新版本
					result = 1;
					break;
				}
				if(a < b){//客户端版本低于服务端发布最新版本
					result = -1;
					break;
				}
				result = 0;
			}
			if(result == 0){//客户端版本与服务端发布版本相同
				if(app.getStatus()==0){//正常
					json1.put("isupdate", 0);
				}else if(app.getStatus()==1){//停用
					json1.put("isupdate", 0);
				}else if(app.getStatus()==2){//回滚
					json1.put("isupdate", 1);
					json1.put("isforceupdate", 1);
					json1.put("downloadurl", basepath+app.getDownloadurl().replace(File.separator, "/"));
					json1.put("description", app.getDescription());
				}
			}else if(result == -1){//客户端版本低于服务端发布最新版本
				if(app.getStatus()==0){//正常
					json1.put("isupdate", 1);
					json1.put("isforceupdate", app.getIsforceupdate());
					json1.put("downloadurl", basepath+app.getDownloadurl().replace(File.separator, "/"));
					json1.put("description", app.getDescription());
				}else if(app.getStatus()==1){//停用
					json1.put("isupdate", 0);
				}else if(app.getStatus()==2){//回滚
					json1.put("isupdate", 1);
					json1.put("isforceupdate", 1);
					app = appService.getLastNormalVersion();//获取发布最新的正常版本进行回滚
					json1.put("downloadurl", basepath+app.getDownloadurl().replace(File.separator, "/"));
					json1.put("description", app.getDescription());
				}
			}else{
				json1.put("isupdate", 0);
			}
		}else {
			json1.put("isupdate", 0);
		}
		
		json.put("data", json1);
		json.put("success", true);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	@RequestMapping("/bindpush.action")
	public void bindpush(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		String certcn = request.getParameter("certcn");
		String pushchannelid = request.getParameter("channelid");//baidu返回值
		String pushuserid = request.getParameter("userid");//baidu返回值
		System.out.println("调用bindpush"+":"+certcn+":"+pushchannelid+":"+pushuserid);
		User user = userService.getUserByScertcn(certcn);
		/*if(StringUtils.isBlank(user.getPushchannelid()) || StringUtils.isBlank(user.getPushuserid())){
			user.setPushchannelid(pushchannelid);
			user.setPushuserid(pushuserid);
			json.put("success", true);
			json.put("data", new JSONObject());
		}else {
			String str = user.getPushchannelid()+user.getPushuserid();
			String str1 = pushchannelid+pushuserid;
			if(str.equals(str1)){
				user.setPushchannelid(pushchannelid);
				user.setPushuserid(pushuserid);
				json.put("success", true);
				json.put("data", new JSONObject());
			}else {
				json.put("success", false);
				json.put("msg", "该证书已绑定其他设备");
			}
		}*/
		user.setPushchannelid(pushchannelid);
		user.setPushuserid(pushuserid);
		userService.save(user);
		json.put("success", true);
		json.put("data", new JSONObject());
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/bugreport.action")
	public void bugreport(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		String devicetype = request.getParameter("devicetype");
		String platform = request.getParameter("platform");
		String phoneid = request.getParameter("phoneid");
		String packagename = request.getParameter("packagename");
		String packageversion = request.getParameter("packageversion");
		String exceptiontime = request.getParameter("exceptiontime");
		String stacktrace = request.getParameter("stacktrace");
		
		BugReport bug = new BugReport();
		bug.setDevicetype(devicetype);
		bug.setPlatform(platform);
		bug.setPhoneid(phoneid);
		bug.setPackagename(packagename);
		bug.setPackageversion(packageversion);
		bug.setExceptiontime(exceptiontime);
		bug.setStacktrace(stacktrace);
		bug.setUploadtime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		bugReportService.save(bug);
		
		json.put("success", true);
		json.put("data", new JSONObject());
		
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/userconfig.action")
	public void userconfig(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		int resultCode = 0;
		//boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		resolver.setResolveLazily(true);
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
		int userid = Integer.parseInt(multipartRequest.getParameter("userid"));
		String skey = multipartRequest.getParameter("key");
		String value = multipartRequest.getParameter("value");
		String description = multipartRequest.getParameter("description");
		
		MultipartFile multipartFile = multipartRequest.getFile("attachment");
		String attachment = "";
		if(multipartFile != null){
			String filename = multipartFile.getOriginalFilename();
			int los = filename.lastIndexOf(".");
			multipartFile.getName();
			String uploadFileNamePre = filename.substring(0,los);
			String uploadFileNameSuf = filename.substring(los,filename.length());
			String basepath = multipartRequest.getRealPath("/usersettings")+File.separator;
			String uploadpath = "U"+userid+File.separator;
			String tempfilename = uploadFileNamePre+"_"+IdGen.uuid()+uploadFileNameSuf;
			attachment = uploadpath+tempfilename;
			try {
				boolean isSuccess = SimpleUpload.uploadByteFile(multipartFile.getInputStream(), basepath+uploadpath,tempfilename);
				if(isSuccess){
					System.out.println("用户自定义文件上传成功");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Parameter parameter = new Parameter();
		parameter.put("p1", userid);
		parameter.put("p2", skey);
		List<UserSettings> list = userSettingsService.findBySql("select * from mlog_user_settings a where a.userid=:p1 and a.skey=:p2", parameter, UserSettings.class);
		UserSettings us = null;
		if(list == null || list.size() <= 0){
			us = new UserSettings();
			us.setUserid(userid);
			us.setSkey(skey);
			if(StringUtils.isBlank(attachment)){
				us.setValue(value);
			}else {
				us.setValue(attachment);
			}
			us.setDescription(description);
			us.setCreatetime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}else {
			us = list.get(0);
			if(StringUtils.isBlank(attachment)){
				us.setValue(value);
			}else {
				us.setValue(attachment);
			}
			us.setDescription(description);
			us.setCreatetime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
		userSettingsService.save(us);
		JSONObject json1 = new JSONObject();
		json.put("success", true);
		json.put("data", json1);
		log.info("用户自定义：："+json);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	@RequestMapping("/getuserconfig.action")
	public void getuserconfig(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int resultCode = 0;
		int userid = Integer.parseInt(request.getParameter("userid"));
		String skey = request.getParameter("key");
		Parameter parameter = new Parameter();
		parameter.put("p1", userid);
		parameter.put("p2", skey);
		List<UserSettings> list = userSettingsService.findBySql("select * from mlog_user_settings a where a.userid=:p1 and a.skey=:p2", parameter, UserSettings.class);
		if(list.size()>0){
			UserSettings us = list.get(0);
			JSONObject json1 = new JSONObject();
			json1.put("value", us.getValue());
			json1.put("description", us.getDescription());
			json.put("data", json1);
			json.put("success", true);
		}else {
			json.put("msg", "用户无此配置");
			json.put("success", false);
		}
		log.info("获取用户配置：："+json);
		super.writeJson(request,response,contentType, resultCode, json);
	}
	
	public static void main(String[] args){
		JSONObject json = new JSONObject();
		json.put("data", "aaa");		
		json.put("success", true);
		Map<String,String> parammap = new HashMap<String,String>();
		parammap.put("aa", null);
		String aa = null;
		System.out.println(String.valueOf(parammap.get("aa")));
		try {
			System.out.println(URLDecoder.decode("\\U9648\\U51a0\\U5e0c", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

}
