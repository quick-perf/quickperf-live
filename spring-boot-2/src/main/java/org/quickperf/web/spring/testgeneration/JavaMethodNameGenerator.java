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

class JavaMethodNameGenerator {

    static JavaMethodNameGenerator INSTANCE = new JavaMethodNameGenerator();

    private JavaMethodNameGenerator() { }

    String generateGetTestMethodFrom(String relativeHttpUrl) {
        if("/".equals(relativeHttpUrl)) {
            return "get_root_url";
        }
        String underscore = "_";
        String methodName = "get"
                           + (relativeHttpUrlWithoutEndSlash(relativeHttpUrl)
                             .replace("/", underscore)
                             .replace("?", underscore)
                             .replace("=", underscore)
                             .replace("_*_", underscore)
                             .replace(".", underscore)
                             .replace("-", underscore)
                             );
        if(methodName.endsWith("_")) {
            return methodName.substring(0, methodName.length() - 1);
        }
        return methodName;
    }

    private String relativeHttpUrlWithoutEndSlash(String relativeHttpUrl) {
        if(relativeHttpUrl.endsWith("/")) {
            int newStringLength = relativeHttpUrl.length() - 1;
            return relativeHttpUrl.substring(0, newStringLength);
        }
        return relativeHttpUrl;
    }

}
