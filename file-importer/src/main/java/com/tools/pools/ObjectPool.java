package com.tools.pools;

public interface ObjectPool<T> {
	int getMaxPoolSize();

	T take() throws Exception;

	void release(T object);
}
