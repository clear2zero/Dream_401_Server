/*
 * 文件名: WebMailAuth.java
 * 版本信息: 
 * 创建人: wu
 * 创建日期: 2007-2-9
 */
package other;
/**
 * <p>说明: </P>
 * <p>描述: </p>
 * <p>版权: Copyright(c)2006</p>
 * <p>公司(团体): Hebca</p>
 * @author wu 
 * @version 1.0
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sto.common.MlogPM;
import sto.common.exception.LoginException;
import sto.dao.account.OperateLogDao;
import sto.model.account.User;
import sto.service.account.UserService;
import sto.utils.SpringContextHolder;

import com.hebca.pki.Cert;
import com.hebca.pki.SignVerifier;
import com.hebca.pkix.verify.DefaultGeneralCertVerify;
import com.hebca.pkix.verify.DefaultGeneralVerify;
import com.hebca.pkix.verify.GeneralVerify;
import com.hebca.util.base64.Base64CoderFactory;
import com.koal.svs.client.SvsClientHelper;
import com.koal.svs.client.st.THostInfoSt;

/**
 * <p>说明: </P>
 * <p>描述: </p>
 * <p>版权: Copyright(c)2006</p>
 * <p>公司(团体): Hebca</p>
 * @author wu 
 * @version 1.0
 */
public class WebMailAuth {
    private static Log log = LogFactory.getLog(WebMailAuth.class); 
    private static THostInfoSt hostInfo = SpringContextHolder.getBean(THostInfoSt.class);
    
    public static AuthProfile authenticateInWebMailServerBySign(String signCert,String signature,String randomOridata)throws Exception{
    	String debug = "true";
    	if(!debug.equals("true")){
	        if(signCert==null || signCert.length()==0 || signature==null || signature.length()==0 || randomOridata==null || randomOridata==""){
    	//add by xj 2012-12-12  登陆经常报此错误,需要记录日志
    	log.debug("<登录>:signCert:"+signCert+"\nsignature:"+signature+"\nrandomOridata:"+randomOridata);
    	throw new LoginException("可能由于网络原因,请重试");//提交用户数据不完整
    }
    	}

        //check signature and cert state
    	
        Cert cert=new Cert(signCert);
        GeneralVerify signverify=new DefaultGeneralVerify(); 
        CertParse cp=new CertParse(cert);
       /* int alg_type=SignVerifier.ALG_SHA1RSA;
        if(cp.isECCCert()){
        	alg_type=SignVerifier.ALG_SM3SM2;
        }*/
       /* if (!debug.equals("true")) {
			signverify.verifySign(alg_type, cert, randomOridata.getBytes(),
					Base64CoderFactory.CreateBase64Coder().Decode(signature));
			DefaultGeneralCertVerify.getVerify().verify(cert);
		}*/
        boolean isVerify = false;
        if(isVerify){
			byte[] arrayOriginData = randomOridata.getBytes();	
			int nOriginDataLen = arrayOriginData.length;
			SvsClientHelper svsclient=SvsClientHelper.getInstance();
			svsclient.initialize(hostInfo.getSvrIP(),hostInfo.getPort(),1000,false,5000);
			int result = 99;
			try {
				result=svsclient.verifyCertSign(-1,-1,arrayOriginData,nOriginDataLen,signCert,signature,1,hostInfo);
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			//result = 0;
			if(result == 0  || result == -4050 || result==2 ){
				
			}
			else{
				String errCode=""; 
				switch(result)
				{
				case -1:
					errCode="签名验证服务器关闭.无法完成签名验证";
					break;
				case -6805:
					errCode="无效的证书文件";
					break;
				case -6406:
					errCode="签名错误";
					break;
				/*case -4050:
					addActionError("证书已经过期。");
					errCode="证书已经过期。";
					break;*/
				default:
					errCode="证书验证失败";
				}
				throw new LoginException(errCode+",请联系单位管理员");
			}
        }
		String certDn=CertIdentify.getCertIdentify(cp);        
        AuthProfile auth=new AuthProfile();
        //UserCert uc=UserCertController.getUserCertByCertId(certDn);
        User user=UserCertController.getUserByCertId(certDn);
        if(null != user){
        	if(user.getIsdelete() == null || user.getIsdelete() == 1
					||user.getIsenable() == null || user.getIsenable() == 0){
        		throw new LoginException("用户已冻结");//提交用户数据不完整
        	}
            //auth.setPassword(DES.decrypt(uc.getUserPass()));
            auth.setUsername(user.getUsername());
            auth.setCertIdentify(certDn);
            auth.setRole(user.getRole());
            auth.setUser(user);
            //auth.setUid(uc.getUid());
            auth.setStatus(user.getIsenable());
            auth.setEnabled(null == user.getIsenable() ? false : (user.getIsenable() == 0 ? false :true));
            return auth;
        }else {
        	throw new LoginException("该证书【"+certDn+"】未绑定用户，请联系单位管理员");//提交用户数据不完整
        }
    }
    
    /**
     * 用在移动接口，远程验证证书是否有效
     * @param signCert
     * @param signature
     * @param randomOridata
     * @return
     * @throws Exception
     */
    public static boolean validateCert(String signCert,String signature,String randomOridata)throws Exception{
        if(signCert==null || signCert.length()==0 || signature==null || signature.length()==0 || randomOridata==null || randomOridata==""){
        	throw new Exception("提交用户数据不完整,请重试");
        }
        Cert cert=new Cert(signCert);
        GeneralVerify signverify=new DefaultGeneralVerify(); 
        CertParse cp=new CertParse(cert);
        int alg_type=SignVerifier.ALG_SHA1RSA;
        if(cp.isECCCert()){
        	alg_type=SignVerifier.ALG_SM3SM2;
        }
        signverify.verifySign(alg_type,
        		cert,
                randomOridata.getBytes(),
                Base64CoderFactory.CreateBase64Coder().Decode(signature));
        
        DefaultGeneralCertVerify.getVerify().verify(cert);
        return true;
       
    }
    
    
}

