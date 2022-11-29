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
package org.quickperf.web.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(
	objectName="QuickPerf:category=Database",
	description="QuickPerf Live MBeans")
public class DatabaseConfig {

	@Value("${quickperf.database.n+1.detected:false}" )
	boolean nPlusOneSelectDetected;

	@Value("${quickperf.database.n+1.threshold:3}" )
	int nPlusOneSelectDetectionThreshold;

	@Value("${quickperf.database.connection.profiled:false}")
	boolean databaseConnectionProfiled;

	@Value("${quickperf.database.sql.displayed:false}")
	boolean sqlDisplayed;

	@Value("${quickperf.database.sql.displayed.selected-columns:false}")
	boolean selectedColumnsDisplayed;

	@Value("${quickperf.database.sql.execution-time.detected:false}")
	private boolean sqlExecutionTimeDetected;

	@Value("${quickperf.database.sql.execution-time.thresholdInMs:0}")
	private int sqlExecutionTimeThresholdInMilliseconds;

	@Value("${quickperf.database.sql.execution.detected:false}")
	private boolean sqlExecutionDetected;

	@Value("${quickperf.database.sql.execution.threshold:10}")
	private int sqlExecutionThreshold;

	@Value("${quickperf.database.sql.without-bind-param.detected:false}")
	private boolean sqlWithoutBindParamDetected;

	@ManagedAttribute
	public boolean isNPlusOneSelectDetected() {
		return nPlusOneSelectDetected;
	}

	@ManagedAttribute
	public int getNPlusOneSelectDetectionThreshold() {
		return nPlusOneSelectDetectionThreshold;
	}

	@ManagedOperation
	public void setNPlusOneSelectDetectionThreshold(short nPlusOneSelectDetectionThreshold) {
		this.nPlusOneSelectDetectionThreshold = nPlusOneSelectDetectionThreshold;
	}

	@ManagedAttribute
	public boolean isSqlExecutionTimeDetected() {
		return sqlExecutionTimeDetected;
	}

	@ManagedOperation
	public void setSqlExecutionTimeDetected(boolean sqlExecutionTimeDetected) {
		this.sqlExecutionTimeDetected = sqlExecutionTimeDetected;
	}

	@ManagedAttribute
	public int getSqlExecutionTimeThresholdInMilliseconds() {
		return sqlExecutionTimeThresholdInMilliseconds;
	}

	@ManagedOperation
	public void setSqlExecutionTimeThresholdInMilliseconds(int sqlExecutionTimeThresholdInMilliseconds) {
		this.sqlExecutionTimeThresholdInMilliseconds = sqlExecutionTimeThresholdInMilliseconds;
	}

	@ManagedAttribute
	public boolean isDatabaseConnectionProfiled() {
		return databaseConnectionProfiled;
	}

	@ManagedAttribute
	public boolean isSqlDisplayed() {
		return sqlDisplayed;
	}

	@ManagedAttribute
	public boolean isSelectedColumnsDisplayed() {
		return selectedColumnsDisplayed;
	}

	@ManagedOperation
	public void setNPlusOneSelectDetected(boolean nPlusOneSelectDetected) {
		this.nPlusOneSelectDetected = nPlusOneSelectDetected;
	}

	@ManagedOperation
	public void setDatabaseConnectionProfiled(boolean databaseConnectionProfiled) {
		this.databaseConnectionProfiled = databaseConnectionProfiled;
	}

	@ManagedOperation
	public void setSelectedColumnsDisplayed(boolean selectedColumnsDisplayed) {
		this.selectedColumnsDisplayed = selectedColumnsDisplayed;
	}

	@ManagedOperation
	public void setSqlDisplayed(boolean sqlDisplayed) {
		this.sqlDisplayed = sqlDisplayed;
	}

	@ManagedAttribute
	public boolean isSqlExecutionDetected() {
		return this.sqlExecutionDetected;
	}
	@ManagedOperation
	public void setSqlExecutionDetected(boolean sqlExecutionDetected){
		this.sqlExecutionDetected = sqlExecutionDetected;
	}
	@ManagedAttribute
	public int getSqlExecutionThreshold(){
		return this.sqlExecutionThreshold;
	}
	@ManagedOperation
	public void setSqlExecutionThreshold(int threshold){
		this.sqlExecutionThreshold = threshold;
	}

	@ManagedAttribute
	public boolean isSqlWithoutBindParamDetected() {
		return sqlWithoutBindParamDetected;
	}

	@ManagedOperation
	public void setSqlWithoutBindParamDetected(boolean sqlWithoutBindParamDetected) {
		this.sqlWithoutBindParamDetected = sqlWithoutBindParamDetected;
	}
}
