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
import org.quickperf.web.spring.Application;
import org.quickperf.web.spring.HttpContentType;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.qstd.QuickSqlTestData;

import java.util.List;
import java.util.Optional;

import static java.lang.System.lineSeparator;

public class TestGenerator {

    public static TestGenerator INSTANCE = new TestGenerator();

    private TestGenerator() {
    }

    //TODO: REFACTOR TO LIMIT PARAMETER NUMBER
    public String generateJUnitTestForGet(List<QueryInfo> selectQueries
                                        , String relativeHttpUrl
                                        , Application application
                                        , String contentType
                                        , String content
                                        , TestGenerationConfig testGenerationConfig
                                        , QuickSqlTestData quickSqlTestData
                                        , JUnitVersion jUnitVersion) {

        String fileNameWithoutExtension = ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension(relativeHttpUrl);
        String resourcePath = testGenerationConfig.getTestResourceFolder();

        Optional<TestFile> optionalSqlFile = Optional.empty();

        if (!selectQueries.isEmpty()) {
            TestFile sqlFile = SqlScriptFileGenerator.INSTANCE
                    .generate(selectQueries
                            , quickSqlTestData
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

        return buildTestGenerationReport(optionalSqlFile, expectedResponseFile
                                        , javaTestFile, jUnitVersion);

    }

    private String buildTestGenerationReport( Optional<TestFile> optionalSqlFile, TestFile expectedResponseFile
                                            , TestFile javaTestFile, JUnitVersion jUnitVersion) {
        StringBuilder report = new StringBuilder();
        report.append("* TEST GENERATION");
        int junitVersionNumber = jUnitVersion.getNumber();
        report.append(lineSeparator() + "\t* JUnit " + junitVersionNumber + " test class: " + javaTestFile.filePath);
        if (optionalSqlFile.isPresent()) {
            TestFile sqlFile = optionalSqlFile.get();
            report.append(lineSeparator() + "\t* SQL script file: " + sqlFile.filePath);
        }
        report.append(lineSeparator() + "\t* Expected response: " + expectedResponseFile.filePath);
        return report.toString();
    }

}
