package com.cubit.celerity.util.dto;

import java.io.Serializable;

public class Mail implements Serializable {
	private static final long serialVersionUID = -269970399060351232L;

	private String address;
	
	private String subject;
	
	private String body;

	public Mail() {}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}