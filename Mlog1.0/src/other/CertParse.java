package other;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.hebca.pki.AbstractParseBase;
import com.hebca.pki.Cert;
import com.hebca.pki.CertBase;
import com.hebca.pki.CertChainVerifyException;
import com.hebca.pki.CertDateVerifyException;
import com.hebca.pki.CertParsingException;
import com.hebca.pki.provider.JceProvider;
/**
 * <p>Title: Cert Parsing</p>
 *
 * <p>Description: ����֤��,ȡ֤������</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: hebca</p>
 *
 * @author �����
 * @version 1.0
 */
public class CertParse extends AbstractParseBase{
    /**
     *
     * @param c Cert : ֤������
     * @throws CertParsingException : ֤���ʽ�������ʱ����
     */
    public CertParse() throws CertParsingException {
        
    }
    
    public CertParse(Cert c) throws CertParsingException {
        set(c);
    }
    
    public CertParse(Cert c, JceProvider provider) throws CertParsingException {
        set(c,provider);
    }
        

  public void set(CertBase c)throws  CertParsingException{
      try
     {
       ByteArrayInputStream bis = new ByteArrayInputStream(c.getDER());
       Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());//Horse_Little
       CertificateFactory cf = CertificateFactory.getInstance("X.509",BouncyCastleProvider.PROVIDER_NAME);
       cert = (X509Certificate) cf.generateCertificate(bis);
       bis.close();
     }
     catch(Exception e)
     {
       throw new CertParsingException("证书编码错误");
     }

  }
  
  public void set(CertBase c , JceProvider provider) throws CertParsingException{
      try
      {
        ByteArrayInputStream bis = new ByteArrayInputStream(c.getDER());
        CertificateFactory cf = CertificateFactory.getInstance("X.509",provider.getName());
        cert = (X509Certificate) cf.generateCertificate(bis);
        bis.close();
      }
      catch(Exception e)
      {
        throw new CertParsingException("证书编码错误");
      }     
  }

  /**
   *
   * @return int : ȡ֤��汾
   */
  public int getVersion(){
    return cert.getVersion();
  }

  /**
   *
   * @return BigInteger : ȡ֤�����к�
   */
  public  BigInteger getSerialNumber()
  {
    return cert.getSerialNumber();
  }

  /**
   *
   * @return String : ȡ֤�����кŵ�16������ʽ�ַ�
   */
  public String getSerialNumberHexString()
  {
    return cert.getSerialNumber().toString(16);
  }

  /**
   *
   * @return String : ȡ֤�����кŵ�10������ʽ�ַ�
   */
  public String getSerialNumberDecString()
  {
    return cert.getSerialNumber().toString();
  }

  /**
   *
   * @return String : ȡ֤���ǩ���㷨��ƣ�hebca�䷢��֤���ǩ���㷨Ϊsha1RSA��
   */
  public String getSigAlgName()
  {
    return cert.getSigAlgName();
  }

  /**
   *
   * @return String : ȡǩ���㷨OID
   */

  public String getSigAlgOID()
  {
    return cert.getSigAlgOID();
  }

  /**
   *
   * @return String : ȡ֤�������ȫ����������Ϣ
   */
  public String getSubject()
  {
    return DNnameMapping(cert.getSubjectDN().toString());
  }

  /**
   *
   * @param dn String : �����DN(����������)
   * @return String : �����ֵ
   */
  public String getSubject(String dn)
  {
    return findSeg(getSubject(),dn);
  }

  /**
   *
   * @param index int �� ���������
   *    <p>����ŷֲ���</p>
   *     0           cn (CN)<br>
   *     1           country(C)<br>
   *     2           orgnization(O)<br>
   *     3           orgnization unit(OU)<br>
   *     4           state(S)<br>
   *     5           location(L)<br>
   *     6           email(E)<br>
   *     7           givename(G)<br>
   *     8           alias(2.5.4.1)<br>
   *     9           title(T)<br>
   *     10          surname<br>
   *     15          phone<br>
   * @return String : �����ֵ
   */
  public String getSubject(int index)
  {
    String dn=this.dnIndextoString(index);
    if(dn==null)
    {
      return "";
    }
    return getSubject(dn);
  }

  /**
   *
   * @return String �� ȡǩ������Ϣ
   */
  public String getIssuer()
  {
    return this.DNnameMapping(cert.getIssuerDN().toString());
  }

  /**
   *
   * @param dn String ����Ϣ������
   * @return String �� ǩ��������ֵ
   */
  public String getIssuer(String dn)
  {
    return findSeg(getIssuer(),dn);
  }

  /**
   *
   * @param index int ����Ϣ�����
   *    <p>����ŷֲ���</p>
   *     0           cn (CN)<br>
   *     1           country(C)<br>
   *     2           orgnization(O)<br>
   *     3           orgnization unit(OU)<br>
   *     4           state(S)<br>
   *     5           location(L)<br>
   *     6           email(E)<br>
   *     7           givename(G)<br>
   *     8           alias(2.5.4.1)<br>
   *     9           title(T)<br>
   *     10          surname<br>
   *     15          phone<br>
   * @return String �� ����ֵ
   */
  public String getIssuer(int index) {
    String dn = this.dnIndextoString(index);
    if (dn == null) {
      return "";
    }
    return getIssuer(dn);
  }
  /**
   *
   * @return Date �� ֤�����Ч��ֹ����
   */
  public Date getNotAfter()
  {
    return cert.getNotAfter();
  }
  /**
   *
   * @return Date��֤��Ŀ�ʼ���ڣ��䷢���ڣ�
   */
  public Date getNotBefore()
  {
    return cert.getNotBefore();
  }
  /**
   *
   * @return PublicKey ��ȡ��Կ
   */
  public PublicKey getPublicKey()
  {
    return cert.getPublicKey();
  }
  /**
   * ��֤֤���Ƿ�Ϊĳ��Կ��ǩ��
   * @param key PublicKey �� ��֤��ĸ�֤��Ĺ�Կ
   * @throws CertChainVerifyException ��ǩ��ֵ����ʱ����
   */
  public void verify(PublicKey key)
      throws CertChainVerifyException
  {
    try
    {
      cert.verify(key);
    }
    catch(Exception e)
    {
      throw new CertChainVerifyException(e.getMessage());
    }
  }

  /**
   * ��֤֤����Ƿ���ĳ������ʱ��Ч
   * @param date Date �� ����
   * @throws CertDateVerifyException �� ֤��δ��Ч�����ʱ����
   */
  public void checkValidity(Date date)
      throws CertDateVerifyException
  {
    try
    {
      cert.checkValidity(date);
    }
    catch(CertificateExpiredException e)
    {
      throw new CertDateVerifyException("证书已过期");
    }
    catch(CertificateNotYetValidException e)
    {
      throw new CertDateVerifyException("证书未生效");
    }
  }

  /**
   * ��֤֤���ʱ����Ч��
   * @throws CertDateVerifyException �� ֤��δ��Ч�����ʱ����
   */
  public void checkValidity()
      throws CertDateVerifyException
 {
   try
   {
     cert.checkValidity();
   }
   catch(CertificateExpiredException e)
   {
     throw new CertDateVerifyException("证书已过期");
   }
   catch(CertificateNotYetValidException e)
   {
     throw new CertDateVerifyException("证书未生效");
    }
 }

 /**
  * �����ƣ�����ָ���֤�����ǩ������֤��Ĳ����û�֤��һ��Ϊ-1��0��ca֤��Ϊһ��ܴ������
  * @return int �� ȡ������
  */
 public int getBasicConstraints()
  {
    return cert.getBasicConstraints();
  }
  /**
   *
   * @return boolean[] �� ȡ֤��ǩ����ID
   */
  public boolean[] getIssuerUniqueID()
  {
    return cert.getIssuerUniqueID();
  }
  /**
   *
   * @return boolean[] �� ȡ֤�������ID
   */
  public boolean[] getSubjectUniqueID()
  {
    return cert.getSubjectUniqueID();
  }
  /**
   *
   * @return boolean[] : ȡ֤����Կ�÷�
   */
  public boolean[] getKeyUsage()
  {
    return cert.getKeyUsage();
  }
  /**
   *
   * @param use int �� ��Կ�÷���
   * @return boolean �� ֤���Ƿ��ж�Ӧ����Կ�÷�
   */
  public boolean getKeyUsage(int use)
  {
    boolean [] b=cert.getKeyUsage();
    return b[use];
  }

  /**
   *
   * @return byte[] �� ֤��ǩ��
   */
  public byte[] getSignature()
  {
    return cert.getSignature();
  }
  /**
   *
   * @return List : ֤���)չ�÷�
   * @throws CertParsingException
   */
  public List getExtendedKeyUsage() throws CertParsingException
  {
    try
    {
      return cert.getExtendedKeyUsage();
    }
    catch(CertificateParsingException e)
    {
      throw new CertParsingException(e.getMessage());
    }
  }
  /**
   *
   * @return Set �� ȡ���йؼ�)չ��OID
   */
  public Set getCriticalExtensionOIDs()
  {
    return cert.getCriticalExtensionOIDs();
  }
  /**
   *
   * @param oid String �� )չ��OID
   * @return byte[] ��)չ��ֵ
   */
  public byte[] getExtensionValue(String oid)
  {
    return cert.getExtensionValue(oid);
  }
  /**
   *
   * @return Set ��ȡ���зǹؼ�)չ��OID
   */
  public Set getNonCriticalExtensionOIDs()
  {
    return cert.getNonCriticalExtensionOIDs();
  }
  /**
   *
   * @return boolean �� �Ƿ�֧�ֹؼ�)չ
   */
  public boolean hasUnsupportedCriticalExtension()
  {
    return cert.hasUnsupportedCriticalExtension();
  }


  public final static int USAGE_DIGITALSIGNATURE=0;
  public final static int USAGE_NONREPUDIATION=1;
  public final static int USAGE_KEYENCIPHERMENT=2;
  public final static int USAGE_DATAENCIPHERMENT=3;
  public final static int USAGE_KEYAGREEMENT=4;
  public final static int USAGE_KEYCERTSIGN=5;
  public final static int USAGE_CRLSIGN=6;
  public final static int USAGE_ENCIPHERONLY=7;
  public final static int USAGE_DECIPHERONLY=8;

  public final static String OID_SUBJECTKEYID="2.5.29.14";
  public final static String OID_KEYUSAGE="2.5.29.15";
  public final static String OID_PRIVATEKEYUSAGE="2.5.29.16";
  public final static String OID_SUBJECTALTERNATIVENAME="2.5.29.17";
  public final static String OID_ISSUERALTERNATIVENAME="2.5.29.18";
  public final static String OID_BASICCONSTRAINTS="2.5.29.19";
  public final static String OID_NAMEONSTRAINTS="2.5.29.30";
  public final static String OID_POLICYMAP="2.5.29.33";
  public final static String OID_AUTHORITYKEYID="2.5.29.35";
  public final static String OID_POLICYCONSTRAINTS="2.5.29.36";

  public String toString()
  {
    return cert.toString();
  }

  public String getCertId(){
	  String cn=this.getSubject(CertParse.DN_CN);
	  String g=this.getSubject(CertParse.DN_GIVENNAME);
	  if(cn.length()>g.length())
		  return cn;
	  else
		  return g;
  }
  
  /**
   * 
  * @Title: 是否是ECC证书
  * @Description: TODO(这里用一句话描述这个方法的作用)
  * @param @return 设定文件
  * @return boolean 返回类型
  * @throws
   */
  public boolean isECCCert(){
	  if(cert.getPublicKey() instanceof ECPublicKey){
		  return true;
	  }
	  return false;
  }
  X509Certificate cert=null;

  
}
