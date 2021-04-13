package com.tools.beans;

public class ParsingError {
	private String errorType;
	private String errorMessage;
	private String optionalInformationAboutError;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getOptionalInformationAboutError() {
		return optionalInformationAboutError;
	}

	public void setOptionalInformationAboutError(String optionalInformationAboutError) {
		this.optionalInformationAboutError = optionalInformationAboutError;
	}
}
