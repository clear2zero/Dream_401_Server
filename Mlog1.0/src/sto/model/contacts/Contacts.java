/**   
 * @Title: Contacts.java 
 * @Package sto.model 
 * @author chenxiaojia  
 * @date 2014-9-25 下午2:28:41 
 * @version V1.0   
 */
package sto.model.contacts;

/**
 * @ClassName: Contacts
 * @Description:
 * @author chenxiaojia
 * @date 2014-9-25 下午2:28:41
 * 
 */
public class Contacts {
	private long contactId;
	private long groupId;
	private String groupName;
	//private String user;
	private String lastName;
	private String sex;
	private String gsmNoPrimary;
	private String emailPrimary;
	private String birthday;
	private String workAddress;
	private String workPhone;
	private String workDepartment;
    private String workCompany;
    private String certInfo;
	public String getCertInfo() {
		return certInfo;
	}

	public void setCertInfo(String certInfo) {
		this.certInfo = certInfo;
	}

	public String getWorkCompany() {
		return workCompany;
	}

	public void setWorkCompany(String workCompany) {
		this.workCompany = workCompany;
	}

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long id) {
		this.contactId = id;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/*public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}*/

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getGsmNoPrimary() {
		return gsmNoPrimary;
	}

	public void setGsmNoPrimary(String gsmNoPrimary) {
		this.gsmNoPrimary = gsmNoPrimary;
	}

	public String getEmailPrimary() {
		return emailPrimary;
	}

	public void setEmailPrimary(String emailPrimary) {
		this.emailPrimary = emailPrimary;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getWorkDepartment() {
		return workDepartment;
	}

	public void setWorkDepartment(String workDepartment) {
		this.workDepartment = workDepartment;
	}

}
