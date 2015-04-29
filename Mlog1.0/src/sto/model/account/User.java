package sto.model.account;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @ClassName: User
 * @Description:
 * @author zzh
 * @date 2014-10-31 11:07:12
 * 
 */
@Entity
@Table(name = "platform_t_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable{
	
	private Integer id;
	private String username; //用户名
	private String name;//角色英文名称
	private String password; 
	private String hcertcn; 
	private String scertcn; 
	private String identitycard; 
	private String mobilephone; 
	private String telephone; 
	private String extension;
	
	private Dept dept;
	private Role role;
	private Integer clientrole;
	private Integer isdelete;
	private Integer isenable; //是否可用 0-否 1-是
	private String rolename;
	private String deptname;
	private Integer userid;
	private Unit unit;
	private String divname;
	private String pushchannelid;
	private String pushuserid;
	private String imei;
	private String deviceid;
	private String packageversion;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHcertcn() {
		return hcertcn;
	}
	public void setHcertcn(String hcertcn) {
		this.hcertcn = hcertcn;
	}
	public String getScertcn() {
		return scertcn;
	}
	public void setScertcn(String scertcn) {
		this.scertcn = scertcn;
	}
	public String getIdentitycard() {
		return identitycard;
	}
	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "deptid")
	@NotFound(action = NotFoundAction.IGNORE)
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "roleid")
	@JsonBackReference
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Integer getClientrole() {
		return clientrole;
	}
	public void setClientrole(Integer clientrole) {
		this.clientrole = clientrole;
	}
	public Integer getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
	}
	public Integer getIsenable() {
		return isenable;
	}
	public void setIsenable(Integer isenable) {
		this.isenable = isenable;
	}
	@Transient
	public String getRolename() {
		if(null != role){
			return role.getName();
		}else {
			return "";
		}
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	@Transient
	public String getDeptname() {
		if(null != dept){
			return dept.getDeptname();
		}else {
			return "";
		}
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	@Transient
	public Integer getUserid() {
		return this.id;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "divid",referencedColumnName = "divid", unique = true, nullable = true)
	@NotFound(action = NotFoundAction.IGNORE)
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	@Transient
	public String getDivname() {
		if(null != unit){
			return unit.getDivname();
		}else {
			return "";
		}
	}
	public void setDivname(String divname) {
		this.divname = divname;
	}
	public String getPushchannelid() {
		return pushchannelid;
	}
	public void setPushchannelid(String pushchannelid) {
		this.pushchannelid = pushchannelid;
	}
	public String getPushuserid() {
		return pushuserid;
	}
	public void setPushuserid(String pushuserid) {
		this.pushuserid = pushuserid;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getPackageversion() {
		return packageversion;
	}
	public void setPackageversion(String packageversion) {
		this.packageversion = packageversion;
	}
}
