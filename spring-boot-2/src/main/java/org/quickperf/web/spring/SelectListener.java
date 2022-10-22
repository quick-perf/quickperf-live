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
import net.ttddyy.dsproxy.QueryType;
import org.quickperf.TestExecutionContext;
import org.quickperf.sql.QueryTypeRetriever;
import org.quickperf.sql.SqlExecutions;
import org.quickperf.sql.SqlRecorder;

import java.util.ArrayList;
import java.util.List;

public class SelectListener implements SqlRecorder<SqlExecutions>  {

    private final List<QueryInfo> selectQueries = new ArrayList<>();

    public List<QueryInfo> getSelectQueries() {
        return new ArrayList<>(selectQueries);
    }

    @Override
    public void addQueryExecution(ExecutionInfo executionInfo, List<QueryInfo> queries, int listenerIdentifier) {
        QueryTypeRetriever queryTypeRetriever = QueryTypeRetriever.INSTANCE;
        for (QueryInfo query : queries) {
            QueryType queryType = queryTypeRetriever.typeOf(query);
            if (queryType == QueryType.SELECT) {
                selectQueries.add(query);
            }
        }
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
