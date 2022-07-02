package com.acme.result;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable{
	
	private static final long serialVersionUID = -8411361783619578365L;

	private boolean completed = true;

	private String errorMsg;

	private List<String> violations;

	private Object data;

	public Result() {
	}

	public Result(Object data) {
		this.completed = true;
		this.data = data;
	}

	public Result(String error) {
		this.completed = false;
		this.errorMsg = error;
	}

	public Result(List<String> violations) {
		this.errorMsg = "Validation Error";
		this.completed = false;
		this.violations = violations;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<String> getViolations() {
		return violations;
	}

	public void setViolations(List<String> violations) {
		this.violations = violations;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
	


}
