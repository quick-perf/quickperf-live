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

import org.quickperf.web.spring.HttpContentType;

class ExpectedResponseFileGenerator {

    static final ExpectedResponseFileGenerator INSTANCE = new ExpectedResponseFileGenerator();

    private ExpectedResponseFileGenerator() {
    }

    TestFile generate(String content, String contentType, String fileNameWithoutExtension) {
        String responseExtension = findExpectedResponseFileExtension(contentType);
        String expectedResponseFileName = fileNameWithoutExtension + "-expected" + responseExtension;
        return new TestFile(expectedResponseFileName, content);
    }

    private String findExpectedResponseFileExtension(String contentType) {
        HttpContentType httpContentType = new HttpContentType(contentType);
        if (httpContentType.isJson()) {
            return ".json";
        } else if (httpContentType.isHtml()) {
            return ".html";
        }
        return ".txt";
    }

}
