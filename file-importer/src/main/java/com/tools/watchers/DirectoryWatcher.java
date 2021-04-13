package com.tools.watchers;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.tools.beans.DataImporterSettings;
import com.tools.pools.AutoReleasableThread;
import com.tools.pools.DataFileParsersPool;
import com.tools.pools.DirectoryWatchersPool;

public class DirectoryWatcher extends AutoReleasableThread implements Watcher<DataImporterSettings> {
	private static final long NO_TIMEOUT = 0;
	private long watcherTimeoutDurationMillis;
	private WatchService watchService;
	private DataImporterSettings dataImporterSettings = null;
	private DirectoryWatchersPool directoryWatchersPool;
	private DataFileParsersPool dataFileParsersPool = new DataFileParsersPool();

	public DirectoryWatcher(DirectoryWatchersPool directoryWatcherPool) {
		this.directoryWatchersPool = directoryWatcherPool;
		watcherTimeoutDurationMillis = NO_TIMEOUT;
	}

	public DirectoryWatcher(DirectoryWatchersPool directoryWatcherPool, long watcherTimeoutDurationMillis) {
		this.directoryWatchersPool = directoryWatcherPool;
		this.watcherTimeoutDurationMillis = watcherTimeoutDurationMillis;
	}

	@Override
	public void startWatching(DataImporterSettings dataImporterSettings) {
		this.dataImporterSettings = dataImporterSettings;
		try {
			registerWatchService(dataImporterSettings.getSourcePath());
			notifyThread();
		} catch (Exception e) {
			e.printStackTrace();
			stopWatching();
		}
	}

	@Override
	public void stopWatching() {
		suspendAndRelease();
		try {
			watchService.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doOperation() {
		try {
			checkAndParseExistingFiles();
			watchAndParseNewFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void releaseThread() {
		directoryWatchersPool.release(this);
	}

	private void registerWatchService(Path path) throws Exception {
		FileSystem fileSystem = path.getFileSystem();
		watchService = fileSystem.newWatchService();
		path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
	}

	private void checkAndParseExistingFiles() throws Exception {
		Files.list(dataImporterSettings.getSourcePath()).filter(Files::isRegularFile)
				.forEach(this::deliverFileForParsing);
		dataFileParsersPool.waitForTakenObjectsToReturn();
	}

	private void deliverFileForParsing(Path filePath) {
		if (filePath.toFile().isFile()) {
			System.out.print("File detected: ");
			System.out.println(filePath.toString());
			try {
				dataFileParsersPool.take().startParsing(filePath, dataImporterSettings);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void watchAndParseNewFiles() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		WatchKey watchKey = null;
		do {
			if (watcherTimeoutDurationMillis == NO_TIMEOUT) {
				watchKey = watchService.take();
			} else {
				watchKey = watchService.poll(watcherTimeoutDurationMillis, TimeUnit.MILLISECONDS);
			}
			parseAnyCreatedFiles(watchKey);
			watchKey.reset();
		} while (watchKey.isValid() && !timeoutDurationPassed(startTime));
	}

	private boolean timeoutDurationPassed(long startTime) {
		return (System.currentTimeMillis() - startTime) < watcherTimeoutDurationMillis
				&& watcherTimeoutDurationMillis != NO_TIMEOUT;
	}

	private void parseAnyCreatedFiles(WatchKey watchKey) {
		Set<Path> createdFilePaths = getCreatedFilePathsFromWatchKey(watchKey);
		if (createdFilePaths.size() != 0) {
			createdFilePaths.stream().forEach(this::deliverFileForParsing);
			dataFileParsersPool.waitForTakenObjectsToReturn();
		}
	}

	private Set<Path> getCreatedFilePathsFromWatchKey(WatchKey watchKey) {
		return watchKey.pollEvents().stream().filter(e -> e.kind().equals(StandardWatchEventKinds.ENTRY_CREATE))
				.map(e -> {
					@SuppressWarnings("unchecked")
					Path eventPath = ((WatchEvent<Path>) e).context();
					return dataImporterSettings.getSourcePath().resolve(eventPath.toString());
				}).collect(Collectors.toSet());
	}
}
