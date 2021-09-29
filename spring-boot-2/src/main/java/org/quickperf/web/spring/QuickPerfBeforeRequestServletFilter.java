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
 * Copyright 2021-2021 the original author or authors.
 */
package org.quickperf.web.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickperf.sql.SqlRecorderRegistry;
import org.quickperf.sql.connection.ConnectionEventsProfiler;
import org.quickperf.sql.connection.ConnectionListenerRegistry;
import org.quickperf.sql.connection.Level;
import org.quickperf.sql.connection.ProfilingParameters;
import org.quickperf.sql.connection.stack.*;
import org.quickperf.web.spring.config.DatabaseConfig;
import org.quickperf.web.spring.config.DatabaseHttpConfig;
import org.quickperf.web.spring.config.JvmConfig;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.quickperf.web.spring.jvm.ByteWatcherSingleThread;
import org.quickperf.web.spring.jvm.ByteWatcherSingleThreadRegistry;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import static java.util.Arrays.asList;

@Component
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class QuickPerfBeforeRequestServletFilter implements Filter {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final DatabaseConfig databaseConfig;

	private final DatabaseHttpConfig databaseHttpConfig;

	private final JvmConfig jvmConfig;

	private final TestGenerationConfig testGenerationConfig;

	public QuickPerfBeforeRequestServletFilter( DatabaseConfig databaseConfig
											  , DatabaseHttpConfig databaseHttpConfig
											  , JvmConfig jvmConfig
			                                  , TestGenerationConfig testGenerationConfig) {
		this.databaseConfig = databaseConfig;
		this.databaseHttpConfig = databaseHttpConfig;
		this.jvmConfig = jvmConfig;
		this.testGenerationConfig = testGenerationConfig;
		logger.debug(this.getClass().getSimpleName() + "is created");
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		if (!httpServletResponse.isCommitted()) {
			try {
				quickPerfProcessing();
			} catch (Exception e) {
				logger.warn("Unexpected QuickPerf issue", e);
			}
		}

		Throwable problem = null;
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Throwable t) {
			problem = t;
		}

		handleProblem(problem);
	}

	@Override
	public void destroy() {

	}

	private void handleProblem(Throwable problem) throws ServletException, IOException {
		if (problem != null) {
			if (problem instanceof ServletException) {
				throw (ServletException) problem;
			}
			if (problem instanceof IOException) {
				throw (IOException) problem;
			}
		}
	}

	static class DiagnosticConnectionProfiler extends ConnectionEventsProfiler {

		private final StringWriter stringWriter;

		public DiagnosticConnectionProfiler(ProfilingParameters profilingParameters, StringWriter stringWriter) {
			super(profilingParameters);
			this.stringWriter = stringWriter;
		}

		static DiagnosticConnectionProfiler build() {
			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
			Collection<StackTraceFilter> stackTraceFilters = asList( QuickPerfStackTraceTraceFilter.INSTANCE
					                                               , QuickPerfSpringStackTraceTraceFilter.INSTANCE);
			StackTraceDisplayConfig stackTraceDisplay = StackTraceDisplayConfig.of(StackTrace.StackDepth.ALL, stackTraceFilters);
			ProfilingParameters profilingParameters = new ProfilingParameters(Level.INFO , stackTraceDisplay, writer);
			return new DiagnosticConnectionProfiler(profilingParameters, stringWriter);
		}

		String getProfilingResult() {
			return stringWriter.toString();
		}

	}

	private void quickPerfProcessing() {

		if(databaseConfig.isSqlDisplayed() || databaseConfig.isNPlusOneSelectDetected()) {
			SqlRecorderRegistry.INSTANCE.register(new SqlExecutionsRecorder());
		}

		if(testGenerationConfig.isTestGenerationEnabled()) {
			SqlRecorderRegistry.INSTANCE.register(new SelectListener());
		}

		if(databaseConfig.isSelectedColumnsDisplayed()) {
			SelectStatsListener selectStatsListener = new SelectStatsListener();
			SqlRecorderRegistry.INSTANCE.register(selectStatsListener);
		}

		if(databaseConfig.isSqlExecutionTimeDetected()) {
			int sqlExecutionThresholdInMilliseconds = databaseConfig.getSqlExecutionTimeThresholdInMilliseconds();
			LongDbRequestsListener longDbRequestsListener = new LongDbRequestsListener(sqlExecutionThresholdInMilliseconds);
			SqlRecorderRegistry.INSTANCE.register(longDbRequestsListener);
		}

		if(databaseConfig.isDatabaseConnectionProfiled()) {
			DiagnosticConnectionProfiler diagnosticConnectionProfiler
					= DiagnosticConnectionProfiler.build();
			ConnectionListenerRegistry.INSTANCE.register(diagnosticConnectionProfiler);
			diagnosticConnectionProfiler.start();
		}

		if(databaseHttpConfig.isSynchronousHttpCallBetweenDbConnectionGottenAndClosedDetected()) {
			PerfEventConnectionListener perfEventConnectionListener = PerfEventConnectionListener.INSTANCE;
			ConnectionListenerRegistry.INSTANCE.register(perfEventConnectionListener);
		}

		if(jvmConfig.isHeapAllocationMeasured() || jvmConfig.isHeapAllocationThresholdDetected()) {
			ByteWatcherSingleThread byteWatcherSingleThread = new ByteWatcherSingleThread();
			byteWatcherSingleThread.reset();
			ByteWatcherSingleThreadRegistry.INSTANCE.register(byteWatcherSingleThread);
		}

	}

}
