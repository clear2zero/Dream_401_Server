package sto.model.account;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="mlog_msg_reply")
public class MsgReply {
	private Integer id;
	private String recontent;
	private String reimage;
	private String redate;
	private Integer replyer;
	private Integer replyto;
	private Msg msg;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRecontent() {
		return recontent;
	}
	public void setRecontent(String recontent) {
		this.recontent = recontent;
	}
	public String getReimage() {
		return reimage;
	}
	public void setReimage(String reimage) {
		this.reimage = reimage;
	}
	public String getRedate() {
		return redate;
	}
	public void setRedate(String redate) {
		this.redate = redate;
	}
	public Integer getReplyer() {
		return replyer;
	}
	public void setReplyer(Integer replyer) {
		this.replyer = replyer;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "msgid")
	public Msg getMsg() {
		return msg;
	}
	public void setMsg(Msg msg) {
		this.msg = msg;
	}
	public Integer getReplyto() {
		return replyto;
	}
	public void setReplyto(Integer replyto) {
		this.replyto = replyto;
	}
	
}
