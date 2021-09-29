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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quickperf.web.spring.Application;
import org.quickperf.web.spring.HttpContentType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Junit4Junit5TestClassGeneratorTestContent {

    private final Application applicationMock = mock(Application.class);

    @BeforeEach
    public void before() {
        when(applicationMock.getClassName()).thenReturn("applicationClassName");
        when(applicationMock.getCanonicalName()).thenReturn("canonicalName");
    }

    @Test public void
    should_use_http_url() {

        Optional<TestFile> noSqlFile = Optional.empty();
        HttpContentType httpContentType = new HttpContentType("");
        JUnitVersion jUnitVersion = JUnitVersion.VERSION_4;
        JavaClassGenerationConfig javaClassGenerationConfig
                = new JavaClassGenerationConfig("/owners?lastName="
                                                , applicationMock
                                                , noSqlFile
                                                , ""
                                                , httpContentType, jUnitVersion);
        assertThat(
                junit4Junit5TestClassContentGenerator.INSTANCE.generate(javaClassGenerationConfig)
                  )
        .contains("mockMvc.perform(get(\"/owners?lastName=\"))");

    }

    @Test public void
    should_generate_method_name() {

        Optional<TestFile> noSqlFile = Optional.empty();
        HttpContentType httpContentType = new HttpContentType("");
        JUnitVersion jUnitVersion = JUnitVersion.VERSION_4;
        JavaClassGenerationConfig javaClassGenerationConfig =
                new JavaClassGenerationConfig("/owners?lastName="
                                             , applicationMock
                                             , noSqlFile
                                             , ""
                                             , httpContentType
                                             , jUnitVersion);

        assertThat(
                junit4Junit5TestClassContentGenerator.INSTANCE.generate(javaClassGenerationConfig)
                  )
        .contains("public void get_owners_lastName()");

    }

    @Test public void
    should_generate_class_name() {

        Optional<TestFile> noSqlFile = Optional.empty();
        HttpContentType httpContentType = new HttpContentType("");
        JUnitVersion jUnitVersion = JUnitVersion.VERSION_4;
        JavaClassGenerationConfig javaClassGenerationConfig =
                new JavaClassGenerationConfig("/owners?lastName="
                                             , applicationMock
                                             , noSqlFile
                                             , ""
                                             , httpContentType
                                             , jUnitVersion);

        assertThat(
                junit4Junit5TestClassContentGenerator.INSTANCE.generate(javaClassGenerationConfig)
                )
       .contains("public class OwnersLastNameTest {");

    }

}