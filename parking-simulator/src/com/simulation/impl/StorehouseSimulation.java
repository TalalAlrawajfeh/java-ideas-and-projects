package com.simulation.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.simulation.Simulation;
import com.simulation.SimulationCase;
import com.simulation.SimulationState;
import com.simulation.SimulationStateListener;

public class StorehouseSimulation implements Simulation {
	private SimulationCase simulationCase;
	private LocalDateTime startTime;
	private StorehouseSimulationState storehouseSimulationState;
	private VehiclesManager vehiclesManager;
	private StorehouseParkingLots storehouseParkingLots;
	private Object listenersMonitor = new Object();
	private ArrayList<SimulationStateListener> listeners = new ArrayList<>();

	@Override
	public void start(SimulationCase simCase) {
		this.simulationCase = simCase;
		startTime = LocalDateTime.now();
		initializeObjects();
		initializeSimulationAndStart();
	}

	private void initializeObjects() {
		vehiclesManager = new VehiclesManager(this);
		storehouseParkingLots = new StorehouseParkingLots(this, vehiclesManager);
		storehouseSimulationState = new StorehouseSimulationState(this, vehiclesManager);
		vehiclesManager.setStorehouseParkingLots(storehouseParkingLots);
	}

	private void initializeSimulationAndStart() {
		vehiclesManager.initializeVehicleStatuses();
		Thread simulationThread = new Thread(new SimulationThread());
		simulationThread.start();
	}

	@Override
	public SimulationState getCurrentState() {
		return storehouseSimulationState;
	}

	@Override
	public void addStateListener(SimulationStateListener listener) {
		listeners.add(listener);
	}

	public void triggerSimulationStarted() {
		listeners.stream().forEach(o -> o.simulationStarted(storehouseSimulationState));
	}

	public int getOccupiedLots() {
		return storehouseParkingLots.occupiedLots();
	}

	public void triggerStatusChanged() {
		synchronized (listenersMonitor) {
			listeners.stream().forEach(o -> o.statusChanged(storehouseSimulationState));
		}
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public SimulationCase getSimulationCase() {
		return simulationCase;
	}

	public void sleepOneTenthOfASecond() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class SimulationThread implements Runnable {
		@Override
		public void run() {
			triggerSimulationStarted();
			waitUntilAllVehiclesArrive();
			waitUntilAllVehiclesLeft();
			simulationStopRunning();
		}

		private void waitUntilAllVehiclesArrive() {
			while (vehiclesManager.anyVehicleNotArrived()) {
				vehiclesManager.checkIfVehiclesArrivedThenPark();
				sleepOneTenthOfASecond();
			}
		}

		private void waitUntilAllVehiclesLeft() {
			storehouseParkingLots.waitUntilParkerThreadDone();
			storehouseParkingLots.waitUntilVehicleTaskThreadsDone();
		}

		private void simulationStopRunning() {
			storehouseSimulationState.stopRunning();
			triggerStatusChanged();
		}
	}
}
