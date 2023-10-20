package com.packsize.evals;

public class PipeLineAction {
	
	private String id = "0a3ba6eb-aa3a-49ae-9a59-2d7537a38dbd";
	private String[] validationErrors = {};
	private Configuration configuration;
	private String type = "SetValue";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getValidationErrors() {
		return validationErrors;
	}
	public void setValidationErrors(String[] validationErrors) {
		this.validationErrors = validationErrors;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/*
	 * @Override public String toString() { return "PipeLineAction [id=" + id +
	 * ", validationErrors=" + Arrays.toString(validationErrors) +
	 * ", configuration=" + configuration + ", type=" + type + "]"; }
	 */
	
	@Override
	public String toString() {
		return "PipeLineAction [configuration=" + configuration + "]";
	}
		

}
