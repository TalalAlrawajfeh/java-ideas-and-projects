/**
 * 
 */
package com.simulation;

import java.time.LocalDateTime;

/**
 * Represents an information about a vehicle case status and history.
 * <p>
 * The vehicle {@link #status()} changes after arrival from
 * {@link Status#WAITING}, {@link Status#PARKED}, to {@link Status#LEFT},
 * {@link #statusTime(Status)} returns the time of a status when changed.
 * </p>
 * 
 * @author phi01tech
 *
 */
public interface VehicleStatus {

	public static enum Status {
		NOT_ARRIVED, WAITING, PARKED, LEFT;
	}

	/**
	 * The related vehicle case.
	 * 
	 * @return
	 */
	VehicleCase vehicle();

	/**
	 * @return the arrival time of this case
	 */
	LocalDateTime arrivalTime();

	/**
	 * Returns the time when a <code>status</code> occurred.
	 * 
	 * @param status
	 *            status to check
	 * @return returns the change time or <code>null</code> if it didn't reach
	 *         this status
	 */
	LocalDateTime statusTime(Status status);

	/**
	 * The current status of this vehicle case.
	 * 
	 * @return
	 */
	Status status();

	default boolean isParked() {
		return isInStatus(Status.PARKED);
	}

	default boolean isWaiting() {
		return isInStatus(Status.WAITING);
	}

	default boolean isLeft() {
		return isInStatus(Status.LEFT);
	}

	default boolean isInStatus(Status status) {
		return status().equals(status);
	}

}
