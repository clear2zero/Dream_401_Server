package sto.model.account;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name = "mlog_journal_reply")
public class JournalReply implements Serializable {

	private Integer id;
	private Integer replyer;
	private String recontent;
	private String reimage;
	private String redate;
	private Journal journal;
	private Integer replyto;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getReplyer() {
		return replyer;
	}

	public void setReplyer(Integer replyer) {
		this.replyer = replyer;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "journalid")
	public Journal getJournal() {
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Integer getReplyto() {
		return replyto;
	}

	public void setReplyto(Integer replyto) {
		this.replyto = replyto;
	}

}