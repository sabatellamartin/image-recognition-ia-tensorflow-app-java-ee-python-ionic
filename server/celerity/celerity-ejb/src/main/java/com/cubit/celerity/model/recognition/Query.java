package com.cubit.celerity.model.recognition;

import java.io.Serializable;
import java.util.Date;

import com.cubit.celerity.util.yandex.params.Language;

public class Query implements Serializable {
	private static final long serialVersionUID = -6415472466481971794L;

	private String _id;

	private String outputFile;
	
	private Long userId;

	private String userEmail;

	private Language from;

	private Language to;
	
	private String langcode;

	private Boolean active;

	private Date date;

	private long timestamp;
	
	private String encodedFile;
	
	public Query() {}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Language getFrom() {
		return from;
	}

	public void setFrom(Language from) {
		this.from = from;
	}

	public Language getTo() {
		return to;
	}

	public void setTo(Language to) {
		this.to = to;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getEncodedFile() {
		return encodedFile;
	}

	public void setEncodedFile(String encodedFile) {
		this.encodedFile = encodedFile;
	}

	public String getLangcode() {
		return langcode;
	}

	public void setLangcode(String langcode) {
		this.langcode = langcode;
	}
	
}