package com.tools.parsers;

import java.io.StringWriter;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.tools.beans.DataImportReport;
import com.tools.beans.ParsingError;

public class DataImportReportGenerator {
	private static final String INDENTATION = "    ";
	private static final String NEW_LINE = "\r\n";
	private static final String _1_0 = "1.0";
	private static final String UTF_8 = "UTF-8";

	private static final String IMPORT_REPORT_TAG = "import-report";

	private static final String XSI_SCHEMA_LOCATION = "xsi:schemaLocation";
	private static final String SCHEMA_LOCATION = "http://www.phi01tech.com/tools/data-import ./data-import.xsd";
	private static final String XMLNS_XSI = "xmlns:xsi";
	private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String XMLNS = "xmlns";
	private static final String NAME_SPACE = "http://www.phi01tech.com/tools/data-import";

	private static final String RESULT_TAG = "result";
	private static final String START_TIME_TAG = "start-time";
	private static final String END_TIME_TAG = "end-time";

	private static final String ERRORS_TAG = "errors";
	private static final String ERROR_MESSAGE_TAG = "error-message";
	private static final String DATA_RECORD_TAG = "data-record";

	public String generateReport(DataImportReport dataImportReport) {
		String xmlString = "";
		try {
			StringWriter stringWriter = new StringWriter();
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

			xmlStreamWriter.writeStartDocument(UTF_8, _1_0);
			writeNewLineAndIndentations(xmlStreamWriter, 0);

			xmlStreamWriter.writeStartElement(IMPORT_REPORT_TAG);
			xmlStreamWriter.writeAttribute(XMLNS, NAME_SPACE);
			xmlStreamWriter.writeAttribute(XMLNS_XSI, XML_SCHEMA);
			xmlStreamWriter.writeAttribute(XSI_SCHEMA_LOCATION, SCHEMA_LOCATION);

			writeResult(dataImportReport, xmlStreamWriter);
			writeStartTime(dataImportReport, xmlStreamWriter);
			writeEndTime(dataImportReport, xmlStreamWriter);
			writeErrorMessageIfPresent(dataImportReport, xmlStreamWriter);
			writeParsingErrorsIfPresent(dataImportReport, xmlStreamWriter);

			writeNewLineAndIndentations(xmlStreamWriter, 0);
			xmlStreamWriter.writeEndElement();

			xmlStreamWriter.writeEndDocument();

			xmlStreamWriter.flush();
			xmlStreamWriter.close();

			xmlString = stringWriter.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlString;
	}

	private void writeParsingErrorsIfPresent(DataImportReport dataImportReport, XMLStreamWriter xmlStreamWriter)
			throws XMLStreamException {

		List<ParsingError> parsingErrors = dataImportReport.getParsingErrors();

		if (parsingErrors == null) {
			return;
		}

		if (parsingErrors.size() == 0) {
			return;
		}

		writeNewLineAndIndentations(xmlStreamWriter, 1);
		xmlStreamWriter.writeStartElement(ERRORS_TAG);
		for (ParsingError parsingError : parsingErrors) {
			writeNewLineAndIndentations(xmlStreamWriter, 2);
			xmlStreamWriter.writeStartElement(parsingError.getErrorType());

			writeParsingErrorMessage(xmlStreamWriter, parsingError);
			writeParsingErrorInformationIfPresent(xmlStreamWriter, parsingError);

			writeNewLineAndIndentations(xmlStreamWriter, 2);
			xmlStreamWriter.writeEndElement();
		}

		writeNewLineAndIndentations(xmlStreamWriter, 1);
		xmlStreamWriter.writeEndElement();
	}

	private void writeParsingErrorInformationIfPresent(XMLStreamWriter xmlStreamWriter, ParsingError parsingError)
			throws XMLStreamException {

		String optionalInformationAboutError = parsingError.getOptionalInformationAboutError();

		if (optionalInformationAboutError == null) {
			return;
		}

		if (optionalInformationAboutError.equals("")) {
			return;
		}

		writeNewLineAndIndentations(xmlStreamWriter, 3);
		xmlStreamWriter.writeStartElement(DATA_RECORD_TAG);
		xmlStreamWriter.writeCData(optionalInformationAboutError);
		xmlStreamWriter.writeEndElement();
	}

	private void writeParsingErrorMessage(XMLStreamWriter xmlStreamWriter, ParsingError parsingError)
			throws XMLStreamException {
		writeNewLineAndIndentations(xmlStreamWriter, 3);
		xmlStreamWriter.writeStartElement(ERROR_MESSAGE_TAG);
		xmlStreamWriter.writeCharacters(parsingError.getErrorMessage());
		xmlStreamWriter.writeEndElement();
	}

	private void writeErrorMessageIfPresent(DataImportReport dataImportReport, XMLStreamWriter xmlStreamWriter)
			throws XMLStreamException {
		String errorMessage = dataImportReport.getErrorMessage();
		if (errorMessage == null) {
			return;
		}

		writeNewLineAndIndentations(xmlStreamWriter, 1);
		xmlStreamWriter.writeStartElement(ERROR_MESSAGE_TAG);
		xmlStreamWriter.writeCharacters(errorMessage);
		xmlStreamWriter.writeEndElement();
	}

	private void writeEndTime(DataImportReport dataImportReport, XMLStreamWriter xmlStreamWriter)
			throws XMLStreamException {

		writeNewLineAndIndentations(xmlStreamWriter, 1);
		xmlStreamWriter.writeStartElement(END_TIME_TAG);
		xmlStreamWriter.writeCharacters(dataImportReport.getEndTime().toString());
		xmlStreamWriter.writeEndElement();
	}

	private void writeStartTime(DataImportReport dataImportReport, XMLStreamWriter xmlStreamWriter)
			throws XMLStreamException {

		writeNewLineAndIndentations(xmlStreamWriter, 1);
		xmlStreamWriter.writeStartElement(START_TIME_TAG);
		xmlStreamWriter.writeCharacters(dataImportReport.getStartTime().toString());
		xmlStreamWriter.writeEndElement();
	}

	private void writeResult(DataImportReport dataImportReport, XMLStreamWriter xmlStreamWriter)
			throws XMLStreamException {
		writeNewLineAndIndentations(xmlStreamWriter, 1);
		xmlStreamWriter.writeStartElement(RESULT_TAG);
		xmlStreamWriter.writeCharacters(dataImportReport.getResult());
		xmlStreamWriter.writeEndElement();
	}

	private void writeNewLineAndIndentations(XMLStreamWriter xmlStreamWriter, int numberOfIndentations)
			throws XMLStreamException {
		xmlStreamWriter.writeCharacters(NEW_LINE);
		for (int i = 0; i < numberOfIndentations; i++) {
			xmlStreamWriter.writeCharacters(INDENTATION);
		}
	}
}
