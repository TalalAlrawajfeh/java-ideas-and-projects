package com.tools.pools;

public class DataFileParsersPool extends CreateOnDemandObjectPool<FileParserThread> {
	public DataFileParsersPool() {
		super();
	}

	public DataFileParsersPool(int maxNumberOfDataFileParsers) {
		super(maxNumberOfDataFileParsers);
	}

	@Override
	protected FileParserThread createNewObject() {
		FileParserThread fileParserThread = new FileParserThread(this);
		fileParserThread.start();
		return fileParserThread;
	}

	@Override
	protected FileParserThread removeAnyObjectFromPool() {
		return (FileParserThread) pool.remove(0);
	}
}
