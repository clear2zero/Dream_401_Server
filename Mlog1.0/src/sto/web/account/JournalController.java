
package sto.web.account;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import other.AuthProfile;
import sto.common.MlogPM;
import sto.common.util.DateUtil;
import sto.common.util.Page;
import sto.common.util.Parameter;
import sto.common.util.StringReg;
import sto.common.web.BaseController;
import sto.model.account.Journal;
import sto.model.account.JournalReply;
import sto.service.account.JournalReplyService;
import sto.service.account.JournalService;
import sto.service.account.SysSettingsService;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户管理
 * 功能：列表、分配角色
 * 
 */
@Controller
@RequestMapping(value = "/journal")
@SuppressWarnings("unchecked")
public class JournalController extends BaseController{
	@Resource
	JournalService journalService;
	@Resource
	SysSettingsService sysSettingsService;
	@Resource
	JournalReplyService journalReplyService;
	
	@RequestMapping("/list.action")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("startDate", DateUtil.dateToString(DateUtil.getFirstDayOfMonth(),"yyyy-MM-dd"));
		model.addAttribute("endDate", DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		return "account/journalList";
	}
	@RequestMapping("/add.action")
	public String add(Model model, HttpServletRequest request) {
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("createtime", DateUtil.dateToString(new Date(),"yyyy-MM-dd"));
		model.addAttribute("user", curruser.getUser());
		return "account/journalSave";
	}
	/**
	 * 新增保存
	 */
	@RequestMapping(value="/saveDo.action",method=RequestMethod.POST)
	@ResponseBody
	public void saveDo(Journal r,HttpServletRequest request,HttpServletResponse response) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
	
		//String content = multipartRequest.getParameter("amcontent")+"#||#"+multipartRequest.getParameter("pmcontent");
		String amcontent = multipartRequest.getParameter("amcontent");
		//String image = multipartRequest.getParameter("image");
		String lgt = multipartRequest.getParameter("longitude");
		String lat = multipartRequest.getParameter("latitude");
		String addr = multipartRequest.getParameter("address");
		//String costtime = request.getParameter("costtime");

		Journal journal = new Journal();
		journal.setWriter(auth.getUser().getId());
		journal.setLgt(lgt);
		journal.setLat(lat);
		journal.setAddr(addr);
		journal.setContent(amcontent);
		//journal.setCosttime(costtime);
		journal.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		journalService.save(journal);
		response.setContentType("text/html;charset=utf-8"); 
		try {
			response.getWriter().write("{'success':'true','msg':'保存成功'}");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	@RequestMapping("/getJournalAllList.action")
	@ResponseBody
	public Map getJournalAllList(Model model, HttpServletRequest request) {
		Map<String, Object> m = new HashMap<String, Object>();
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("startDate", request.getParameter("startDate"));
		paramap.put("endDate", request.getParameter("endDate"));
		paramap.put("name", request.getParameter("name"));
		List<Map<String,Object>>  list = journalService.getJournalAllList(p,paramap);
		m.put("rows", list);
		m.put("total", p.getTotalCount());
		return m;
	}
	
	@RequestMapping("/journalReplyList.action")
	public String journalReplyList(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		AuthProfile curruser = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String journalid = request.getParameter("journalid");
		model.addAttribute("replyer", curruser.getUser().getId());
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("journalid", journalid);
		List<Map<String,Object>>  list = journalService.getJournalReplyByJournalId(paramap);
		model.addAttribute("replyList", list);
		model.addAttribute("journalid", request.getParameter("journalid"));
		String content = journalService.get(Integer.parseInt(journalid)).getContent().replace("\n", "<br/>");
		model.addAttribute("content", StringReg.replaceToHref(content));
		return "account/journalReplyList";
	}
	
	@RequestMapping("/replyJournal.action")
	@ResponseBody
	public Map replyJournal(JournalReply jr, HttpServletRequest request) {
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		int journalid = Integer.parseInt(request.getParameter("journalid"));
		String content = request.getParameter("content");
		
		JournalReply journalReply = new JournalReply();
		journalReply.setJournal(journalService.get(journalid));
		journalReply.setRecontent(content);
		journalReply.setRedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		journalReply.setReplyer(auth.getUser().getId());
		journalReplyService.save(journalReply);
		return suc();
	}
	
	@RequestMapping("/resetWarnStatus.action")
	@ResponseBody
	public Map resetWarnStatus(HttpServletRequest request) {
		Session session = SecurityUtils.getSubject().getSession();
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		int id = Integer.parseInt(request.getParameter("journalid"));
		Journal journal = journalService.get(id);
		if(auth.getUser().getId().intValue() == journal.getWriter().intValue()){
			journalService.updateBySql("update mlog_journal set iswarn=0 where id=:p1", new Parameter(id));
		}
		return suc();
	}
	
	@RequestMapping("/getWarnStatus.action")
	@ResponseBody
	public JSONObject getWarnStatus(){
		JSONObject json = new JSONObject();
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		json.put("iswarn", 0);
		if(auth.getUser().getId()!=null){
			List<Journal> list = journalService.findBySql("select * from mlog_journal a where a.writer=:p1 and a.iswarn=1", new Parameter(auth.getUser().getId()), Journal.class);
			if(list != null && list.size()>0){
				json.put("iswarn", 1);
			}
		}
		return json;
	}
	
	
	public static void main(String[] args){
		String protocol = "(?:(mailto|ssh|ftp|https?)://)?"; 
		String hostname = "(?:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+(?:com|net|edu|biz|gov|org|in(?:t|fo)|(?-i:[a-z][a-z]))"; 
		String ip = "(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])"; 
		String port= "(?::(\\d{1,5}))?"; 
		String path = "(/.*)?"; 
		String url = "("+protocol + "((?:" + hostname + "|" + ip + "))" + port + path+")";
		Pattern p = Pattern.compile(url);
		Matcher m = p.matcher("测试url http://www.baidu.comdadfsdf");
		while (m.find()) {  
			System.out.println(m.group(1));  
		}
	}
}
