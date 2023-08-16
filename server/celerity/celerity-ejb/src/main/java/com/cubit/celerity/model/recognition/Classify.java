package com.cubit.celerity.model.recognition;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Classify implements Serializable {
	private static final long serialVersionUID = 7190511853861510928L;
	
	private String name;
	
	private String path;
	
	private List<Prediction> predictions;
	
	private List<Prediction> translated;
	
	public Classify() {}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public List<Prediction> getPredictions() {
		return predictions;
	}
	
	public void setPredictions(List<Prediction> predictions) {
		this.predictions = predictions;
	}

	public List<Prediction> getTranslated() {
		return translated;
	}

	public void setTranslated(List<Prediction> translated) {
		this.translated = translated;
	}	
	
}
