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

import java.util.ArrayList;
import java.util.List;

public class ClassNameGenerator {

    public static ClassNameGenerator INSTANCE = new ClassNameGenerator();

    public ClassNameGenerator() { }

    public String generateClassNameFrom(String relativeHttpUrl) {

        if("/".equals(relativeHttpUrl)) {
            return "RootUrlTest";
        }

        List<Integer> positionToSetUppercase = findSlashQuestionMarkStarDotDashEqualPositions(relativeHttpUrl);

        String upperCasesBeforeSlash = setUpperCaseBeforePositions(relativeHttpUrl
                                                                 , positionToSetUppercase);
        String emptyString = "";
        return   upperCasesBeforeSlash
                .replace("/", emptyString)
                .replace("?", emptyString)
                .replace("*", emptyString)
                .replace("=", emptyString)
                .replace(".", emptyString)
                .replace("-", emptyString)
                + "Test";

    }

    private List<Integer> findSlashQuestionMarkStarDotDashEqualPositions(String relativeHttpUrl) {
        List<Integer> slashPositions = new ArrayList<>();
        String[] stringElements = relativeHttpUrl.split("");
        for (int pos = 0; pos < stringElements.length; pos++) {
            String element = stringElements[pos];
            if(  "/".equals(element)  || "?".equals(element)
              || "=".equals(element)  || "*".equals(element)
              || ".".equals(element)  || "-".equals(element)
              )
            slashPositions.add(pos);
        }
        return slashPositions;
    }

    private String setUpperCaseBeforePositions(String relativeHttpUrl, List<Integer> slashPositions) {
        String[] elements = relativeHttpUrl.split("");
        String[] elementsWithUpperCaseBeforeSlash = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            if (i == 0 || slashPositions.contains(i - 1)) {
                elementsWithUpperCaseBeforeSlash[i] = elements[i].toUpperCase();
            } else {
                elementsWithUpperCaseBeforeSlash[i] = elements[i];
            }
        }
        return String.join("", elementsWithUpperCaseBeforeSlash);
    }

}
