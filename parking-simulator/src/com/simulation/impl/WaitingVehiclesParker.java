package com.simulation.impl;

/*
 * This class works on parking vehicles after they arrive and are waiting
 * 
 */

public class WaitingVehiclesParker extends Thread {
	private final Object signalMonitor = new Object();
	private StorehouseParkingLots storehouseParkingLots;
	private VehiclesManager vehiclesManager;
	private boolean doneWorkingFlag = false;
	private boolean wasSignaled = false;
	private boolean parkerWaiting = false;

	public WaitingVehiclesParker(StorehouseParkingLots storehouseParkingLots, VehiclesManager vehiclesManager) {
		this.vehiclesManager = vehiclesManager;
		this.storehouseParkingLots = storehouseParkingLots;
	}

	public boolean doneWorking() {
		return doneWorkingFlag;
	}

	public void signal() {
		synchronized (signalMonitor) {
			wasSignaled = true;
			signalMonitor.notify();
		}
	}

	public boolean isParkerWaiting() {
		return parkerWaiting;
	}

	@Override
	public void run() {
		workUntilAllVehiclesArrive();
		doneWorkingFlag = true;
		parkWaitingVehicles();
	}

	private void workUntilAllVehiclesArrive() {
		while (vehiclesManager.anyVehicleNotArrived()) {
			if (!wasSignaled)
				waitForSignal();
			wasSignaled = false;
			parkWaitingVehicles();
		}
	}

	private void waitForSignal() {
		synchronized (signalMonitor) {
			try {
				parkerWaiting = true;
				signalMonitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		parkerWaiting = false;
	}

	private void parkWaitingVehicles() {
		vehiclesManager.doOperationOnWaitingVehicles((o) -> storehouseParkingLots.park(o));
	}
}