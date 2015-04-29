/*
 * 文件名: VerifyInitiator.java
 * 版本信息: 
 * 创建人: echo
 * 创建日期: Feb 11, 2007
 */
package other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.hebca.pkix.verify.DefaultGeneralCertVerify;

/**
 * <p>说明: </P>
 * <p>描述: </p>
 * <p>版权: Copyright(c)2006</p>
 * <p>公司(团体): Hebca</p>
 * @author echo 
 * @version 1.0
 */
public class VerifyInitiator extends HttpServlet {
    private static Log log = LogFactory.getLog(Initiator.class);
    private static XmlWebApplicationContext o;
    
    public void init() throws ServletException {
         //doInit();
        //Security.addProvider(new BouncyCastleProvider());
		/*try {
			LicenceController.getLicenceContent();
		} catch (Exception e) {
			throw new ServletException(e);
		}*/
		
    }
    
    public static void doInit(){
    	 InputStream is=null;
        try {
        	
            Properties prop = new Properties();
            String path = VerifyInitiator.class.getResource("/").getPath();
            path = path.replace("classes", "config");
    		// 初始化配置文件
            is=new FileInputStream(new File(path
    				+ "verify.properties"));
            prop.load(is);
            
            DefaultGeneralCertVerify.initFromProp(prop,Paths.getPrefix());
            
        } catch (Exception e) {
            log.fatal("Could not init cert verifior", e);
        } finally{
        	if(is!=null){
        		try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }

}
