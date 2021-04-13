package com.tools.parsers.handlers;

import java.io.BufferedReader;

import com.tools.beans.ConnectionSettings;
import com.tools.beans.DataImportReport;

public class CSVDataFileParserHandler extends FileParserHandler {
	private static final int SETTINGS_COUNT = 4;
	private static final int USERNAME_INDEX = 0;
	private static final int PASSWORD_INDEX = 1;
	private static final int URL_INDEX = 2;
	private static final int DRIVER_CLASS_NAME_INDEX = 3;

	private String[] columnNames;

	public CSVDataFileParserHandler(DataImportReport dataImportReport) {
		super(dataImportReport);
	}

	public void parse(BufferedReader bufferedReader) throws Exception {
		getConnectionSettings(bufferedReader);
		createNewConnection();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();
			parseLine(line);
		}
		finishParsing();
	}

	private void parseLine(String line) throws Exception {
		if (line.equals("")) {
			return;
		}
		if (line.startsWith("table#")) {
			int indexOfFirstComma = line.indexOf(',');
			checkCommaIfPresent(line, indexOfFirstComma);
			String columns = line.substring(indexOfFirstComma + 1).trim();
			checkIfColumnsNonEmpty(line, columns);
			columnNames = columns.split(",");
			tableName = line.substring(line.indexOf('#') + 1, indexOfFirstComma).trim();
			checkTableNameIfPresent(line);
			closeQueryBatch();
			createNewQueryBatchFromColumnNames(columnNames);
		} else {
			String[] values = line.split(",");
			checkValuesCount(line, values);
			addQueryFromValues(values);
		}
	}

	private void checkValuesCount(String line, String[] values) throws Exception {
		if (values.length != columnNames.length) {
			createAndThrowParsingError(line);
		}
	}

	private void checkTableNameIfPresent(String line) throws Exception {
		if (tableName == "") {
			createAndThrowParsingError(line);
		}
	}

	private void checkIfColumnsNonEmpty(String line, String columns) throws Exception {
		if (columns == "") {
			createAndThrowParsingError(line);
		}
	}

	private void checkCommaIfPresent(String line, int indexOfFirstComma) throws Exception {
		if (indexOfFirstComma == -1) {
			createAndThrowParsingError(line);
		}
	}

	private void createAndThrowParsingError(String line) throws Exception {
		createNewParsingError("File parsing error", "Invalid format, line: " + line);
		throw new Exception("Error while parsing file");
	}

	private void createNewConnectionSettings(String[] settings) {
		connectionSettings = new ConnectionSettings();
		connectionSettings.setUsername(settings[USERNAME_INDEX]);
		connectionSettings.setPassword(settings[PASSWORD_INDEX]);
		connectionSettings.setUrl(settings[URL_INDEX]);
		connectionSettings.setDriverClassName(settings[DRIVER_CLASS_NAME_INDEX]);
	}

	private void getConnectionSettings(BufferedReader bufferedReader) throws Exception {
		String line = "";
		while (line != null) {
			if (!line.trim().equals("")) {
				break;
			}
			line = bufferedReader.readLine();
		}
		if (line != null) {
			String[] settings = line.split(",");
			checkSettingsCount(line, settings);
			createNewConnectionSettings(settings);
		}
	}

	private void checkSettingsCount(String line, String[] settings) throws Exception {
		if (settings.length != SETTINGS_COUNT) {
			createNewParsingError("Connection settings parsing error",
					"There are necessary connection settings missing in line: " + line);
			throw new Exception("Error while establishing connection");
		}
	}
}
