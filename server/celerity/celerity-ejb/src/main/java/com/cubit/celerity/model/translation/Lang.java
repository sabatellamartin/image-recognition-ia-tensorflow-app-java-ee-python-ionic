package com.cubit.celerity.model.translation;

import java.io.Serializable;

public class Lang implements Serializable {
	private static final long serialVersionUID = -8491558537089874973L;

	private Integer ordinal;
	
	private String name;
	
	private String code;
	
	public Lang() {}

	public Lang(Integer ordinal, String name, String code) {
		this.name = name;
		this.code = code;
		this.ordinal = ordinal;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
