package com.tools.pools;

public abstract class AutoReleasableThread extends Thread {
	private final Object startMonitor = new Object();
	private boolean suspended = true;

	protected abstract void doOperation();
	protected abstract void releaseThread();

	@Override
	public void run() {
		do {
			waitUntilNotified();
			doOperation();
			suspendAndRelease();
		} while (!isInterrupted());
	}

	protected void waitUntilNotified() {
		synchronized (startMonitor) {
			if (suspended) {
				try {
					startMonitor.wait();
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	}

	protected void suspendAndRelease() {
		synchronized (startMonitor) {
			suspended = true;
		}
		releaseThread();
	}

	protected void notifyThread() {
		synchronized (startMonitor) {
			suspended = false;
			startMonitor.notify();
		}
	}
}
