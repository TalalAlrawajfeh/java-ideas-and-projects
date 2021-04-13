package com.tools.parsers;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.tools.beans.DataImportReport;
import com.tools.beans.DataImporterSettings;
import com.tools.parsers.handlers.CSVDataFileParserHandler;

public class CSVDataFileParser extends DataFileParser {
	private BufferedReader bufferedReader = null;

	public CSVDataFileParser(DataImporterSettings dataImporterSettings) {
		super(dataImporterSettings);
	}

	@Override
	protected void deliverFileToParserHandler(Path filePath, DataImportReport dataImportReport) throws Exception {
		CSVDataFileParserHandler csvDataFileParserHandler = new CSVDataFileParserHandler(dataImportReport);
		bufferedReader = Files.newBufferedReader(filePath);
		csvDataFileParserHandler.parse(bufferedReader);
	}

	@Override
	protected void closeAllResources() {
		try {
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
