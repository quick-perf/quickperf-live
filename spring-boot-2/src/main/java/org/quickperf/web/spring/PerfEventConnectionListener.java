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

import org.quickperf.sql.connection.ConnectionListener;

import java.sql.Connection;

class PerfEventConnectionListener extends ConnectionListener {

	static final PerfEventConnectionListener INSTANCE = new PerfEventConnectionListener();

	private PerfEventConnectionListener() { }

	@Override
	public void theDatasourceGetsTheConnection(Connection connection) {
		PerfEventsRegistry.INSTANCE.registerPerfEvent(PerfEvent.GET_DB_CONNECTION);
	}

	@Override
	public void commit(Connection connection) {
		PerfEventsRegistry.INSTANCE.registerPerfEvent(PerfEvent.DB_COMMIT);
	}

	@Override
	public void close(Connection connection) {
		PerfEventsRegistry.INSTANCE.registerPerfEvent(PerfEvent.CLOSE_DB_CONNECTION);
	}

}
