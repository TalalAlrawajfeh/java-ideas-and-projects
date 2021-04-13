package com.tools.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class DataImportReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private String result;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String errorMessage;
	private List<ParsingError> parsingErrors;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<ParsingError> getParsingErrors() {
		return parsingErrors;
	}

	public void setParsingErrors(List<ParsingError> parsingErrors) {
		this.parsingErrors = parsingErrors;
	}
}
