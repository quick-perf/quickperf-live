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

import java.util.Optional;

public class junit4Junit5TestClassContentGenerator implements JavaClassGenerator {

    private static final String SPRING_BOOT_APPLICATION_CANONICAL_NAME_PART = "spring_boot_application_canonical_name";
    private static final String SPRING_BOOT_APPLICATION_CLASS_NAME_PART = "spring_boot_application_class_name";
    private static final String HTTP_URL_TEMPLATE_PART = "http_url";
    private static final String METHOD_NAME_TEMPLATE_PART = "method_name";
    private static final String CLASS_NAME_TEMPLATE_PART = "test_class_name";
    private static final String SQL_SCRIPT_FILE_TEMPLATE_PART = "script-file";
    private static final String RESPONSE_TEMPLATE_PART = "response-file-name";
    private static final String QUICK_PERF_TEST_ANNOT_IMPORT = "import org.quickperf.junit5.QuickPerfTest;\n";
    private static final String DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAM_VALUES_ANNOT_IMPORT = "import org.quickperf.sql.annotation.DisableSameSelectTypesWithDifferentParamValues;\n";
    private static final String QUICK_PERF_TEST_ANNOT = "@QuickPerfTest\n";

    private static final String TEST_CLASS_TEMPLATE
            //"package ...\n" +
                    //"\n" +
            ;

    private static final String SQL_SCRIPT_ANNOT = "@Sql(\"/" + SQL_SCRIPT_FILE_TEMPLATE_PART + "\")\n";

    private static final String DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAM_VALUES_ANNOT = "    " + "@DisableSameSelectTypesWithDifferentParamValues\n";

    private static final String JSONCOMPARE_MODE_IMPORT = "import org.skyscreamer.jsonassert.JSONCompareMode;\n";

    private static final String ASSERTJ_ASSERT_THAT_IMPORT = "import static org.assertj.core.api.Assertions.assertThat;\n";

    private static final String JSON_ASSERT_ASSERT_EQUALS_IMPORT = "import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;\n";

    private static final String JUNIT_5_TEST_ANNOT_IMPORT = "import org.junit.jupiter.api.Test;\n";

    private static final String SQL_ANNOT_IMPORT = "import org.springframework.test.context.jdbc.Sql;\n";

    static {
        TEST_CLASS_TEMPLATE =
                JUNIT_5_TEST_ANNOT_IMPORT +
                QUICK_PERF_TEST_ANNOT_IMPORT +
                DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAM_VALUES_ANNOT_IMPORT +
                JSONCOMPARE_MODE_IMPORT +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;\n" +
                "import org.springframework.boot.test.context.SpringBootTest;\n" +
                "import org.springframework.mock.web.MockHttpServletResponse;\n" +
                "import " + SPRING_BOOT_APPLICATION_CANONICAL_NAME_PART + ";\n" +
                SQL_ANNOT_IMPORT +
                "import org.springframework.test.web.servlet.MockMvc;\n" +
                "import org.springframework.test.web.servlet.MvcResult;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.net.URISyntaxException;\n" +
                "import java.nio.file.Files;\n" +
                "import java.nio.file.Path;\n" +
                "import java.nio.file.Paths;\n" +
                "import java.util.stream.Collectors;\n" +
                "import java.util.stream.Stream;\n" +
                "\n" +
                JSON_ASSERT_ASSERT_EQUALS_IMPORT +
                ASSERTJ_ASSERT_THAT_IMPORT +
                "import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;\n" +
                "import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\n" +
                "\n" +

                "@SpringBootTest(classes = {" + SPRING_BOOT_APPLICATION_CLASS_NAME_PART + ".class})\n" +
                SQL_SCRIPT_ANNOT +
                QUICK_PERF_TEST_ANNOT +
                "@AutoConfigureMockMvc\n" +
                "public class " + CLASS_NAME_TEMPLATE_PART + " {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private MockMvc mockMvc;\n" +
                "\n" +
                DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAM_VALUES_ANNOT +
                "    @Test\n" +
                "    public void " + METHOD_NAME_TEMPLATE_PART + "() throws Exception {\n" +
                "        MvcResult result = mockMvc.perform(get(" + "\"" + HTTP_URL_TEMPLATE_PART + "\"))\n" +
                "                                  .andExpect(status().isOk())\n" +
                "                                  .andReturn();\n" +
                "\n" +
                "        MockHttpServletResponse response = result.getResponse();\n" +
                "        String actualResponseContent = response.getContentAsString();\n" +
                "\n" +
                "        String expectedResponseContent = findExpectedResponseContent();\n" +
                "\n";
    }

