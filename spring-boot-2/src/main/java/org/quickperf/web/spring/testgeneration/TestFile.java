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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class TestFile {

    final String name;

    final String content;

    String filePath = "";

    TestFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    void writeInto(String dir) {
        writeFile(dir);
        filePath =  dir + File.separator + name;
    }

    private void writeFile(String dir) {
        Path sqlScriptFilePath = Paths.get(dir + File.separator + name);
        try {
            Files.write(sqlScriptFilePath, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
