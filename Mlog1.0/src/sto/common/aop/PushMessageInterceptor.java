package sto.common.aop;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

/**
 * 系统拦截器
 * @author ThinkGem
 * @version 2013-6-6
 */
public class PushMessageInterceptor extends BaseServiceImpl implements HandlerInterceptor {

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
        String apiKey = MlogPM.get("baidu.yun.channel.apikey");
        String secretKey = MlogPM.get("baidu.yun.channel.secretkey");
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });
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
    			PushUnicastMessageRequest pushrequest = new PushUnicastMessageRequest();
	            pushrequest.setDeviceType(3); // device_type => 1: web 2: pc 3:android 4:ios 5:wp
	            pushrequest.setChannelId(Long.parseLong(StringUtils.trimToEmpty(user.getPushchannelid()).equals("") ? "0" : user.getPushchannelid()));
	            pushrequest.setUserId(user.getPushuserid());
	            pushrequest.setMessage(JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect));
	            // 5. 调用pushMessage接口
	            PushUnicastMessageResponse pushresponse = null;
	            try{
	            	pushresponse = channelClient.pushUnicastMessage(pushrequest);
	            	 // 6. 认证推送成功
    	            System.out.println("push amount : " + pushresponse.getSuccessAmount());
	            }catch (ChannelClientException e) {
	                // 处理客户端错误异常
	                e.printStackTrace();
	            } catch (ChannelServerException e) {
	                // 处理服务端错误异常
	                System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s",
	                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	            }
    		}
    	}
	}

}
