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
package org.quickperf.web.spring.testgeneration;

import net.ttddyy.dsproxy.QueryInfo;
import org.qstd.SqlQuery;
import org.qstd.QuickSqlTestData;

import java.util.ArrayList;
import java.util.List;

public class SqlScriptFileGenerator {

    static final SqlScriptFileGenerator INSTANCE = new SqlScriptFileGenerator();

    private SqlScriptFileGenerator() {
    }

    TestFile generate(List<QueryInfo> selectQueries, QuickSqlTestData quickSqlTestData, String fileNameWithoutExtension) {
        String fileName = fileNameWithoutExtension + ".sql";
        String fileContent = createSqlScriptFile(selectQueries, quickSqlTestData);
        return new TestFile(fileName, fileContent);
    }

    private String createSqlScriptFile(List<QueryInfo> selectQueries, QuickSqlTestData quickSqlTestData) {
        List<SqlQuery> sqlQueriesForSqlTestDataGenerator = buildQueriesForSqlTestDataGenerator(selectQueries);
        return quickSqlTestData.generateInsertScriptFor(sqlQueriesForSqlTestDataGenerator);
    }

    private List<SqlQuery> buildQueriesForSqlTestDataGenerator(List<QueryInfo> selectQueries) {
        List<SqlQuery> sqlQueries = new ArrayList<>();
        for (QueryInfo selectQuery : selectQueries) {
            String query = selectQuery.getQuery();
            List<Object> params = QueryParamsExtractor.INSTANCE.getParamsOf(selectQuery);
            SqlQuery sqlQuery = new SqlQuery(query, params);
            sqlQueries.add(sqlQuery);
        }
        return sqlQueries;
    }

}
