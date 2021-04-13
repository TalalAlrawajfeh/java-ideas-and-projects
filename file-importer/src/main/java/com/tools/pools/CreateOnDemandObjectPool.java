package com.tools.pools;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateOnDemandObjectPool<T> implements ObjectPool<T> {
	private static final int DEFAULT_MAX_POOL_SIZE = 4;
	protected final Object poolMonitor = new Object();

	protected int maxPoolSize;
	protected List<T> pool = new ArrayList<>();
	protected List<T> taken = new ArrayList<>();
	protected int numberOfObjectsTaken = 0;

	public CreateOnDemandObjectPool() {
		maxPoolSize = DEFAULT_MAX_POOL_SIZE;
	}

	public CreateOnDemandObjectPool(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	protected abstract T createNewObject() throws Exception;

	protected abstract T removeAnyObjectFromPool() throws Exception;

	@Override
	public T take() throws Exception {
		synchronized (poolMonitor) {
			while (numberOfObjectsTaken == maxPoolSize) {
				try {
					poolMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			T t = null;
			if (pool.size() == 0 && numberOfObjectsTaken < maxPoolSize) {
				t = createNewObject();
			} else {
				t = removeAnyObjectFromPool();
			}

			taken.add(t);
			numberOfObjectsTaken++;
			return t;
		}
	}

	public void release(T t) {
		synchronized (poolMonitor) {
			pool.add(t);
			taken.remove(t);
			numberOfObjectsTaken--;
			poolMonitor.notifyAll();
		}
	}

	@Override
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void waitForTakenObjectsToReturn() {
		synchronized (poolMonitor) {
			while (numberOfObjectsTaken > 0) {
				try {
					poolMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
