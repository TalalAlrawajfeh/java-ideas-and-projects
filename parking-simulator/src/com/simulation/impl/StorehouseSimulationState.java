package com.simulation.impl;

import java.time.LocalDateTime;

import com.simulation.SimulationCase;
import com.simulation.SimulationState;
import com.simulation.VehicleStatus;

public class StorehouseSimulationState implements SimulationState {
	private boolean running = true;
	private LocalDateTime completeTime = null;
	private StorehouseSimulation storehouseSimulation;
	private VehiclesManager vehiclesManager;

	public StorehouseSimulationState(StorehouseSimulation storehouseSimulation, VehiclesManager vehiclesManager) {
		this.storehouseSimulation = storehouseSimulation;
		this.vehiclesManager = vehiclesManager;
	}

	@Override
	public SimulationCase getCase() {
		return storehouseSimulation.getSimulationCase();
	}

	@Override
	public int occupiedParkinglots() {
		return storehouseSimulation.getOccupiedLots();
	}

	@Override
	public LocalDateTime startTime() {
		return storehouseSimulation.getStartTime();
	}

	@Override
	public LocalDateTime completeTime() {
		return completeTime;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public void stopRunning() {
		running = false;
		completeTime = LocalDateTime.now();
	}

	@Override
	public Iterable<VehicleStatus> getVehicleStatuses() {
		return vehiclesManager.castStorehouseVehicleStatusesArray();
	}
}
