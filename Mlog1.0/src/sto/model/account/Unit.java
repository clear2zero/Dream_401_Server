package sto.model.account;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mlog_unit")
public class Unit implements Serializable{
	private Integer id;
	private String projectid;
	private String parentid;
	private String divid;
	private String adminuser;
	private String divname;
	private String addr;
	private String tel;
	private String linkman;
	private String corporation;
	private Integer licence;
	private String acceptno;
	private Integer isregistered;
	private Integer initstep;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getDivid() {
		return divid;
	}
	public void setDivid(String divid) {
		this.divid = divid;
	}
	public String getAdminuser() {
		return adminuser;
	}
	public void setAdminuser(String adminuser) {
		this.adminuser = adminuser;
	}
	public String getDivname() {
		return divname;
	}
	public void setDivname(String divname) {
		this.divname = divname;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	public Integer getLicence() {
		return licence;
	}
	public void setLicence(Integer licence) {
		this.licence = licence;
	}
	public String getAcceptno() {
		return acceptno;
	}
	public void setAcceptno(String acceptno) {
		this.acceptno = acceptno;
	}
	public Integer getIsregistered() {
		return isregistered;
	}
	public void setIsregistered(Integer isregistered) {
		this.isregistered = isregistered;
	}
	public Integer getInitstep() {
		return initstep;
	}
	public void setInitstep(Integer initstep) {
		this.initstep = initstep;
	}
}
