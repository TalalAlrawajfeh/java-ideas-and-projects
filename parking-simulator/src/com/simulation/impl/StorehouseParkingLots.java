package com.simulation.impl;

import java.util.ArrayList;

import com.simulation.VehicleStatus.Status;

/*
 * This is a simaphore for creating a thread for each vehicle case task depending on parkingLotsCount()
 * 
 */

public class StorehouseParkingLots {
	private final Object occupiedLotsMonitor = new Object();
	private final Object parkMonitor = new Object();
	private final Object vehicleTaskThreadsMonitor = new Object();

	private int occupiedParkingLots = 0;
	private StorehouseSimulation storehouseSimulation;
	private VehiclesManager vehiclesManager;
	private WaitingVehiclesParker waitingVehiclesParker;
	private ArrayList<VehicleTaskThread> vehicleTaskThreads = new ArrayList<>();

	public StorehouseParkingLots(StorehouseSimulation storehouseSimulation, VehiclesManager vehiclesManager) {
		this.vehiclesManager = vehiclesManager;
		this.storehouseSimulation = storehouseSimulation;
		waitingVehiclesParker = new WaitingVehiclesParker(this, vehiclesManager);
		waitingVehiclesParker.start();
	}

	public void signalWaitingVehiclesParker() {
		waitingVehiclesParker.signal();
	}

	public int occupiedLots() {
		return occupiedParkingLots;
	}

	public void waitUntilVehicleTaskThreadsDone() {
		while (!vehicleTaskThreads.isEmpty()) {
			try {
				vehicleTaskThreads.get(0).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitUntilParkerThreadDone() {
		waitUntilParkerDoneWorking();

		try {
			waitingVehiclesParker.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void waitUntilParkerDoneWorking() {
		while (!waitingVehiclesParker.doneWorking()) {
			if (waitingVehiclesParker.isParkerWaiting()) {
				waitingVehiclesParker.signal();
				break;
			}
			storehouseSimulation.sleepOneTenthOfASecond();
		}
	}

	public void park(StorehouseVehicleStatus storehouseVehicleStatus) {
		waitUntilCanPark();
		parkVehicle(storehouseVehicleStatus);
		createNewVehicleTaskThread(storehouseVehicleStatus);
	}

	private void waitUntilCanPark() {
		synchronized (parkMonitor) {
			if (!canPark()) {
				try {
					parkMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean canPark() {
		synchronized (occupiedLotsMonitor) {
			return occupiedParkingLots != storehouseSimulation.getSimulationCase().parkingLotsCount();
		}
	}

	private void createNewVehicleTaskThread(StorehouseVehicleStatus storehouseVehicleStatus) {
		VehicleTaskThread vehicleTaskThread = new VehicleTaskThread(storehouseVehicleStatus, this, vehiclesManager);

		synchronized (vehicleTaskThreadsMonitor) {
			vehicleTaskThreads.add(vehicleTaskThread);
		}

		vehicleTaskThread.start();
	}

	private void parkVehicle(StorehouseVehicleStatus storehouseVehicleStatus) {
		storehouseVehicleStatus.changeStatus(Status.PARKED);

		synchronized (occupiedLotsMonitor) {
			occupiedParkingLots++;
		}

		storehouseSimulation.triggerStatusChanged();
	}

	public void leave(StorehouseVehicleStatus storehouseVehicleStatus, VehicleTaskThread vehicleTaskThread) {
		leaveVehicle(storehouseVehicleStatus);

		synchronized (vehicleTaskThreadsMonitor) {
			vehicleTaskThreads.remove(vehicleTaskThread);
		}
	}

	private void leaveVehicle(StorehouseVehicleStatus storehouseVehicleStatus) {
		storehouseVehicleStatus.changeStatus(Status.LEFT);

		synchronized (parkMonitor) {
			synchronized (occupiedLotsMonitor) {
				occupiedParkingLots--;
			}
			parkMonitor.notify();
		}

		storehouseSimulation.triggerStatusChanged();
	}
}
