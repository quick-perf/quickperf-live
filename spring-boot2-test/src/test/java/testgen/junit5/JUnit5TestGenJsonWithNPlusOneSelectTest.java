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
package testgen.junit5;

import messages.util.TestableQuickPerfMessage;
import org.junit.jupiter.api.Test;
import org.quickperf.spring.springboottest.controller.HttpUrl;
import testgen.util.FileContentVerifier;
import testgen.util.TestGen;

import java.io.File;
import java.io.IOException;

import static messages.util.QuickPerfMessagesAssert.assertThat;

public class JUnit5TestGenJsonWithNPlusOneSelectTest extends TestGen {

    @Test public void
    should_generate_a_junit5_test_for_json_return_and_n_plus_one_select() throws IOException {

        // GIVEN
        testGenerationConfig.setJunit5GenerationEnabled(true);

        String testRelativePath = "junit5" + File.separator + "json-with-n-plus-one-select";

        String pathOfFolderContainingGeneratedFiles =
                createTestFolderAndReturnPathWithTestFolderName(testRelativePath);

        testGenerationConfig.setJavaClassFolder(pathOfFolderContainingGeneratedFiles);
        testGenerationConfig.setTestResourceFolder(pathOfFolderContainingGeneratedFiles);

        // WHEN
        performGetOnUrlReturningJson(HttpUrl.JSON_WITH_N_PLUS_ONE_SELECT);

        // THEN
        TestableQuickPerfMessage quickPerfInfos = quickPerfHttpCallInfoInterceptor.getInfos();
        assertThat(quickPerfInfos).hasAMessageContaining("* JUnit 5 test class: ")
                                  .hasAMessageContaining("* SQL script file:")
                                  .hasAMessageContaining("* Expected response:");

        FileContentVerifier fileContentVerifier =
                new FileContentVerifier(pathOfFolderContainingGeneratedFiles
                                      , testRelativePath);

        fileContentVerifier.compareGeneratedFileWithReferenceFile("json-with-n-plus-one-select.sql");
        fileContentVerifier.compareGeneratedFileWithReferenceFile("json-with-n-plus-one-select-expected.json");
        fileContentVerifier.compareGeneratedFileWithReferenceFile("JsonWithNPlusOneSelectTest.java");

    }

}
