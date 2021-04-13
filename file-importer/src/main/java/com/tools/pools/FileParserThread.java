package com.tools.pools;

import java.nio.file.Path;

import com.tools.beans.DataImporterSettings;
import com.tools.parsers.CSVDataFileParser;
import com.tools.parsers.SAXDataFileParser;

public class FileParserThread extends AutoReleasableThread {
	private Path filePath;
	private DataImporterSettings dataImporterSettings;
	private SAXDataFileParser saxDataFileParser;
	private CSVDataFileParser csvDataFileParser;
	private DataFileParsersPool dataFileParsersPool;

	public FileParserThread(DataFileParsersPool dataFileParsersPool) {
		this.dataFileParsersPool = dataFileParsersPool;
	}

	public void startParsing(Path filePath, DataImporterSettings dataImporterSettings) {
		this.filePath = filePath;
		this.dataImporterSettings = dataImporterSettings;

		notifyThread();
	}

	private void parseFileWithProperParser() {
		String path = filePath.toString().toLowerCase();
		if (path.endsWith("xml")) {
			saxDataFileParser = new SAXDataFileParser(dataImporterSettings);
			saxDataFileParser.parseFile(filePath);
		} else if (path.endsWith("csv")) {
			csvDataFileParser = new CSVDataFileParser(dataImporterSettings);
			csvDataFileParser.parseFile(filePath);
		}
	}

	@Override
	protected void doOperation() {
		parseFileWithProperParser();
	}

	@Override
	protected void releaseThread() {
		dataFileParsersPool.release(this);
	}
}