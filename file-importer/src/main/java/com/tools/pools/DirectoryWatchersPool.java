package com.tools.pools;

import com.tools.watchers.DirectoryWatcher;

public class DirectoryWatchersPool extends CreateOnDemandObjectPool<DirectoryWatcher> {
	private static final int NO_TIMEOUT = 0;
	private long directoryWatchersTimeoutDuration = NO_TIMEOUT;

	public DirectoryWatchersPool() {
		super();
	}

	public DirectoryWatchersPool(int maxNumberOfDirectoryWatchers) {
		super(maxNumberOfDirectoryWatchers);
	}

	public DirectoryWatchersPool(long directoryWatchersTimeoutLength) {
		super();
		this.directoryWatchersTimeoutDuration = directoryWatchersTimeoutLength;
	}

	public DirectoryWatchersPool(int maxNumberOfDirectoryWatchers, long directoryWatchersTimeoutDuration) {
		super(maxNumberOfDirectoryWatchers);
		this.directoryWatchersTimeoutDuration = directoryWatchersTimeoutDuration;
	}

	@Override
	protected DirectoryWatcher createNewObject() {
		DirectoryWatcher directoryWatcher;
		if (directoryWatchersTimeoutDuration == NO_TIMEOUT) {
			directoryWatcher = new DirectoryWatcher(this);
		} else {
			directoryWatcher = new DirectoryWatcher(this, directoryWatchersTimeoutDuration);
		}
		directoryWatcher.start();
		return directoryWatcher;
	}

	@Override
	protected DirectoryWatcher removeAnyObjectFromPool() {
		return (DirectoryWatcher) pool.remove(0);
	}
}
