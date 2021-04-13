package com.simulation.impl;

import java.time.Duration;

/*
 * Created by StorehouseParkingLots semaphore for running the vehicle cases' task
 * 
 */

public class VehicleTaskThread extends Thread {
	private StorehouseVehicleStatus storehouseVehicleStatus;
	private VehiclesManager vehiclesManager;
	private StorehouseParkingLots storehouseParkingLots;

	public VehicleTaskThread(StorehouseVehicleStatus storehouseVehicleStatus,
			StorehouseParkingLots storehouseParkingLots, VehiclesManager vehiclesManager) {
		this.storehouseVehicleStatus = storehouseVehicleStatus;
		this.storehouseParkingLots = storehouseParkingLots;
		this.vehiclesManager = vehiclesManager;
	}

	@Override
	public void run() {
		waitUntilTaskDone();
		storehouseParkingLots.leave(storehouseVehicleStatus, this);
	}

	private void waitUntilTaskDone() {
		Duration taskDuration = vehiclesManager.vehicleTaskDuration(storehouseVehicleStatus);
		try {
			sleep(taskDuration.getSeconds() * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
