package com.simulation.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.simulation.VehicleCase;
import com.simulation.VehicleStatus;
import com.simulation.VehicleStatus.Status;

/*
 * This class contains methods for handling vehicle statuses
 * 
 */

public class VehiclesManager {
	private ArrayList<StorehouseVehicleStatus> vehicleStatuses = new ArrayList<>();
	private StorehouseSimulation storehouseSimulation;
	private StorehouseParkingLots storehouseParkingLots;

	public VehiclesManager(StorehouseSimulation storehouseSimulation) {
		this.storehouseSimulation = storehouseSimulation;
	}

	public void setStorehouseParkingLots(StorehouseParkingLots storehouseParkingLots) {
		this.storehouseParkingLots = storehouseParkingLots;
	}

	public void initializeVehicleStatuses() {
		storehouseSimulation.getSimulationCase().vehicleCasesAsStream()
				.forEach((o) -> vehicleStatuses.add(new StorehouseVehicleStatus(o)));
	}

	public Iterable<VehicleStatus> castStorehouseVehicleStatusesArray() {
		return vehicleStatuses.stream().map(o -> (VehicleStatus) o).collect(Collectors.toList());
	}

	public void checkIfVehiclesArrivedThenPark() {
		vehicleStatuses.stream().filter(this::isVehicleNotArrived).filter(this::vehicleArrived)
				.forEach(this::changeStatusToWaitingAndPark);
	}

	private boolean vehicleArrived(VehicleCase vehicleCase) {
		Duration duration = Duration.between(storehouseSimulation.getStartTime(), LocalDateTime.now());
		return duration.compareTo(vehicleCase.arriveAfter()) >= 0;
	}

	private boolean vehicleArrived(StorehouseVehicleStatus storehouseVehicleStatus) {
		return vehicleArrived(storehouseVehicleStatus.vehicle());
	}

	private void changeStatusToWaitingAndPark(StorehouseVehicleStatus storehouseVehicleStatus) {
		storehouseVehicleStatus.changeStatus(Status.WAITING);
		storehouseVehicleStatus.arrive();
		storehouseSimulation.triggerStatusChanged();
		storehouseParkingLots.signalWaitingVehiclesParker();
	}

	public void doOperationOnWaitingVehicles(Consumer<StorehouseVehicleStatus> operation) {
		vehicleStatuses.stream().filter(this::isVehicleWaiting).forEach(operation);
	}

	private boolean isVehicleWaiting(StorehouseVehicleStatus storehouseVehicleStatus) {
		return storehouseVehicleStatus.status() == Status.WAITING;
	}

	public Duration vehicleTaskDuration(VehicleCase vehicleCase) {
		return storehouseSimulation.getSimulationCase().getTaskDuration(vehicleCase.type(), vehicleCase.task());
	}

	public Duration vehicleTaskDuration(VehicleStatus vehicleStatus) {
		VehicleCase vehicleCase = vehicleStatus.vehicle();
		return vehicleTaskDuration(vehicleCase);
	}

	public boolean anyVehicleNotArrived() {
		return vehicleStatuses.stream().anyMatch(this::isVehicleNotArrived);
	}

	private boolean isVehicleNotArrived(StorehouseVehicleStatus storehouseVehicleStatus) {
		return storehouseVehicleStatus.status() == Status.NOT_ARRIVED;
	}
}
