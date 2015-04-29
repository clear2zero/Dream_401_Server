package sto.model.account;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;

import sto.utils.IdGen;

/**
 * @ClassName: Role
 * @Description:
 * @author zzh
 * @date 2014-10-31 11:07:12
 * 
 */
@Entity
@Table(name = "platform_t_role")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role {
	
	private Integer id; //角色id
	private String name; //角色名称
	private String enname;//角色英文名称
	private String remark; //备注
	private int orderid; //顺序 
	private Integer status; //状态 0-停用；1-正常
	private Set<RoleModule> roleauths = new HashSet<RoleModule>(); //角色菜单权限表
	private List<RoleUser> roleUser = Lists.newArrayList();	// 拥有此权限的用户列表
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	@Fetch(FetchMode.SUBSELECT)
	@JsonBackReference
	public Set<RoleModule> getRoleauths() {
		return roleauths;
	}
	public void setRoleauths(Set<RoleModule> roleauths) {
		this.roleauths = roleauths;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	@Fetch(FetchMode.SUBSELECT)
	@JsonBackReference
	public List<RoleUser> getRoleUser() {
		return roleUser;
	}
	public void setRoleUser(List<RoleUser> roleUser) {
		this.roleUser = roleUser;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
