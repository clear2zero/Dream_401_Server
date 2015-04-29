/*
 * 文件名: UserCert.java
 * 版本信息: 
 * 创建人: echo
 * 创建日期: Mar 8, 2007
 */
package other;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>说明: </P>
 * <p>描述: </p>
 * <p>版权: Copyright(c)2006</p>
 * <p>公司(团体): Hebca</p>
 * @author echo 
 * @version 1.0
 */
public class UserCert implements Serializable {
    private Long id;
    private String user;
    private String certId;
    private String userPass;
    private byte [] signCert;
    private byte [] cryptCert;
    
    //add by shenqn 2012.5.18
    private String platform;
    private String deviceType;
    private String keyType;
    private Boolean useMessage;
    private Boolean enable;
//    private Date lastLoginDate;
    private String hash;
  //add by zhangzhiguang 2012.9.5
    private Long uid;//单位ID
    private Integer state;//用户状态（正常:0 冻结-1 审核未通过 -2）
    private String role;//用户角色
    private Long mailcapacity;//邮箱容量
    private Long netdiskcapacity;//网盘容量
    //add by hgy 2013.4.28
    private String mobile_client_version;
    private Long userApplyId=0L;//对应手机用户申请表user_apply的id
    private String checkMailToken;//检测新邮件登录凭证
    
    public String getMobile_client_version() {
		return mobile_client_version;
	}
	public void setMobile_client_version(String mobile_client_version) {
		this.mobile_client_version = mobile_client_version;
	}
	public Long getNetdiskcapacity() {
		return netdiskcapacity;
	}
	public void setNetdiskcapacity(Long netdiskcapacity) {
		this.netdiskcapacity = netdiskcapacity;
	}
	public Long getMailcapacity() {
		return mailcapacity;
	}
	public void setMailcapacity(Long mailcapacity) {
		this.mailcapacity = mailcapacity;
	}
	public String getPlatform()
    {
        return platform;
    }
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }
    public String getDeviceType()
    {
        return deviceType;
    }
    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }
    public String getKeyType()
    {
        return keyType;
    }
    public void setKeyType(String keyType)
    {
        this.keyType = keyType;
    }
    public Boolean getUseMessage()
    {
        return useMessage;
    }
    public void setUseMessage(Boolean useMessage)
    {
        this.useMessage = useMessage;
    }
    public Boolean getEnable()
    {
        return enable;
    }
    public void setEnable(Boolean enable)
    {
        this.enable = enable;
    }
//    public Date getLastLoginDate()
//    {
//        return lastLoginDate;
//    }
//    public void setLastLoginDate(Date lastLoginDate)
//    {
//        this.lastLoginDate = lastLoginDate;
//    }
    public String getHash()
    {
        return hash;
    }
    public void setHash(String hash)
    {
        this.hash = hash;
    }
    public byte[] getCryptCert() {
        return cryptCert;
    }
    public void setCryptCert(byte[] cryptCert) {
        this.cryptCert = cryptCert;
    }
    public byte[] getSignCert() {
        return signCert;
    }
    public void setSignCert(byte[] signCert) {
        this.signCert = signCert;
    }
    public String getCertId() {
        return certId;
    }
    public void setCertId(String certId) {
        this.certId = certId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUserPass() {
        return userPass;
    }
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getUserApplyId() {
		return userApplyId;
	}
	public void setUserApplyId(Long userApplyId) {
		this.userApplyId = userApplyId;
	}
	public String getCheckMailToken() {
		return checkMailToken;
	}
	public void setCheckMailToken(String checkMailToken) {
		this.checkMailToken = checkMailToken;
	}

    
    
}
