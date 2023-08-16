package com.cubit.celerity.util.dto;

import java.io.Serializable;
import java.util.Date;

public class UserAudit implements Serializable {
	private static final long serialVersionUID = -6887840322758101795L;

	private String email;

	private String type; // login or register
	
	private Boolean result;
	
	private Boolean notified;

	private Long timestamp;
	
	private Date date;

	public UserAudit() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public Boolean getNotified() {
		return notified;
	}

	public void setNotified(Boolean notified) {
		this.notified = notified;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}