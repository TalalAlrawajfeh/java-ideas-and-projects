package com.tools.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.tools.beans.DataImportReport;
import com.tools.beans.DataImporterSettings;
import com.tools.parsers.handlers.SAXDataFileParserHandler;

public class SAXDataFileParser extends DataFileParser {
	private InputStream inputStream = null;

	public SAXDataFileParser(DataImporterSettings dataImporterSettings) {
		super(dataImporterSettings);
	}

	@Override
	protected void deliverFileToParserHandler(Path filePath, DataImportReport dataImportReport) throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		inputStream = Files.newInputStream(filePath);
		saxParser.parse(inputStream, new SAXDataFileParserHandler(dataImportReport));
	}

	@Override
	protected void closeAllResources() {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
