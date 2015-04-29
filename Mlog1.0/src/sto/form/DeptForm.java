package sto.form;

import java.util.List;

import com.google.common.collect.Lists;

public class DeptForm {
	private String deptid;
	private String deptname;
	private List<DeptForm> depts = Lists.newArrayList();
	private List<UserForm> contacts = Lists.newArrayList();
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public List<DeptForm> getDepts() {
		return depts;
	}
	public void setDepts(List<DeptForm> depts) {
		this.depts = depts;
	}
	public List<UserForm> getContacts() {
		return contacts;
	}
	public void setContacts(List<UserForm> contacts) {
		this.contacts = contacts;
	}
	
}
