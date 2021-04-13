/**
 * 
 */
package com.simulation;

/**
 * Defines a simulation execution strategy. The assumption is to create a new
 * instance for each simulation run.
 * 
 * @author Sami
 *
 */
public interface Simulation {

	/**
	 * Pass the simulation the case to test, <code>simCase</code> contains all
	 * information needed to run the simulation, the tasks duration for each
	 * vehicle type and the vehicle tasks and arrival time.
	 * 
	 * @param simCase
	 *            the case to test and run
	 */
	public void start(SimulationCase simCase);

	/**
	 * Gets a snapshot for the simulation execution started through
	 * {@link #start(SimulationCase)} and where it is standing, returned object
	 * shall tells how many parking lots are occupied and the arrival time for
	 * vehicles and the status for each vehicle in the simulation.
	 * 
	 * @return a snapshot from the current execution status.
	 */
	public SimulationState getCurrentState();

	/**
	 * Add a listener to this simulation to get notification for any update
	 * happening inside the simulation, listener methods receive instances of
	 * {@link SimulationState}
	 * 
	 * @param listener a listener to simulation execution
	 */
	public void addStateListener(SimulationStateListener listener);

}
