package other;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionControllers {
	private static Log log=LogFactory.getLog(SessionControllers.class);
	
	//---------------------------需要一直保持的session------------------------------
	//与邮件服务器的连接信息，登陆后设置，一直保持
	public final static String Handler="handler";
	//用户认证信息，登陆后设置，一直保持
	public final static String Auth="auth";
	//邮件服务器的配置信息，登陆后设置，一直保持
	public final static String Profile="profile";
	//邮件文件夹信息，登陆后设置，一直保持
	public final static String MailFolders="mailFolders";
	//便签文件夹信息，登陆后设置，一直保持
	public final static String NotesFolders="notesFolders";
	//模块信息，登陆后设置，一直保持
	public final static String Module="module";
	//发送邮件模块
	public final static String UploadContext="uploadContext";
	//手动检查邮件，邮箱满
	public final static String MailBoxCapacityFull="mailBoxCapacityFull";
	//手动检查邮件，邮件超出容量的邮件大小
	public final static String NewMailSize="newMailSize";
	//用户的空余控件
	public final static String FreeSpace="user_freeSpace";
	public final static String Templates="templates";
	public final static String TemplatesOffice="TemplatesOffice";
	//add by xj 2012-11-29系统是否为调试状态 , 如果打开可屏蔽KEY检查等 ,调试状态为true,非调试状态为false或空
	public final static String sysDebug = "sysDebug";
	//邮箱使用百分比
	public final static String UsedMailMapacity="usedMailMapacity";
	//手机新旧版本兼容登陆时添加手机端版本号
	public final static String Phone_APKversion="phoneAPKversion";
	//-------------------------临时的session---------------------------------------
	
	
	//登陆随机数：在登陆之前设置，登陆验证成功后删除
	public final static String LoginRandom="loginRandom";
	
	//申请手机验证码
	public final static String MobileValidateCode="MobileValidateCode";
	public final static String MobileValidateErrorCount="MobileValidateErrorCount";

	//绑定账号验证码
	public final static String BindAccount="BindAccount";
	
	//证书找回，缓存userName、phoneID
	public final static String GetBackCert="GetBackCert";
	
	
	public static Object get(HttpServletRequest request,String name){
		return request.getSession().getAttribute(name);
	}
	
	public static void set(HttpServletRequest request,String name,Object o){
		log.debug("<session> add "+name);
		request.getSession().setAttribute(name, o);
	}
	
	public static void remove(HttpServletRequest request,String name){
		log.debug("<session> remove "+name);
		request.getSession().removeAttribute(name);
	}
	
	// 保存用户皮肤设置 2013.05.20 gaobiao
	public final static String MAILSKIN = "mailskin";
	//add by shenqn
	public final static String USER_FONTSIZE="user_fontSize";
}
