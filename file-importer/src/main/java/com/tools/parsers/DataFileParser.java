package com.tools.parsers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tools.beans.DataImportReport;
import com.tools.beans.DataImporterSettings;
import com.tools.beans.ParsingError;

public abstract class DataFileParser implements FileParser {
	DataImporterSettings dataImporterSettings;
	DataImportReportGenerator dataImportReportGenerator = new DataImportReportGenerator();
	DataImportReport dataImportReport = new DataImportReport();

	public DataFileParser(DataImporterSettings dataImporterSettings) {
		this.dataImporterSettings = dataImporterSettings;
	}

	@Override
	public void parseFile(Path filePath) {
		dataImportReport.setStartTime(LocalDateTime.now());

		try {
			deliverFileToParserHandler(filePath, dataImportReport);
		} catch (Exception e) {
			configureImportReport(e);
		} finally {
			closeAllResources();
		}

		dataImportReport.setEndTime(LocalDateTime.now());

		writeReportFileAndMoveParsedFile(filePath);

		System.out.println("Done processing file: " + filePath.toString());
	}

	protected abstract void deliverFileToParserHandler(Path filePath, DataImportReport dataImportReport)
			throws Exception;

	protected abstract void closeAllResources();

	private void writeReportFileAndMoveParsedFile(Path filePath) {
		try {
			Path resultsDirectoryPath = getResultsDirectoryPath();
			ensureDirectoryExists(resultsDirectoryPath);
			writeReportFile(getReportFileName(filePath.getFileName().toString()), resultsDirectoryPath);
			Files.move(filePath, resultsDirectoryPath.toAbsolutePath().resolve(filePath.getFileName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeReportFile(String fileName, Path resultsDirectoryPath) throws IOException {
		Path reportPath = resultsDirectoryPath.resolve(fileName);
		BufferedWriter bufferedWriter = Files.newBufferedWriter(reportPath);
		bufferedWriter.write(dataImportReportGenerator.generateReport(dataImportReport));
		bufferedWriter.close();
	}

	private Path getResultsDirectoryPath() {
		Path resultsDirectoryPath;
		if (dataImportReport.getErrorMessage() != null) {
			resultsDirectoryPath = dataImporterSettings.getErrorPath();
		} else {
			resultsDirectoryPath = dataImporterSettings.getSuccessPath();
		}
		return resultsDirectoryPath;
	}

	private void ensureDirectoryExists(Path resultPath) throws IOException {
		if (!Files.isDirectory(resultPath)) {
			Files.createDirectories(resultPath);
		}
	}

	private String getReportFileName(String fileName) {
		return fileName.substring(0, fileName.indexOf('.')) + "-report.xml";
	}

	private void configureImportReport(Exception exception) {
		if (dataImportReport.getResult() == null) {
			dataImportReport.setResult("FAILED");
		}
		if (dataImportReport.getErrorMessage() == null) {
			dataImportReport.setErrorMessage("Error while parsing file");
		}
		addParsingErrorToDataImportReport(exception);
	}

	private void addParsingErrorToDataImportReport(Exception exception) {
		ParsingError parsingError = new ParsingError();
		parsingError.setErrorMessage(exception.getMessage());
		parsingError.setOptionalInformationAboutError("Parser exception");
		parsingError.setErrorType("data-error");
		if (dataImportReport.getParsingErrors() == null) {
			List<ParsingError> parsingErrors = new ArrayList<>();
			parsingErrors.add(parsingError);
			dataImportReport.setParsingErrors(parsingErrors);
		} else {
			dataImportReport.getParsingErrors().add(parsingError);
		}
	}
}
