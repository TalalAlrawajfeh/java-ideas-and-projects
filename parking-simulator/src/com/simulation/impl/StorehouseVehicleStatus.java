package com.simulation.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.simulation.VehicleCase;
import com.simulation.VehicleStatus;

public class StorehouseVehicleStatus implements VehicleStatus {
	private VehicleCase vehicleCase;
	private Map<Status, LocalDateTime> statusTimeMap;
	LocalDateTime arrivalTime = null;
	private Status status;

	public StorehouseVehicleStatus(VehicleCase vehicleCase) {
		this.vehicleCase = vehicleCase;

		status = Status.NOT_ARRIVED;

		statusTimeMap = new HashMap<>();
		statusTimeMap.put(Status.NOT_ARRIVED, LocalDateTime.now());
		statusTimeMap.put(Status.WAITING, null);
		statusTimeMap.put(Status.PARKED, null);
		statusTimeMap.put(Status.LEFT, null);
	}

	@Override
	public LocalDateTime arrivalTime() {
		return arrivalTime;
	}

	@Override
	public LocalDateTime statusTime(Status status) {
		return statusTimeMap.get(status);
	}

	@Override
	public Status status() {
		return status;
	}

	public void arrive() {
		arrivalTime = LocalDateTime.now();
	}

	public void changeStatus(Status status) {
		this.status = status;
		statusTimeMap.put(status, LocalDateTime.now());
	}

	@Override
	public VehicleCase vehicle() {
		return vehicleCase;
	}
}
