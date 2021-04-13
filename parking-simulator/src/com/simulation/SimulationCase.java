package com.simulation;

import java.time.Duration;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a simulation case to execute.
 * 
 * @author phi01tech
 *
 */
public interface SimulationCase {

	/**
	 * Returns the number of parking lots for this case.
	 * 
	 * @return
	 */
	int parkingLotsCount();
	
	/**
	 * Returns the duration (time needed) for a vehicle of type
	 * <code>vehicleType</code> to perform <code>vehicleTask</code>.
	 * 
	 * @param vehicleType
	 *            the type of vehicle.
	 * @param vehicleTask
	 *            the task of vehicle.
	 * @return the duration needed
	 */
	Duration getTaskDuration(VehicleCase.Type vehicleType, VehicleCase.Task vehicleTask);

	/**
	 * A list for vehicle cases.
	 * 
	 * @return
	 */
	Iterable<VehicleCase> vehicleCases();
	
	/**
	 * A stream for vehicle cases.
	 * 
	 * @return
	 */
	default Stream<VehicleCase> vehicleCasesAsStream() {
		Iterable<VehicleCase> cases = vehicleCases();
		return StreamSupport.stream(cases.spliterator(), false);
	}

	/**
	 * The 
	 * @return
	 */
	default Duration getTruckLoadDuration() {
		return getTaskDuration(VehicleCase.Type.TRUCK, VehicleCase.Task.LOAD);
	}

	default Duration getVanLoadDuration() {
		return getTaskDuration(VehicleCase.Type.VAN, VehicleCase.Task.LOAD);
	}

	default Duration getTruckOffloadDuration() {
		return getTaskDuration(VehicleCase.Type.VAN, VehicleCase.Task.OFFLOAD);
	}

	default Duration getVanOffloadDuration() {
		return getTaskDuration(VehicleCase.Type.VAN, VehicleCase.Task.OFFLOAD);
	}
}
