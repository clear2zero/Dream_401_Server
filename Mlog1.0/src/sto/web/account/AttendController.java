
package sto.web.account;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import other.AuthProfile;
import sto.common.HttpUtil;
import sto.common.MlogPM;
import sto.common.util.DateUtil;
import sto.common.util.Page;
import sto.common.util.RoleType;
import sto.common.util.StringUtils;
import sto.common.web.BaseController;
import sto.model.account.Attend;
import sto.service.account.AttendService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户管理
 * 功能：列表、分配角色
 * 
 */
@Controller
@RequestMapping(value = "/attend")
@SuppressWarnings("unchecked")
public class AttendController extends BaseController{
	@Resource
	AttendService attendService;
	
	@RequestMapping("/list.action")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("startDate", DateUtil.dateToString(DateUtil.getFirstDayOfMonth(),"yyyy-MM-dd"));
		model.addAttribute("endDate", DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		return "account/attendList";
	}
	/**
	 * 列表返回json
	 */
	@RequestMapping("/listJson.action")
	@ResponseBody
	public Map listJson(Model model, HttpServletRequest request) {
		Map<String, Object> m = new HashMap<String, Object>();
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("startDate", request.getParameter("startDate"));
		paramap.put("endDate", request.getParameter("endDate"));
		paramap.put("name", request.getParameter("name"));
		List<Map<String,Object>>  list = attendService.getAttendAllList(p,paramap);
		m.put("rows", list);
		m.put("total", p.getTotalCount());
		return m;
	}
	@RequestMapping("/attendcardmxList.action")
	public String attendcardmxList(Model model, HttpServletRequest request) {
		model.addAttribute("startDate", DateUtil.dateToString(DateUtil.getFirstDayOfMonth(),"yyyy-MM-dd"));
		model.addAttribute("endDate", DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		return "account/attendcardmxList";
	}
	@RequestMapping("/getAttendCardListByUserid.action")
	@ResponseBody
	public Map getAttendCardListByUserid(Model model, HttpServletRequest request) {
		Map<String, Object> m = new HashMap<String, Object>();
		
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")));
		Map<String, Object> paramap = new HashMap<String, Object>();
		request.getParameterNames();
		paramap.put("date", request.getParameter("date"));
		paramap.put("userid", request.getParameter("userid"));
		List<Map<String,Object>>  list = attendService.getAttendCardListByUserid(p,paramap);
		m.put("rows", list);
		m.put("total", p.getTotalCount());
		return m;
	}
	@RequestMapping("/attend.action")
	public String attend(Model model, HttpServletRequest request) {
		return "account/attend";
	}
	@RequestMapping("/attendDo.action")
	@ResponseBody
	public JSONObject attendDo(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		int userid = auth.getUser().getId();
		String lgt = "0.0";
		String lat = "0.0";
		String address = "#";
		int type = Integer.parseInt(request.getParameter("type"));
		//根据ip获取定位信息
		Map<String,String> map = new HashMap<String,String>();
		map.put("ip", StringUtils.getRemoteAddr(request));
		map.put("ak", MlogPM.get("baidu.service.apikey"));
		//map.put("sn", "");
		map.put("coor", "bd09ll");
		JSONObject bdjson = HttpUtil.invoke(MlogPM.get("baidu.map.location.ip"), map);
		if(!bdjson.isEmpty()){
			if(bdjson.getString("success") == null){
				if(bdjson.getIntValue("status") == 0){
					JSONObject contentjson = bdjson.getJSONObject("content");
					address = contentjson.getString("address");
					JSONObject pointjson = contentjson.getJSONObject("point");
					lgt = pointjson.getString("x");
					lat = pointjson.getString("y");
				}
			}else {
				System.out.println("baidu定位连接异常"+bdjson.getString("msg"));
			}
			
		}
		
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
		return json;
	}
	
	@RequestMapping("/checkAttendStatus.action")
	@ResponseBody
	public JSONObject checkAttendStatus(Model model, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		List<Attend> adList = attendService.getAttendByUserid(auth.getUser().getId());
		if(adList != null && adList.size() > 0){
			Attend ad = adList.get(0);
			if(ad != null && ad.getStatus() != null){
				json.put("status", ad.getStatus());
				if(ad.getStatus() == 0){
					json.put("preontime", adList.size() > 1 ? adList.get(1).getOntime() == null ? "" : adList.get(1).getOntime().subSequence(0, 19) : "");
					json.put("preofftime", adList.size() > 1 ? adList.get(1).getOfftime() == null ? "" : adList.get(1).getOfftime().subSequence(0, 19) : "");
					json.put("ontime", ad.getOntime().subSequence(0, 19));
					json.put("offtime","");
				}else {
					json.put("preontime", ad.getOntime() == null ? "" : ad.getOntime().subSequence(0, 19));
					json.put("preofftime", ad.getOfftime() == null ? "" : ad.getOfftime().subSequence(0, 19));
					json.put("ontime", "");
					json.put("offtime", "");
				}
			}else {
				json.put("status", -1);
				json.put("preontime","");
				json.put("preofftime","");
				json.put("ontime","");
				json.put("offtime","");
			}
		}else {
			json.put("status", -1);
			json.put("preontime","");
			json.put("preofftime","");
			json.put("ontime","");
			json.put("offtime","");
		}
		
		return json;
	}
	@RequestMapping("/statisticList.action")
	public String statisticList(Model model, HttpServletRequest request) {
		model.addAttribute("startDate", DateUtil.dateToString(DateUtil.getFirstDayOfMonth(),"yyyy-MM-dd"));
		model.addAttribute("endDate", DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		return "account/attendstatisticList";
	}
	/**
	 * 列表返回json
	 */
	@RequestMapping("/statisticListJson.action")
	@ResponseBody
	public Map statisticListJson(Model model, HttpServletRequest request) {
		Map<String, Object> m = new HashMap<String, Object>();
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("startDate", request.getParameter("startDate"));
		paramap.put("endDate", request.getParameter("endDate"));
		paramap.put("name", request.getParameter("name"));
		List<Map<String,Object>>  list = attendService.getAttendAllList(p,paramap);
		m.put("rows", list);
		m.put("total", p.getTotalCount());
		return m;
	}
	
	@RequestMapping("/exportToExcel.action")
	public void exportToExcel(HttpServletRequest request,HttpServletResponse response){
		//创建一个新的excel 
		HSSFWorkbook wb = new HSSFWorkbook(); 
		//创建sheet页 
		HSSFSheet sheet = wb.createSheet();
		//设置默认样式
		HSSFCellStyle defaultstyle=wb.createCellStyle();
		defaultstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置底线和颜色
		defaultstyle.setBottomBorderColor(HSSFColor.BLACK.index);
		defaultstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   //设置左边线和颜色
		defaultstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		defaultstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   //设置右边线和颜色
		defaultstyle.setRightBorderColor(HSSFColor.BLACK.index);
		defaultstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   //设置上面线和颜色
		defaultstyle.setTopBorderColor(HSSFColor.BLACK.index);
		//设置默认样式
		HSSFCellStyle numbericstyle=wb.createCellStyle();
		numbericstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置底线和颜色
		numbericstyle.setBottomBorderColor(HSSFColor.BLACK.index);
		numbericstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   //设置左边线和颜色
		numbericstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		numbericstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   //设置右边线和颜色
		numbericstyle.setRightBorderColor(HSSFColor.BLACK.index);
		numbericstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   //设置上面线和颜色
		numbericstyle.setTopBorderColor(HSSFColor.BLACK.index);
		numbericstyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		//设置标题样式
		HSSFCellStyle titlestyle=wb.createCellStyle();
		titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置底线和颜色
		titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   //设置左边线和颜色
		titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   //设置右边线和颜色
		titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   //设置上面线和颜色
		titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
		titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//自定义RGB颜色
		/*HSSFPalette palette2 = wb.getCustomPalette();  
		palette2.setColorAtIndex(HSSFColor.LIME.index, (byte) 140, (byte) 216, (byte) 226);*/
		//设置 前景色           
		//titlestyle.setFillForegroundColor(HSSFColor.BLUE.index);//添加前景色,内容看的清楚
		//titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short)10);   //字体大小
		font1.setFontName("宋体");   //什么字体
		font1.setItalic(false);//是不倾斜
		font1.setStrikeout(false); //是不是划掉
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   //字体加粗
		titlestyle.setFont(font1);
		
		//设置考勤异常日期样式
		HSSFCellStyle style2=wb.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置底线和颜色
		style2.setBottomBorderColor(HSSFColor.BLACK.index);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);   //设置左边线和颜色
		style2.setLeftBorderColor(HSSFColor.BLACK.index);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);   //设置右边线和颜色
		style2.setRightBorderColor(HSSFColor.BLACK.index);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);   //设置上面线和颜色
		style2.setTopBorderColor(HSSFColor.BLACK.index);
		//自定义RGB颜色
		/*HSSFPalette palette2 = wb.getCustomPalette();  
		palette2.setColorAtIndex(HSSFColor.LIME.index, (byte) 140, (byte) 216, (byte) 226);*/
		//设置 前景色           
		style2.setFillForegroundColor(HSSFColor.YELLOW.index);//添加前景色,内容看的清楚
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		//创建header页 
		HSSFHeader header = sheet.getHeader(); 
		//设置标题居中 
		header.setCenter("标题");
		
