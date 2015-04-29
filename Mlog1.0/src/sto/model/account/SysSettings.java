package sto.model.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mlog_system_settings")
public class SysSettings {
	
	private Integer id;
	private String name;
	private String skey;
	private String value;
	private Integer iscommon;
	private Integer isvisible;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSkey() {
		return skey;
	}
	public void setSkey(String skey) {
		this.skey = skey;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getIscommon() {
		return iscommon;
	}
	public void setIscommon(Integer iscommon) {
		this.iscommon = iscommon;
	}
	public Integer getIsvisible() {
		return isvisible;
	}
	public void setIsvisible(Integer isvisible) {
		this.isvisible = isvisible;
	}
}
