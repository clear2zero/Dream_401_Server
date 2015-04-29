package other;

import java.io.Serializable;
import java.util.List;

import sto.model.account.Role;
import sto.model.account.User;

/**
 * @author Umut Gokbayrak
 *changed by hebca wuzhifeng 2007.2.9
 */
public class AuthProfile implements Serializable{
    private String username;
    private String password;
    private String pkcsCode;
    private String authMode;
    private String caMode;
    private String lastLoginDate;
    private Boolean admin;
    private SettingCacheProfile settingCache;
    private String certIdentify;
    private Boolean sealadmin;
    //add by shenqn 2012-09-03
    private Long uid;//单位ID
    private Role role;//角色
    private String isPass;//是否通过审核
    private int status;
    private String visitDomain;//多域名下，访问的域名
    //add by Linxiao 证书是否禁用
    private Boolean enabled;
	private List<Long> departmentId;  //当前用户所属的部门ID列表
    private List<Long> manageDepartmentId;  //当前部门管理员能够管理的部门ID列表
    private User user;
    public List<Long> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Long> departmentId) {
		this.departmentId = departmentId;
	}

	public List<Long> getManageDepartmentId() {
		return manageDepartmentId;
	}

	public void setManageDepartmentId(List<Long> manageDepartmentId) {
		this.manageDepartmentId = manageDepartmentId;
	}
    
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getIsPass() {
		return isPass;
	}

	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}

	public Boolean getSealadmin() {
		return sealadmin;
	}

	public void setSealadmin(Boolean sealadmin) {
		this.sealadmin = sealadmin;
	}

	public String getCaMode() {
		return caMode;
	}

	public void setCaMode(String caMode) {
		this.caMode = caMode;
	}

	public SettingCacheProfile getSettingCache() {
		return settingCache;
	}

	public void setSettingCache(SettingCacheProfile settingCache) {
		this.settingCache = settingCache;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getPkcsCode() {
        return pkcsCode;
    }

    public void setPkcsCode(String pkcsCode) {
        this.pkcsCode = pkcsCode;
    }

    public AuthProfile() {
        super();
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    public String getVisitDomain() {
		return visitDomain;
	}

	public void setVisitDomain(String visitDomain) {
		
		this.visitDomain = visitDomain;
	}

	/**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

	public String getCertIdentify() {
		return certIdentify;
	}

	public void setCertIdentify(String certIdentify) {
		this.certIdentify = certIdentify;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    
}
