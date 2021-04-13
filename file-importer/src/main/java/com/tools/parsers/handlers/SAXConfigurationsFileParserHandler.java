package com.tools.parsers.handlers;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.tools.beans.DataImporterSettings;
import com.tools.parsers.utility.SAXDataFileParserHandlerReflectionUtility;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXConfigurationsFileParserHandler extends DefaultHandler {
	private String currentElement;
	private List<DataImporterSettings> dataImporterSettingsList = new ArrayList<>();
	private DataImporterSettings dataImporterSettings;

	public List<DataImporterSettings> getDataImporterSettings() {
		return dataImporterSettingsList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("import-setting")) {
			dataImporterSettings = new DataImporterSettings();
		}
		currentElement = qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement = "";
		if (qName.equalsIgnoreCase("import-setting")) {
			dataImporterSettingsList.add(dataImporterSettings);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String elementValue = new String(ch, start, length).trim();
		if (!elementValue.equals("")) {
			String setterMethodName = SAXDataFileParserHandlerReflectionUtility
					.getSetterNameFromElementName(currentElement);
			try {
				SAXDataFileParserHandlerReflectionUtility.invokeSetterMethodIfPresent(dataImporterSettings,
						setterMethodName, Paths.get(elementValue));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}