package sto.common.aop;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import other.AuthProfile;
import sto.common.MlogPM;
import sto.common.service.BaseServiceImpl;
import sto.common.util.Parameter;
import sto.common.util.StringUtils;
import sto.model.account.Journal;
import sto.model.account.Msg;
import sto.model.account.User;
import sto.service.account.DataRulesService;
import sto.service.account.JournalService;
import sto.service.account.MsgService;
import sto.service.account.UserService;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;

public class JPushInterceptor  extends BaseServiceImpl implements HandlerInterceptor {
	private static Logger log = Logger.getLogger(sto.common.aop.JPushInterceptor.class);
	//private static OperateLogDao operateLogDao = SpringContextHolder.getBean(OperateLogDao.class);
	@Resource
	DataRulesService dataRulesService;
	@Resource
	JournalService journalService;
	@Resource
	MsgService msgService;
	@Resource
	UserService userService;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	@Transactional(readOnly = false)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		// 1. 设置developer平台的ApiKey/SecretKey
        String masterSecret = MlogPM.get("cn.jpush.api.mastersecret");
        String appKey = MlogPM.get("cn.jpush.api.appkey");
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

        List<User> list = null;
        int model = 1;//1消息2日志
        String requestRri = request.getRequestURI();
        if (StringUtils.endsWith(requestRri, "/journal/saveDo.action")){
        	model = 2;
        	AuthProfile auth = (AuthProfile) SecurityUtils.getSubject().getPrincipal();
        	list = dataRulesService.getUsersViewed(auth.getUser().getId(), 2);
        }
        if (StringUtils.endsWith(requestRri, "/journal/replyJournal.action")){
        	model = 2;
			int journalid = Integer.parseInt(request.getParameter("journalid"));
			journalService.updateBySql("update mlog_journal set iswarn=1 where id=:p1", new Parameter(journalid));
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute("iswarn", 1);
			Journal j = journalService.get(journalid);
			list = new ArrayList<User>();
			list.add(userService.get(j.getWriter()));
        }
        if (StringUtils.endsWith(requestRri, "/interface/writeJournal.action")){
        	model = 2;
    		int userid = Integer.parseInt(String.valueOf(request.getAttribute("userid")));
    		list = dataRulesService.getUsersViewed(userid, 2);
        }
		if (StringUtils.endsWith(requestRri, "/interface/replyJournal.action")){
			model = 2;
			int journalid = Integer.parseInt(request.getParameter("replyid"));
			journalService.updateBySql("update mlog_journal set iswarn=1 where id=:p1", new Parameter(journalid));
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute("iswarn", 1);
			Journal j = journalService.get(journalid);
			list = new ArrayList<User>();
			list.add(userService.get(j.getWriter()));
		}
		if (StringUtils.endsWith(requestRri, "/interface/sendMsg.action")){
			model = 1;
    		String receivers = String.valueOf(request.getAttribute("receivers"));
    		receivers = receivers.lastIndexOf(",") == (receivers.length()-1) ? receivers.substring(0, receivers.length()-1) : receivers;
    		list = userService.findBySql("select * from platform_t_user a where a.id in ("+receivers+")", null, User.class);
		}
		if (StringUtils.endsWith(requestRri, "/interface/replyMsg.action")){
			model = 1;
			int msgid = Integer.parseInt(request.getParameter("replyid"));
			Msg m = msgService.get(msgid);
			list = new ArrayList<User>();
			list.add(userService.get(m.getPublisher()));
		}
		
            // 4. 创建请求类对象
            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
    	if(list != null){
    		for(User user : list){
    			JSONObject json = new JSONObject();
    			json.put("certcn", user.getScertcn());
    			json.put("model", model);
    			PushPayload payload = PushPayload.newBuilder()
     	                .setPlatform(Platform.all())
     	                .setAudience(Audience.alias(user.getPushuserid()))
     	                .setMessage(Message.content(JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect)))
     	                .build();

     	        try {
     	            PushResult result = jpushClient.sendPush(payload);
     	            log.info("Got result - " + result);
     	        } catch (APIConnectionException e) {
     	            // Connection error, should retry later
     	        	log.error("Connection error, should retry later", e);
     	        } catch (APIRequestException e) {
     	            // Should review the error, and fix the request
     	        	log.error("Should review the error, and fix the request", e);
     	        	log.info("HTTP Status: " + e.getStatus());
     	        	log.info("Error Code: " + e.getErrorCode());
     	        	log.info("Error Message: " + e.getErrorMessage());
     	        }catch (Exception e) {
     	        	log.info(e.getMessage());
     	        	e.printStackTrace();
     	        }
    		}
    	}
	}
}
