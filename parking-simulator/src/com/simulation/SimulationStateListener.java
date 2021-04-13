/**
 * 
 */
package com.simulation;

/**
 * Defines an object as a listener to {@link Simulation} state changes
 * 
 * @author Sami
 *
 */
public interface SimulationStateListener {

	/**
	 * Called only once when the simulation is started.
	 * 
	 * @param state
	 *            the current state of the simulation.
	 */
	public void simulationStarted(SimulationState state);

	/**
	 * Called by the simulation to notify the listener that a change happened in
	 * the simulation.
	 * 
	 * @param state
	 */
	public void statusChanged(SimulationState state);
}
