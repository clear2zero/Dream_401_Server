package sto.model.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mlog_attend")
public class Attend {
	
	private Integer id;
	private Integer userid;
	private String onlgt;
	private String onlat;
	private String onaddr;
	private String ontime;
	private String offlgt;
	private String offlat;
	private String offaddr;
	private String offtime;
	private String lasttime;
	private Integer status;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getOnlgt() {
		return onlgt;
	}
	public void setOnlgt(String onlgt) {
		this.onlgt = onlgt;
	}
	public String getOnlat() {
		return onlat;
	}
	public void setOnlat(String onlat) {
		this.onlat = onlat;
	}
	public String getOnaddr() {
		return onaddr;
	}
	public void setOnaddr(String onaddr) {
		this.onaddr = onaddr;
	}
	public String getOntime() {
		return ontime;
	}
	public void setOntime(String ontime) {
		this.ontime = ontime;
	}
	public String getOfflgt() {
		return offlgt;
	}
	public void setOfflgt(String offlgt) {
		this.offlgt = offlgt;
	}
	public String getOfflat() {
		return offlat;
	}
	public void setOfflat(String offlat) {
		this.offlat = offlat;
	}
	public String getOffaddr() {
		return offaddr;
	}
	public void setOffaddr(String offaddr) {
		this.offaddr = offaddr;
	}
	public String getOfftime() {
		return offtime;
	}
	public void setOfftime(String offtime) {
		this.offtime = offtime;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
