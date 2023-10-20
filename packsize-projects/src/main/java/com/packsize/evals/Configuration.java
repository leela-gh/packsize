package com.packsize.evals;

public class Configuration {
	private String target;
	private String valueExpression;
	private String elseTarget;
	private String elseTargetValueExpression;
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getValueExpression() {
		return valueExpression;
	}
	public void setValueExpression(String valueExpression) {
		this.valueExpression = valueExpression;
	}
	public String getElseTarget() {
		return elseTarget;
	}
	public void setElseTarget(String elseTarget) {
		this.elseTarget = elseTarget;
	}
	public String getElseTargetValueExpression() {
		return elseTargetValueExpression;
	}
	public void setElseTargetValueExpression(String elseTargetValueExpression) {
		this.elseTargetValueExpression = elseTargetValueExpression;
	}
	@Override
	public String toString() {
		return "Configuration [target=" + target + ", valueExpression=" + valueExpression + ", elseTarget=" + elseTarget
				+ ", elseTargetValueExpression=" + elseTargetValueExpression + "]";
	}
	
	
}
