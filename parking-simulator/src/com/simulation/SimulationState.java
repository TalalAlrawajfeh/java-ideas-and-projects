/**
 * 
 */
package com.simulation;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Defines a representation for a simulation status.
 * 
 * @author Sami
 *
 */
public interface SimulationState {

	/**
	 * 
	 * @return the simulation related case
	 */
	SimulationCase getCase();

	/**
	 * Number of parking lots currently occupied
	 * 
	 * @return
	 */
	int occupiedParkinglots();

	/**
	 * The start time of the simulation.
	 * 
	 * @return the start time of the simulation
	 */
	LocalDateTime startTime();

	/**
	 * Complete time of the simulation.
	 * 
	 * @return simulation completion time or <code>null</code> if it is still
	 *         running.
	 */
	LocalDateTime completeTime();

	/**
	 * Returns if the simulation is still running, a simulation is considered
	 * running if it stills has vehicle cases not served.
	 * 
	 * @return <code>true</code> if the simulation is still running.
	 */
	boolean isRunning();

	/**
	 * Returns a representation for vehicle statuses.
	 * 
	 * @return
	 */
	Iterable<VehicleStatus> getVehicleStatuses();

	default Stream<VehicleStatus> getVehicleStatusesAsStream() {
		Iterable<VehicleStatus> iterable = getVehicleStatuses();
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	default boolean hasEmptyLots() {
		return getCase().parkingLotsCount() != occupiedParkinglots();
	}

}
