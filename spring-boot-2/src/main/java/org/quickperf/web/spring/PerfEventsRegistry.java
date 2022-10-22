/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2021-2022 the original author or authors.
 */
package org.quickperf.web.spring;

import java.util.ArrayDeque;
import java.util.Deque;

public class PerfEventsRegistry {

	public static final PerfEventsRegistry INSTANCE = new PerfEventsRegistry();

	private static final ThreadLocal<Deque<PerfEvent>> PERF_EVENTS = ThreadLocal.withInitial(() -> new ArrayDeque<>());

	private PerfEventsRegistry() { }

	public void registerPerfEvent(PerfEvent perfEvent) {
		Deque<PerfEvent> perfEventsAsDeque = PERF_EVENTS.get();
		perfEventsAsDeque.add(perfEvent);
	}

	public void unregisterPerfEvents() {
		PERF_EVENTS.remove();
	}

	public Deque<PerfEvent> getPerfEvents() {
		return PERF_EVENTS.get();
	}

}
