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
package org.quickperf.web.spring;

public class HttpContentType {

    private final String contentTypeAsString;

    public HttpContentType(String contentTypeAsString) {
        this.contentTypeAsString = contentTypeAsString;
    }

    public boolean isJson() {
        return contentTypeAsString != null && contentTypeAsString.contains("application/json");
    }

    public boolean isText() {
        return contentTypeAsString != null && contentTypeAsString.contains("text/plain");
    }

    public boolean isHtml() {
        return contentTypeAsString != null && contentTypeAsString.contains("text/html");
    }

    public boolean isPdf() {
        return contentTypeAsString != null && contentTypeAsString.contains("application/pdf");
    }

    public boolean isZip() {
        return contentTypeAsString != null && contentTypeAsString.contains("application/zip");
    }

}
