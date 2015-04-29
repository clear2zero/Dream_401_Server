package sto.web.account;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import other.AuthProfile;
import other.ConnectionProfile;
import other.UserCert;
import sto.common.util.Parameter;
import sto.common.web.BaseAction;
import sto.model.account.Journal;
import sto.service.account.CaUsernamePasswordToken;
import sto.service.account.JournalService;
import sto.utils.CacheUtils;

import com.hebca.pki.Cert;
import com.hebca.pki.CertCodingException;
import com.hebca.pki.CertParse;
import com.hebca.pki.CertParsingException;

/**
 * @Description:
 * @author chenxiaojia
 * @date 2014-7-28 17:04:47
 * 
 */
@Controller
@RequestMapping(value = "/account")
public class LoginEndAction extends BaseAction {
	@Resource
	JournalService journalService;
	public static final String RELOAD = "redirect:/loginend/";

	@RequestMapping(value = "/loginEnd.action")
	public String list() {
		Session session = SecurityUtils.getSubject().getSession();
		Object obj = session.getAttribute("userCert");
		UserCert userCert = obj == null ? null : (UserCert) obj;
		AuthProfile auth = (AuthProfile) SecurityUtils.getSubject()
				.getPrincipal();
		System.out.println(session.getAttribute("userToken"));
		System.out.println(request.getSession().getAttribute("userToken"));
		session.setAttribute("certcn",auth.getCertIdentify());
		session.setAttribute("name",auth.getUser().getName());
		try {
			/*login((CaUsernamePasswordToken) session.getAttribute("userToken"),
					request,
					(ConnectionProfile) ConnectionProfileList
							.getDefaultProfile(), auth);*/
			System.out.println(session.getAttribute("auth"));
			System.out.println(request.getSession().getAttribute("auth"));
		} catch (Exception e) {
			SecurityUtils.getSubject().logout();
			System.out.println("LoginEndAction:Login out");
			e.printStackTrace();
		}
		CacheUtils.getCacheManager().clearAll();//登陆前清除缓存
		int userid = auth.getUser().getId();
		session.setAttribute("iswarn", 0);
		List<Journal> list = journalService.findBySql("select * from mlog_journal a where a.writer=:p1 and a.iswarn=1", new Parameter(userid), Journal.class);
		if(list != null && list.size()>0){
			session.setAttribute("iswarn", 1);
		}	
		
		return "forward:/layout/main.jsp";
	}

	public void login(CaUsernamePasswordToken bean, HttpServletRequest request,
			ConnectionProfile profile, AuthProfile auth) throws Exception {
	}

	/**
	 * @Description:
	 * @param @param auth
	 * @param @param mailskin
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getSkin(AuthProfile auth) throws Exception {
		String mailskin = "default";		
		return mailskin;
	}

	protected boolean isUpdate(UserCert uc, CaUsernamePasswordToken bean)
			throws CertParsingException, CertCodingException

	{
		if (null != uc) {
			CertParse nowCertParse = new CertParse(new Cert(bean.getSignCert()));
			boolean op1 = false;
			boolean op2 = false;
			if (uc.getSignCert() != null && uc.getSignCert().length != 0) {
				CertParse databaseCertParse = new CertParse(new Cert(
						uc.getSignCert()));
				// 与数据库中存在的不一致
				Date nowNotAfter = nowCertParse.getNotAfter();
				Date databaseNotAfter = databaseCertParse.getNotAfter();
				boolean timeb = nowNotAfter.compareTo(databaseNotAfter) > 0;

				String serialNumberDecString = nowCertParse
						.getSerialNumberDecString();
				String oserialNumberDecString2 = databaseCertParse
						.getSerialNumberDecString();
				boolean snb = !serialNumberDecString
						.equals(oserialNumberDecString2);
				op2 = snb || timeb;
			}

			// 数据库没有加密及签名证书
			op1 = uc.getSignCert() == null || uc.getSignCert().length == 0
					|| uc.getCryptCert() == null
					|| uc.getCryptCert().length == 0;

			if (op1 || op2) {
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = "/logout.action")
	public String logout() {
		SecurityUtils.getSubject().logout();
		CacheUtils.getCacheManager().clearAll();//退出登陆时清除缓存
		return "redirect:/account/login.action";
	}
}
