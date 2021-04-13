package com.tools.parsers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.tools.beans.DataImporterSettings;
import com.tools.parsers.handlers.SAXConfigurationsFileParserHandler;

public class SAXConfigurationsFileParser implements FileParser {
	private SAXConfigurationsFileParserHandler saxParserHandler;

	public List<DataImporterSettings> parseConfigurationsFile(String configurationsFilePath) {
		parseFile(Paths.get(configurationsFilePath));
		return saxParserHandler.getDataImporterSettings();
	}

	@Override
	public void parseFile(Path filePath) {
		try (InputStream inputStream = Files.newInputStream(filePath)) {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParserHandler = new SAXConfigurationsFileParserHandler();
			saxParser.parse(inputStream, saxParserHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
