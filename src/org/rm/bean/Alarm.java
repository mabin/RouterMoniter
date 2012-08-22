package org.rm.bean;

import java.sql.Date;
import java.sql.Timestamp;

import org.rm.core.basebean;

/**
 * AlarmEmail entity. @author MyEclipse Persistence Tools
 */

public class Alarm extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String title;
	private String content;
	private String email;
	private String phone;
	private String sent;
	private String unsent;
	private String time;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getSent() {
		return sent;
	}


	public void setSent(String sent) {
		this.sent = sent;
	}


	public String getUnsent() {
		return unsent;
	}


	public void setUnsent(String unsent) {
		this.unsent = unsent;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "alarm";
	}

}