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

import com.jakewharton.fliptables.FlipTable;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.QueryType;
import org.quickperf.TestExecutionContext;
import org.quickperf.sql.QueryTypeRetriever;
import org.quickperf.sql.SqlExecutions;
import org.quickperf.sql.SqlRecorder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

class SelectStatsListener implements SqlRecorder<SqlExecutions> {

    private final Map<String, Map<String, Set<String>>> columnsByTableBySchema = new HashMap<>();

    @Override
    public void addQueryExecution(ExecutionInfo execInfo, List<QueryInfo> queries, int listenerIdentifier) {

        if (!atLeastOneSelect(queries)) {
            return;
        }

        if (!dbExceptionHappened(execInfo) && !executeMethodOnStatement(execInfo)) {

            ResultSet resultSet = (ResultSet) execInfo.getResult();
            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {

                    String schema = metaData.getSchemaName(i);
                    Map<String, Set<String>> columnsByTable = columnsByTableBySchema.computeIfAbsent(schema, s -> new HashMap<>());

                    String table = metaData.getTableName(i);
                    String column = metaData.getColumnName(i);
                    Set<String> columns = columnsByTable.computeIfAbsent(table, t -> new HashSet<>());
                    columns.add(column);

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    private boolean atLeastOneSelect(List<QueryInfo> queries) {
        QueryTypeRetriever queryTypeRetriever = QueryTypeRetriever.INSTANCE;
        for (QueryInfo query : queries) {
            QueryType queryType = queryTypeRetriever.typeOf(query);
            if (queryType == QueryType.SELECT) {
                return true;
            }
        }
        return false;
    }

    private boolean dbExceptionHappened(ExecutionInfo executionInfo) {
        return executionInfo.getResult() == null;
    }

    private boolean executeMethodOnStatement(ExecutionInfo executionInfo) {
        return !(executionInfo.getResult() instanceof ResultSet);
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

    public String getColumnsByTableBySchemaAsString() {

        Set<String> schemas = columnsByTableBySchema.keySet();

        StringBuilder sb = new StringBuilder();

        for (String schema : schemas) {

            String[] headers = {"Table", "Selected columns", "Nb"};

            String[][] data = buildTableData(schema);

            String table = buildTable(schema, headers, data);

            String tableWithoutLastLineBreak = table.substring(0, table.length() - 1);

            sb.append(tableWithoutLastLineBreak);

        }

        return sb.toString();

    }

    private String[][] buildTableData(String schema) {
        List<String[]> dataList = buildTableDataAsList(schema, columnsByTableBySchema);
        return dataList.toArray(new String[dataList.size()][2]);
    }

    private List<String[]> buildTableDataAsList(String schema, Map<String, Map<String, Set<String>>> columnsByTableBySchema) {

        List<String[]> dataList = new ArrayList<>();

        Map<String, Set<String>> columnsByTable = columnsByTableBySchema.get(schema);
        Set<String> tables = columnsByTable.keySet();

        for (String table : tables) {
            String[] data = buildDataLine(columnsByTable, table);
            dataList.add(data);
        }

        return dataList;

    }

    private String[] buildDataLine(Map<String, Set<String>> columnsByTable, String table) {
        Set<String> columns = columnsByTable.get(table);
        String columnsAsString = String.join(" * ", columns);

        String[] data = new String[3];
        data[0] = table;
        data[1] = columnsAsString;
        data[2] = String.valueOf(columns.size());

        return data;
    }

    private String buildTable(String schema, String[] headers, String[][] data) {
        String table = FlipTable.of(headers, data);
        if (schema != null && !schema.isEmpty()) {
            return addSchemaAtFirstLine(schema, table);
        }
        return table;
    }

    private String addSchemaAtFirstLine(String schema, String table) {
        String[] noHeader = {schema};
        String[][] allData = {{table}};
        return FlipTable.of(noHeader, allData);
    }

}
