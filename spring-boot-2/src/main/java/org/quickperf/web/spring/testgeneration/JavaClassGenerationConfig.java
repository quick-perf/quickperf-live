package org.quickperf.web.spring.testgeneration;/*
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
import org.quickperf.web.spring.Application;
import org.quickperf.web.spring.HttpContentType;

import java.util.Optional;

class JavaClassGenerationConfig {

    final String relativeHttpUrl;
    final Application application;
    final Optional<TestFile> optionalSqlFile;
    final String expectedResponseFileName;
    final HttpContentType httpContentType;
    final JUnitVersion jUnitVersion;

    //TODO: REFACTOR TO LIMIT PARAMETER NUMBER
    JavaClassGenerationConfig(String relativeHttpUrl
                             , Application application
                             , Optional<TestFile> optionalSqlFile
                             , String expectedResponseFileName
                             , HttpContentType httpContentType
                             , JUnitVersion jUnitVersion) {
        this.relativeHttpUrl = relativeHttpUrl;
        this.application = application;
        this.optionalSqlFile = optionalSqlFile;
        this.expectedResponseFileName = expectedResponseFileName;
        this.httpContentType = httpContentType;
        this.jUnitVersion = jUnitVersion;
    }

}
