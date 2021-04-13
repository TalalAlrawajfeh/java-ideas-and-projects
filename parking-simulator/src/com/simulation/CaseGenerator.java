/**
 * 
 */
package com.simulation;

import java.time.Duration;

import com.simulation.VehicleCase.Task;
import com.simulation.VehicleCase.Type;

/**
 * @author phi01tech
 *
 */
public interface CaseGenerator {

	CaseGenerator setParkingLotsCount(int count);

	CaseGenerator addVehicleCase(VehicleCase vehicleCase);

	CaseGenerator setTaskDuration(VehicleCase.Type type, VehicleCase.Task task, Duration duration);

	default CaseGenerator setTruckLoadingDuration(Duration duration) {
		return setTaskDuration(Type.TRUCK, Task.LOAD, duration);
	}

	default CaseGenerator setTruckOffLoadingDuration(Duration duration) {
		return setTaskDuration(Type.TRUCK, Task.OFFLOAD, duration);
	}

	default CaseGenerator setVanOffLoadingDuration(Duration duration) {
		return setTaskDuration(Type.VAN, Task.OFFLOAD, duration);
	}

	default CaseGenerator setVanLoadingDuration(Duration duration) {
		return setTaskDuration(Type.VAN, Task.LOAD, duration);
	}

	SimulationCase generateCase();
}
