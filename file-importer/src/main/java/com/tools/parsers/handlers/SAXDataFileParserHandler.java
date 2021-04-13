package com.tools.parsers.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.tools.beans.ConnectionSettings;
import com.tools.beans.DataImportReport;
import com.tools.parsers.utility.SAXDataFileParserHandlerReflectionUtility;

public class SAXDataFileParserHandler extends DefaultHandler {
	private boolean enteredConnectionSettingsTag = false;
	private boolean enteredDataTag = false;
	private SAXDataFileDefaultHander saxDataFileDefaultHander;

	public SAXDataFileParserHandler(DataImportReport dataImportReport) {
		saxDataFileDefaultHander = new SAXDataFileDefaultHander(dataImportReport);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		saxDataFileDefaultHander.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		saxDataFileDefaultHander.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		saxDataFileDefaultHander.characters(ch, start, length);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		saxDataFileDefaultHander.error(e);
	}

	private class SAXDataFileDefaultHander extends FileParserHandler {
		private String settingName = "";

		public SAXDataFileDefaultHander(DataImportReport dataImportReport) {
			super(dataImportReport);
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (qName.equalsIgnoreCase("connection-settings")) {
				connectionSettings = new ConnectionSettings();
				enteredConnectionSettingsTag = true;
				return;
			}
			if (enteredConnectionSettingsTag) {
				settingName = qName;
				return;
			}
			if (qName.equalsIgnoreCase("data")) {
				enteredDataTag = true;
				return;
			}
			parseDataElements(qName, attributes);
		}

		private void parseDataElements(String qName, Attributes attributes) throws SAXException {
			if (enteredDataTag) {
				try {
					if (!qName.equals(tableName)) {
						tableName = qName;
						closeQueryBatch();
						createNewQueryBatchFromColumnNames(getColumnNamesFromElementAttributes(attributes));
					}
					addQueryFromValues(getValuesFromAttributes(attributes));
				} catch (Exception e) {
					throw new SAXException(e.getMessage());
				}
			}
		}

		private String[] getValuesFromAttributes(Attributes attributes) {
			String[] values = new String[attributes.getLength()];
			for (int i = 0; i < attributes.getLength(); i++) {
				values[i] = attributes.getValue(i);
			}
			return values;
		}

		private String[] getColumnNamesFromElementAttributes(Attributes attributes) {
			String[] columnNames = new String[attributes.getLength()];
			for (int i = 0; i < attributes.getLength(); i++) {
				columnNames[i] = attributes.getQName(i);
			}
			return columnNames;
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			if (enteredConnectionSettingsTag) {
				String elementValue = new String(ch, start, length).trim();
				if (!elementValue.equals("")) {
					String setterMethodName = SAXDataFileParserHandlerReflectionUtility
							.getSetterNameFromElementName(settingName);
					invokeSetterMethodOfConnectionSettings(setterMethodName, elementValue);
				}
			}
		}

		private void invokeSetterMethodOfConnectionSettings(String setterMethodName, String value) throws SAXException {
			try {
				SAXDataFileParserHandlerReflectionUtility.invokeSetterMethodIfPresent(connectionSettings,
						setterMethodName, value);
			} catch (Exception e) {
				createNewParsingError(e.getMessage(), "Error while parsing connection settings");
				setImportReportResultFailedAndParsingError();
				throw new SAXException("failed while parsing connection settings");
			}
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("connection-settings")) {
				enteredConnectionSettingsTag = false;
				try {
					createNewConnection();
				} catch (Exception e) {
					throw new SAXException(e.getMessage());
				}
				return;
			}
			if (qName.equalsIgnoreCase("data")) {
				enteredDataTag = false;
				return;
			}
			if (qName.equalsIgnoreCase("import-request")) {
				finishParsing();
				return;
			}
		}

		public void error(SAXParseException e) throws SAXException {
			createNewParsingError(e.getMessage(), "SAX Exception");
		}
	}
}
