package com.tools.watchers;

public interface Watcher<T> {
	void startWatching(T t);

	void stopWatching();
}
