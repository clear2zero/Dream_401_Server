package sto.model.meet;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 通知开会人
 */
@Entity
@Table(name="oa_meet_toUser")
public class MeetToUser  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/*会议室id*/
	private Long meetApplyId;
	/*通知开会人*/
	private String user;
	/*未读:0  已读:1 */
	private int isRead;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMeetApplyId() {
		return meetApplyId;
	}
	public void setMeetApplyId(Long meetApplyId) {
		this.meetApplyId = meetApplyId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	
	
	

}
