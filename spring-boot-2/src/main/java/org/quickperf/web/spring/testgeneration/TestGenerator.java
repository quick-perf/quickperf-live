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
package org.quickperf.web.spring.testgeneration;

import net.ttddyy.dsproxy.QueryInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickperf.web.spring.Application;
import org.quickperf.web.spring.HttpContentType;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.stdg.SqlTestDataGenerator;

import java.util.List;
import java.util.Optional;

import static java.lang.System.lineSeparator;

public class TestGenerator {

    public static TestGenerator INSTANCE = new TestGenerator();

    private final Log logger = LogFactory.getLog(this.getClass());

    private TestGenerator() {
    }

    //TODO: REFACTOR TO LIMIT PARAMETER NUMBER
    public void generateJUnitTestForGet(List<QueryInfo> selectQueries
                                      , String relativeHttpUrl
                                      , Application application
                                      , String contentType
                                      , String content
                                      , TestGenerationConfig testGenerationConfig
                                      , String httpCallReport
                                      , SqlTestDataGenerator sqlTestDataGenerator
                                      , JUnitVersion jUnitVersion) {

        String fileNameWithoutExtension = ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension(relativeHttpUrl);
        String resourcePath = testGenerationConfig.getTestResourceFolder();

        Optional<TestFile> optionalSqlFile = Optional.empty();

        if (!selectQueries.isEmpty()) {
            TestFile sqlFile = SqlScriptFileGenerator.INSTANCE
                    .generate(selectQueries
                            , sqlTestDataGenerator
                            , fileNameWithoutExtension);
            sqlFile.writeInto(resourcePath);
            optionalSqlFile = Optional.of(sqlFile);
        }

        TestFile expectedResponseFile = ExpectedResponseFileGenerator.INSTANCE
                .generate(content, contentType, fileNameWithoutExtension);
        expectedResponseFile.writeInto(resourcePath);

        HttpContentType httpContentType = new HttpContentType(contentType);

        TestFile javaTestFile =
                    JavaTestClassGenerator.INSTANCE.generateJunitTestClass(
                              relativeHttpUrl
                            , application
                            , optionalSqlFile
                            , expectedResponseFile.name
                            , httpContentType
                            , jUnitVersion
                    );

        String javaDir = testGenerationConfig.getJavaClassFolder();
        javaTestFile.writeInto(javaDir);

        String report = buildReport( httpCallReport
                                   , optionalSqlFile
                                   , expectedResponseFile
                                   , javaTestFile);

        logger.info(report);

    }

    private String buildReport( String httpCallReport
                              , Optional<TestFile> optionalSqlFile
                              , TestFile expectedResponseFile
                              , TestFile javaTestFile) {
        StringBuilder report = new StringBuilder();
        report.append("Test generation for HTTP call: " + httpCallReport);
        report.append(lineSeparator() + "\t * JUnit 5 test class: " + javaTestFile.filePath);
        if (optionalSqlFile.isPresent()) {
            TestFile sqlFile = optionalSqlFile.get();
            report.append(lineSeparator() + "\t * SQL script file: " + sqlFile.filePath);
        }
        report.append(lineSeparator() + "\t * Expected response: " + expectedResponseFile.filePath);
        return report.toString();
    }

}
