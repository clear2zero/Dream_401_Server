/*
 * 文件名: UserCertController.java
 * 版本信息: 
 * 创建人: echo
 * 创建日期: Mar 8, 2007
 */
package other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sto.model.account.User;
import sto.service.account.UserService;
import sto.utils.SpringContextHolder;

import com.jenkov.mrpersister.itf.IGenericDao;
import com.jenkov.mrpersister.util.JdbcUtil;

/**
 * <p>
 * 说明:
 * </P>
 * <p>
 * 描述:
 * </p>
 * <p>
 * 版权: Copyright(c)2006
 * </p>
 * <p>
 * 公司(团体): Hebca
 * </p>
 * 
 * @author echo
 * @version 1.0
 */
public class UserCertController {
	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	private static Log log = LogFactory.getLog(UserCertController.class); 
	/**
	 * 个人用户使用
	 * @param certId
	 * @return
	 * @throws Exception
	 */
	public static UserCert getUserCertByCertId(final String certId) throws Exception {
		return (UserCert)Finder.find(Finder.Key.getUserCertByCertId,certId, new NoResultQuery(){
			@Override
			public Object query() throws Exception{
					return getUserCertByCertId_agoMethod(certId);
			}
			
		});
		
	}
	
	public static UserCert getUserCertByCertId_agoMethod(String certId) throws Exception {
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();

			String sql = "select * from user_cert where cert_id=?";
			Object o = dao.read(UserCert.class, sql, new Object[] { certId });
			if (o == null)
				return null;
			return (UserCert) o;
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}

	}
	
	public static User getUserByCertId(final String certId) throws Exception {
		return (User)Finder.find(Finder.Key.getUserCertByCertId,certId, new NoResultQuery(){
			@Override
			public Object query() throws Exception{
					return getUserByCertId_agoMethod(certId);
			}
			
		});
		
	}
	
	public static User getUserByCertId_agoMethod(String certId) throws Exception {
		User user = userService.findUniqueBy("hcertcn", certId);
		return user;
	}

	/*public static UserCert getUserCertById(Long id)throws Exception{
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();
			String sql = "select * from user_cert where id=?";
			return (UserCert)dao.read(UserCert.class, sql, new Object[]{id});
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
	}*/
}