		String divid = request.getParameter("divid");
		if(divid == null){
			AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
			if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
				divid = auth.getUser().getUnit().getDivid();
			}
		}
		String deptid = request.getParameter("deptid");
		String date1 = request.getParameter("startDate");
		String date2 = request.getParameter("endDate");
		String columns = request.getParameter("columns");
		Map<String,String> m = new HashMap<String,String>();
		m.put("divid", divid);
		m.put("deptid", deptid);
		m.put("date1", date1);
		m.put("date2", date2);
		List<Map<String, Object>> list = null;
		try {
			list = attendService.getAttendStatisticList(m);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		HSSFRow row = null;
		HSSFCell cell = null;
		Map<String,Object> map = null;
		if(list != null && list.size()>0){
			row = sheet.createRow(0);//创建标题行
			row.setHeight((short)400);
			
			JSONArray columnsarr = JSON.parseArray(columns);
			for(int j=0;j<columnsarr.size();j++){
				String title =  ((JSONObject)columnsarr.get(j)).getString("title");
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(title));
				cell.setCellStyle(titlestyle);
				sheet.setColumnWidth(j, 3500);
			}
			//添加请假列标题
			cell = row.createCell(columnsarr.size());
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString("请假天数"));
			cell.setCellStyle(titlestyle);
			sheet.setColumnWidth(columnsarr.size(), 2500);
			
			for(int i=0;i<list.size();i++){
				map = list.get(i);
				row = sheet.createRow(i+1);//创建内容行
				cell = row.createCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(map.get("name"))));
				cell.setCellStyle(defaultstyle);
				sheet.setColumnWidth(0, 3000);
				
				int normaldays = 0;
				int baddays = 0;
				for(int j=0;j<columnsarr.size()-3;j++){
					cell = row.createCell(j+1);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(((String[])map.get("worktimes"))[j]));
					sheet.setColumnWidth(j+1, 3500);
					cell.setCellStyle(defaultstyle);
					if((((String[])map.get("isbads"))[j]).equals("true")){
						cell.setCellStyle(style2);
						baddays++;
					}else {
						normaldays++;
						
					}
				}
				//添加正常天数列内容
				cell = row.createCell(columnsarr.size()-2);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(new HSSFRichTextString(normaldays+""));
				cell.setCellStyle(numbericstyle);
				sheet.setColumnWidth(columnsarr.size()-2, 2500);
				//添加异常天数列内容
				cell = row.createCell(columnsarr.size()-1);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(new HSSFRichTextString(baddays+""));
				cell.setCellStyle(numbericstyle);
				sheet.setColumnWidth(columnsarr.size()-1, 2500);
				
				//添加请假列内容
				cell = row.createCell(columnsarr.size());
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(new HSSFRichTextString(""));
				cell.setCellStyle(titlestyle);
				sheet.setColumnWidth(columnsarr.size(), 2500);
			}
		}
		//设置默认列宽
		sheet.setDefaultRowHeight((short)400);
		//设置footer 
		sheet.setGridsPrinted(true); 
		HSSFFooter footer = sheet.getFooter(); 
		footer.setRight("page   " + HeaderFooter.page() + "of" + HeaderFooter.numPages());
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/ms-excel");
		//response.setContentLength((int)file.length());
		response.setHeader("Result-Code", "0");
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("考勤统计.xls","UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			/*OutputStream os = new FileOutputStream(new File("d:\\test.xls"));
			wb.write(os);
			os.flush();
			os.close();*/
			response.setBufferSize(1024);
			/*OutputStream os = response.getOutputStream();
			os.write(wb.getBytes());
			os.flush();
			os.close();*/
			
			wb.write(response.getOutputStream());
			response.flushBuffer();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//
		}
	}
	@RequestMapping("/statisticQuery.action")
	@ResponseBody
	public JSONObject statisticQuery(HttpServletRequest request){
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
		String divid = request.getParameter("divid");
		JSONObject json = new JSONObject();
		if(divid == null){
			if(RoleType.UNIT_USERGROUP.getName().contains(auth.getRole().getEnname())){
				divid = auth.getUser().getUnit().getDivid();
				if(divid == null){
					json.put("rows", "");
					json.put("total", 0);
					return json;
				}
			}else {
				json.put("rows", "");
				json.put("total", 0);
				return json;
			}
		}
		String deptid = request.getParameter("deptid");
		String date1 = request.getParameter("startDate");
		String date2 = request.getParameter("endDate");
		String columns = request.getParameter("columns");
		Page<Object[]> p = new Page<Object[]>(Integer.parseInt(request.getParameter("rows")==null?"10":request.getParameter("rows")));
		p.setPageNo(Integer.parseInt(request.getParameter("page")==null?"1":request.getParameter("page")));
		Map<String,String> m = new HashMap<String,String>();
		m.put("divid", divid);
		m.put("deptid", deptid);
		m.put("date1", date1);
		m.put("date2", date2);
		m.put("columns", columns);
		m.put("pagesize", request.getParameter("rows")==null?"10":request.getParameter("rows"));
		m.put("pageno", request.getParameter("page")==null?"1":request.getParameter("page"));
		return attendService.getAttendStatisticListQuery(m);
	}
	@RequestMapping("/statisticQueryColumns.action")
	@ResponseBody
	public JSONObject statisticQueryColumns(HttpServletRequest request){
		String date1 = request.getParameter("startDate");
		String date2 = request.getParameter("endDate");
		Map<String,String> m = new HashMap<String,String>();
		m.put("date1", date1);
		m.put("date2", date2);
		JSONObject json = new JSONObject();
		try {
			json = attendService.getAttendStatisticQueryColumns(m);
			if(!json.isEmpty()){
				json.put("success", true);
			}else {
				json.put("success", false);
				json.put("msg", "查询失败");
			}
		} catch (Exception e1) {
			json.put("success", false);
			json.put("msg", e1.getMessage());
			e1.printStackTrace();
		}
		return json;
	}
}
