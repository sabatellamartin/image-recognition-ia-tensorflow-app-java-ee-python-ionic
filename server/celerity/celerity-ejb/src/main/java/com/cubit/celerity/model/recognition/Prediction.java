package com.cubit.celerity.model.recognition;

import java.io.Serializable;

public class Prediction implements Serializable {
	private static final long serialVersionUID = 6901139242651869830L;
	
	private String description;
	
	private Float score;
	
	public Prediction() {}

	public Prediction(String description, Float score) {
		super();
		this.description = description;
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
