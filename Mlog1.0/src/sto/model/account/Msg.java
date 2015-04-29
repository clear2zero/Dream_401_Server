package sto.model.account;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;

@Entity
@Table(name="mlog_msg")
public class Msg {
	private Integer id;
	private String content;
	private String image;
	private String createtime;
	private Integer publisher;
	private String lgt;
	private String lat;
	private String addr;
	private Set<MsgReceiver> msgReceivers = new HashSet<MsgReceiver>();
	private List<MsgReply> msgReply = Lists.newArrayList();
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public Integer getPublisher() {
		return publisher;
	}
	public void setPublisher(Integer publisher) {
		this.publisher = publisher;
	}
	public String getLgt() {
		return lgt;
	}
	public void setLgt(String lgt) {
		this.lgt = lgt;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "msg")
	@Fetch(FetchMode.SUBSELECT)
	@JsonBackReference
	public Set<MsgReceiver> getMsgReceivers() {
		return msgReceivers;
	}
	public void setMsgReceivers(Set<MsgReceiver> msgReceivers) {
		this.msgReceivers = msgReceivers;
	}
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "msg")
	@Fetch(FetchMode.SUBSELECT)
	@JsonBackReference
	public List<MsgReply> getMsgReply() {
		return msgReply;
	}
	public void setMsgReply(List<MsgReply> msgReply) {
		this.msgReply = msgReply;
	}
}
