/*
 * 文件名: CertIdentify.java
 * 版本信息: 
 * 创建人: echo
 * 创建日期: Mar 8, 2007
 */
package other;



/**
 * <p>说明: </P>
 * <p>描述: </p>
 * <p>版权: Copyright(c)2006</p>
 * <p>公司(团体): Hebca</p>
 * @author echo 
 * @version 1.0
 */
public class CertIdentify {
    public static String getCertIdentify(CertParse parser)
    {
        //change by shenqn 2012.05.17  增加对新系统的支持
        String  cn1=parser.getSubject(CertParse.DN_CN);
        String cn2=parser.getSubject(CertParse.DN_GIVENNAME);
        return cn1.length()>cn2.length()?cn1:cn2;
    }
}
