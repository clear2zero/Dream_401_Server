package sto.model.account;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;

@Entity
@Table(name = "mlog_dept")
public class Dept implements Serializable {

	private Integer id;
	private String deptid;
	private String deptname;
	private Integer isleaf;
	private Integer isdelete;
	private List<Dept> children = Lists.newArrayList();	// 拥有子区域列表
	private Dept parent; //上级编码
	private Integer level;
	private String createtime;
	private Integer orderid; //顺序
	
	private String state;//easyui tree节点是否打开(open)、关闭(closed)
	private String text; //easyui tree节点中text
	
	private List<User> contacts = Lists.newArrayList();
	private List<Dept> depts = Lists.newArrayList();
	private String icon;
	private String divid;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public Integer getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
	}
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@OrderBy(value="orderid")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Dept> getChildren() {
		return children;
	}

	public void setChildren(List<Dept> children) {
		this.children = children;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentid")
	@NotNull
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonBackReference
	public Dept getParent() {
		return parent;
	}
	public void setParent(Dept parent) {
		this.parent = parent;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	@Transient
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Transient
	public String getText() {
		return deptname;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@OneToMany(mappedBy = "dept", fetch=FetchType.LAZY,cascade={CascadeType.REFRESH})
	@NotFound(action = NotFoundAction.IGNORE)
	@Where(clause="isdelete=0 and isenable=1 ")
	@JsonBackReference
	public List<User> getContacts() {
		return contacts;
	}

	public void setContacts(List<User> contacts) {
		this.contacts = contacts;
	}
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@OrderBy(value="orderid")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonBackReference
	public List<Dept> getDepts() {
		return depts;
	}

	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}
	@Transient
	public String getIcon() {
		return "../_static/images/group.jpg";
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDivid() {
		return divid;
	}

	public void setDivid(String divid) {
		this.divid = divid;
	}
	
	

}