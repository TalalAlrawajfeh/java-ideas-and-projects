package com.tools.parsers.handlers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tools.beans.ConnectionSettings;
import com.tools.beans.DataImportReport;
import com.tools.beans.ParsingError;
import com.tools.jdbc.JDBCConnectionUtility;
import com.tools.jdbc.JDBCQueryBatchHandler;

public abstract class FileParserHandler {
	private JDBCQueryBatchHandler queryBatchHandler = new JDBCQueryBatchHandler();
	private DataImportReport dataImportReport;
	private List<ParsingError> parsingErrors = new ArrayList<>();
	private Connection connection;
	protected ConnectionSettings connectionSettings;
	protected String tableName = "";

	public FileParserHandler(DataImportReport dataImportReport) {
		this.dataImportReport = dataImportReport;
		dataImportReport.setParsingErrors(parsingErrors);
	}

	protected void createNewConnection() throws Exception {
		try {
			connection = JDBCConnectionUtility.getNewConnection(connectionSettings);
		} catch (Exception e) {
			createNewParsingError(e.getMessage(), "");
			dataImportReport.setResult("FAILED");
			dataImportReport.setErrorMessage("Error while establishing connection with database");
			throw new Exception("failed while establishing connection with database");
		}
	}

	protected void closeQueryBatch() throws Exception {
		try {
			queryBatchHandler.closeBatch();
		} catch (Exception e) {
			createNewParsingError(e.getMessage(), "Error while closing query batch");
			setImportReportResultFailedAndParsingError();
			throw new Exception("failed while closing query batch");
		}
	}

	protected void addQueryFromValues(String[] values) throws Exception {
		try {
			queryBatchHandler.addBatch(values);
		} catch (Exception e) {
			createNewParsingError(e.getMessage(), "Error while adding query to batch");
			setImportReportResultFailedAndParsingError();
			throw new Exception("failed while adding query to batch");
		}
	}

	protected void createNewQueryBatchFromColumnNames(String[] columnNames) throws Exception {
		try {
			queryBatchHandler.createNewBatch(connection, tableName, columnNames);
		} catch (Exception e) {
			createNewParsingError(e.getMessage(), "Error while creating new query batch");
			setImportReportResultFailedAndParsingError();
			throw new Exception("failed while creating new query batch");
		}
	}

	protected void finishParsing() {
		try {
			queryBatchHandler.closeBatch();
		} catch (Exception e) {
			createNewParsingError(e.getMessage(), "Error while closing batch");
		}
		closeConnection();
		setDataImportReportResult();
	}

	protected void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			createNewParsingError(e.getMessage(), "");
			dataImportReport.setErrorMessage("Error while closing database connection");
		}
	}

	protected void setImportReportResultFailedAndParsingError() {
		dataImportReport.setResult("FAILED");
		dataImportReport.setErrorMessage("Error while parsing file");
	}

	protected void createNewParsingError(String errorMessage, String optionalInformationAboutError) {
		ParsingError parsingError = new ParsingError();
		parsingError.setErrorMessage(errorMessage);
		parsingError.setErrorType("data-error");
		parsingError.setOptionalInformationAboutError(optionalInformationAboutError);
		parsingErrors.add(parsingError);
	}

	private void setDataImportReportResult() {
		if (parsingErrors.size() == 0) {
			dataImportReport.setResult("SUCCESS");
		} else {
			dataImportReport.setResult("PARTIALLY");
			if (dataImportReport.getErrorMessage().equals("")) {
				dataImportReport.setErrorMessage("Error while parsing file");
			}
		}
	}
}
