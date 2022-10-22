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

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import org.quickperf.TestExecutionContext;
import org.quickperf.sql.SqlExecutions;
import org.quickperf.sql.SqlRecorder;

import java.util.List;

class LongDbRequestsListener implements SqlRecorder<SqlExecutions>  {

    private final int thresholdInMilliseconds;

    private final SqlExecutions sqlExecutionsGreaterOrEqualToThreshold = new SqlExecutions();

    public LongDbRequestsListener(int thresholdInMilliseconds) {
        this.thresholdInMilliseconds = thresholdInMilliseconds;
    }

    @Override
    public void addQueryExecution(ExecutionInfo execInfo, List<QueryInfo> queries, int listenerIdentifier) {
        long elapsedTime = execInfo.getElapsedTime();
        if(elapsedTime >= thresholdInMilliseconds) {
            sqlExecutionsGreaterOrEqualToThreshold.add(execInfo, queries);
        }
    }

    public SqlExecutions getSqlExecutionsGreaterOrEqualToThreshold() {
        return sqlExecutionsGreaterOrEqualToThreshold;
    }

    @Override
    public void startRecording(TestExecutionContext testExecutionContext) {

    }

    @Override
    public void stopRecording(TestExecutionContext testExecutionContext) {

    }

    @Override
    public SqlExecutions findRecord(TestExecutionContext testExecutionContext) {
        return null;
    }

    @Override
    public void cleanResources() {
    }

}
