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

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.quickperf.spring.springboottest.controller.HttpUrl;
import testgen.util.FileContentVerifier;
import testgen.util.TestGen;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnit5TestGenHtmlWithoutSqlTest extends TestGen {

    @Test public void
    should_generate_a_junit5_test_for_html_return_and_without_sql_execution() throws IOException {

        // GIVEN
        testGenerationConfig.setJunit5GenerationEnabled(true);

        String testRelativePath = "junit5" + File.separator + "html-without-sql";

        String pathOfFolderContainingGeneratedFiles
                = createTestFolderAndReturnPathWithTestFolderName(testRelativePath);
        testGenerationConfig.setJavaClassFolder(pathOfFolderContainingGeneratedFiles);
        testGenerationConfig.setTestResourceFolder(pathOfFolderContainingGeneratedFiles);

        // WHEN
        performGetOnUrlReturningHtmlOrText(HttpUrl.HTML_WITHOUT_SQL);

        // THEN
        File folderContainingGeneratedFiles = new File(pathOfFolderContainingGeneratedFiles);

        Condition<? super File> aFileEndingWithSqlExtension = new Condition<File>() {
            @Override
            public boolean matches(File file) {
                return file.getName().endsWith(".sql");
            }
        };
        assertThat(folderContainingGeneratedFiles)
                .doesNotHave(aFileEndingWithSqlExtension);

        FileContentVerifier fileContentVerifier =
                new FileContentVerifier(pathOfFolderContainingGeneratedFiles
                                      , testRelativePath);

        fileContentVerifier.compareGeneratedFileWithReferenceFile("html-without-sql-expected.html");
        fileContentVerifier.compareGeneratedFileWithReferenceFile("HtmlWithoutSqlTest.java");

    }

}
