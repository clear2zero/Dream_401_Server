package other;

//缓存用户设置信息，避免频繁访问数据库
public class SettingCacheProfile {
    private String fullName;
    private String fromAddress;
    private boolean bindCert;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public boolean isBindCert() {
		return bindCert;
	}
	public void setBindCert(boolean bindCert) {
		this.bindCert = bindCert;
	}
    
    
}
