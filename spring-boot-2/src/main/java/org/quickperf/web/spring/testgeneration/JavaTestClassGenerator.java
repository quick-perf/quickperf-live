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

import org.quickperf.web.spring.Application;
import org.quickperf.web.spring.HttpContentType;

import java.util.Optional;

class JavaTestClassGenerator {

    static final JavaTestClassGenerator INSTANCE = new JavaTestClassGenerator();

    private JavaTestClassGenerator() {
    }

    TestFile generateJunitTestClass(String relativeHttpUrl
                                  , Application application
                                  , Optional<TestFile> optionalSqlFile
                                  , String expectedResponseFileName
                                  , HttpContentType httpContentType
                                  , JUnitVersion jUnitVersion) {

        JavaClassGenerationConfig javaClassGenerationConfig
                = new JavaClassGenerationConfig(  relativeHttpUrl
                                                , application
                                                , optionalSqlFile
                                                , expectedResponseFileName
                                                , httpContentType
                                                , jUnitVersion);
        String testClassContent = junit4Junit5TestClassContentGenerator.INSTANCE
                .generate(javaClassGenerationConfig);

        String className = ClassNameGenerator.INSTANCE
                .generateClassNameFrom(relativeHttpUrl) + ".java";

        return new TestFile(className, testClassContent);

    }

}
