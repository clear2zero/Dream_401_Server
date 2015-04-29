package sto.model.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mlog_app")
public class App {
	private Integer id;
	private String apkversion;
	private int isforceupdate;
	private String downloadurl;
	private String description;
	private int status;
	private int publisher;
	private String publishtime;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApkversion() {
		return apkversion;
	}
	public void setApkversion(String apkversion) {
		this.apkversion = apkversion;
	}
	public int getIsforceupdate() {
		return isforceupdate;
	}
	public void setIsforceupdate(int isforceupdate) {
		this.isforceupdate = isforceupdate;
	}
	public String getDownloadurl() {
		return downloadurl;
	}
	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPublisher() {
		return publisher;
	}
	public void setPublisher(int publisher) {
		this.publisher = publisher;
	}
	public String getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}
}
