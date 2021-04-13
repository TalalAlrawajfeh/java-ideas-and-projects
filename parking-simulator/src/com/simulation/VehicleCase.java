/**
 * 
 */
package com.simulation;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author phi01tech
 *
 */
public interface VehicleCase {

	public static final IdGenerator IDS_GENERATOR = new IdGenerator();

	public static enum Type {
		TRUCK, VAN;
	}

	public static enum Task {
		LOAD, OFFLOAD;
	}

	default String identifier() {
		return IDS_GENERATOR.getIdentifier(this);
	}

	Type type();

	Task task();

	Duration arriveAfter();

	default boolean isTruck() {
		return type().equals(Type.TRUCK);
	}

	default boolean isLoadingTask() {
		return task().equals(Task.LOAD);
	}

	public static class IdGenerator {
		private final Map<VehicleCase, String> ids = new HashMap<>();
		private final Map<Type, AtomicInteger> counters = new HashMap<>();
		{
			counters.put(Type.TRUCK, new AtomicInteger(0));
			counters.put(Type.VAN, new AtomicInteger(0));
		}

		String getIdentifier(VehicleCase vehicleCase) {
			String id = ids.get(vehicleCase);
			if (id == null) {
				id = vehicleCase.type() + "-" + counters.get(vehicleCase.type()).incrementAndGet();
				ids.put(vehicleCase, id);
			}
			return id;
		}
	}

}
