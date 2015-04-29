package sto.model.account;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;

import sto.utils.IdEntity;

@Entity
@Table(name = "platform_t_district")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class District extends IdEntity<District> {

	private String districtid; //区划编码
	private String districtname; //区划名称
	private String postcode; //邮政编码
	private String isend; //是否末级 1-是，0-否
	private District parent; //上级编码
	private String levelid; //级次码
	private String status; //状态：0-停用，1-正常
	private int orderid; //顺序
	private List<District> children = Lists.newArrayList();	// 拥有子区域列表
	
	private String state;//easyui tree节点是否打开(open)、关闭(closed)
	private String text; //easyui tree节点中text
	
	
	
	public String getDistrictid() {
		return districtid;
	}
	public void setDistrictid(String districtid) {
		this.districtid = districtid;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getIsend() {
		return isend;
	}
	public void setIsend(String isend) {
		this.isend = isend;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="superid")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonBackReference
	public District getParent() {
		return parent;
	}
	public void setParent(District parent) {
		this.parent = parent;
	}
	public String getLevelid() {
		return levelid;
	}
	public void setLevelid(String levelid) {
		this.levelid = levelid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDistrictname() {
		return districtname;
	}
	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
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
		return districtname;
	}
	public void setText(String text) {
		this.text = text;
	}
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@OrderBy(value="orderid") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<District> getChildren() {
		return children;
	}

	public void setChildren(List<District> children) {
		this.children = children;
	}
	
}