    public static junit4Junit5TestClassContentGenerator INSTANCE = new junit4Junit5TestClassContentGenerator();

    private static final String ASSERTJ_ASSERTION =     "        assertThat(actualResponseContent).isEqualToNormalizingWhitespace(expectedResponseContent);\n";
    private static final String JSON_ASSERT_ASSERTION = "        assertEquals(expectedResponseContent, actualResponseContent, JSONCompareMode.LENIENT);\n";

    private static final String textTemplatePart =
                    JSON_ASSERT_ASSERTION +
                    ASSERTJ_ASSERTION +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    private String findExpectedResponseContent() {\n" +
                    "        Path path;\n" +
                    "        try {\n" +
                    "            path = Paths.get(getClass().getClassLoader()\n" +
                    "                        .getResource(\"" + RESPONSE_TEMPLATE_PART + "\").toURI());\n" +
                    "        } catch (URISyntaxException e) {\n" +
                    "            throw new IllegalStateException(e);\n" +
                    "        }\n" +
                    "        try(Stream<String> lines = Files.lines(path)) {\n" +
                    "            return lines.collect(Collectors.joining(\"\\n\"));\n" +
                    "         } catch (IOException e) {\n" +
                    "            throw new IllegalStateException(e);\n" +
                    "         }\n" +
                    "    }" + "\n" +
                    "\n" +
                    "}\n";

    junit4Junit5TestClassContentGenerator() { }

    @Override
    public String generate(JavaClassGenerationConfig generationConfig) {

        String methodName = JavaMethodNameGenerator.INSTANCE
                           .generateGetTestMethodFrom(generationConfig.relativeHttpUrl);

        String className = ClassNameGenerator.INSTANCE
                          .generateClassNameFrom(generationConfig.relativeHttpUrl);

        String result = (TEST_CLASS_TEMPLATE + textTemplatePart);

        Optional<TestFile> optionalSqlFile = generationConfig.optionalSqlFile;

        if(generationConfig.jUnitVersion == JUnitVersion.VERSION_4) {
            result = result.replace(JUNIT_5_TEST_ANNOT_IMPORT, "import org.junit.Test;\n" );
            result = result.replace(QUICK_PERF_TEST_ANNOT_IMPORT, "import org.junit.runner.RunWith;\n" +
                                                                  "import org.quickperf.spring.junit4.QuickPerfSpringRunner;\n");
            result = result.replace(QUICK_PERF_TEST_ANNOT, "@RunWith(QuickPerfSpringRunner.class)\n");
        }

        if(optionalSqlFile.isPresent()) {
            TestFile sqlFile = optionalSqlFile.get();
            result = result.replace(SQL_SCRIPT_FILE_TEMPLATE_PART, sqlFile.name);
        } else {
            result = result.replace(SQL_ANNOT_IMPORT, "");
            result = result.replace(QUICK_PERF_TEST_ANNOT_IMPORT, "");
            result = result.replace(QUICK_PERF_TEST_ANNOT, "");
            result = result.replace(DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAM_VALUES_ANNOT_IMPORT, "");
            result = result.replace(DISABLE_SAME_SELECT_TYPES_WITH_DIFFERENT_PARAM_VALUES_ANNOT, "");
            result = result.replace(SQL_SCRIPT_ANNOT, "");
        }

        if(!generationConfig.httpContentType.isJson()) {
            result = result.replace(JSONCOMPARE_MODE_IMPORT, "");
            result = result.replace(JSON_ASSERT_ASSERT_EQUALS_IMPORT, "");
            result = result.replace(JSON_ASSERT_ASSERTION, "");
        } else {
            result = result.replace(ASSERTJ_ASSERT_THAT_IMPORT, "");
            result = result.replace(ASSERTJ_ASSERTION, "");
        }

        return   result
                .replace(SPRING_BOOT_APPLICATION_CANONICAL_NAME_PART, generationConfig.application.getCanonicalName())
                .replace(SPRING_BOOT_APPLICATION_CLASS_NAME_PART, generationConfig.application.getClassName())
                .replace(HTTP_URL_TEMPLATE_PART, generationConfig.relativeHttpUrl)
                .replace(METHOD_NAME_TEMPLATE_PART, methodName)
                .replace(CLASS_NAME_TEMPLATE_PART, className)
                .replace(RESPONSE_TEMPLATE_PART, generationConfig.expectedResponseFileName)
                ;

    }

}
