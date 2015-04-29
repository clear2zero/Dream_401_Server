package sto.model.meet;


import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 会议室申请
 */
@Entity
@Table(name="oa_meet_apply")
public class MeetApply  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/*标题*/
	private String title;
	/*申请开始时间*/
	private Date beginTime;
	@Transient
	private String beginTimeStr;
	/*申请结束时间*/
	private Date endTime;
	@Transient
	private String endTimeStr;
	/*会议室对象*/
	@OneToOne
	@JoinColumn(name="meet_id",insertable=true,updatable=true)
	private Meet meet;
	/*是否短信提醒*/
	private Boolean isSMS;
	/*内容*/
	private String cont;
	
	/*审核状态  未审核:0  审核通过:1 审核未通过:-1*/
	private int audit;
	
	/*记录创建时间*/
	private Date createTime;

	/*通知的用户*/
	@Transient
	private List<MeetToUser> meetToUserList;
	@Transient
	private String meetToUserStr;
	
	/*单位id*/
	private Long unitId;

	private String createUser;
	
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	
	public List<MeetToUser> getMeetToUserList() {
		return meetToUserList;
	}

	public void setMeetToUserList(List<MeetToUser> meetToUserList) {
		this.meetToUserList = meetToUserList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Meet getMeet() {
		return meet;
	}

	public void setMeet(Meet meet) {
		this.meet = meet;
	}

	

	

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBeginTimeStr() {
		return beginTimeStr;
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public Boolean getIsSMS() {
		return isSMS;
	}

	public void setIsSMS(Boolean isSMS) {
		this.isSMS = isSMS;
	}

	public String getMeetToUserStr() {
		return meetToUserStr;
	}

	public void setMeetToUserStr(String meetToUserStr) {
		this.meetToUserStr = meetToUserStr;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
	

}
