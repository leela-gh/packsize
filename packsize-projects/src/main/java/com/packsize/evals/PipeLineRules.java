package com.packsize.evals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PipeLineRules {
	
	private String ruleId = "0a3ba6eb-aa3a-49ae-9a59-2d7537a38dbd";
	private String condition;
	private String name = "Test Rule";
	private String description = "Test Desc";
	private boolean isEnabled = true;
	private List<PipeLineAction> actions = new ArrayList<PipeLineAction>();
	private String notes = "Test Notes";
	private boolean isValid = true;
	private String[] validationErrors = {};
	private String type = "Conditional";
	
	
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public List<PipeLineAction> getActions() {
		return actions;
	}
	public void setActions(List<PipeLineAction> actions) {
		this.actions = actions;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String[] getValidationErrors() {
		return validationErrors;
	}
	public void setValidationErrors(String[] validationErrors) {
		this.validationErrors = validationErrors;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/*
	 * @Override public String toString() { return "PipeLineRules [ruleId=" + ruleId
	 * + ", condition=" + condition + ", name=" + name + ", description=" +
	 * description + ", isEnabled=" + isEnabled + ", actions=" + actions +
	 * ", notes=" + notes + ", isValid=" + isValid + ", validationErrors=" +
	 * Arrays.toString(validationErrors) + ", type=" + type + "]"; }
	 */	
	
	@Override
	public String toString() {
		return "PipeLineRules [condition=" + condition + ", actions=" + actions + "]";
	}

}
