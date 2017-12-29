package com.ortus.narco.utils;

public enum ExceptionTypes {
	NOT_FOUND("The drug type %s was not found."),
	EFFECT_NOT_FOUND("The effect %s was not found.");
	
	final String exception;
	
	private ExceptionTypes(final String exception) {
		this.exception = exception;
	}
	
	public String getException(String field) {
		return String.format(exception, field);
	}
	
	public String getException() {
		return exception.replace("%s ", "");
	}
}