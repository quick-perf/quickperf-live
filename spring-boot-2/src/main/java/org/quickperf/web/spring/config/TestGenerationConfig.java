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
package org.quickperf.web.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.qstd.QuickSqlTestData;

import javax.sql.DataSource;

@Component
@ManagedResource(
        objectName="QuickPerf:category=Test generation",
        description="Test generation MBeans")
public class TestGenerationConfig {

    @Value("${quickperf.test-generation.junit4.enabled:false}")
    private boolean junit4GenerationEnabled;

    @Value("${quickperf.test-generation.junit5.enabled:false}")
    private boolean junit5GenerationEnabled;

    @Value("${quickperf.test-generation.resource-folder-path:}")
    private String testResourceFolder;

    @Value("${quickperf.test-generation.java-folder-path:}")
    private String javaClassFolder;

    @ManagedOperation
    public void setJunit4GenerationEnabled(boolean junit4GenerationEnabled) {
        this.junit4GenerationEnabled = junit4GenerationEnabled;
    }

    @ManagedOperation
    public void setJunit5GenerationEnabled(boolean junit5GenerationEnabled) {
        this.junit5GenerationEnabled = junit5GenerationEnabled;
    }

    @ManagedAttribute
    public boolean isJunit5GenerationEnabled() {
        return junit5GenerationEnabled;
    }

    @ManagedAttribute
    public boolean isJunit4GenerationEnabled() {
        return junit4GenerationEnabled;
    }

    public boolean isTestGenerationEnabled() {
        return isJunit4GenerationEnabled() || isJunit5GenerationEnabled();
    }

    @ManagedAttribute
    public String getTestResourceFolder() {
        return testResourceFolder;
    }

    @ManagedAttribute
    public String getJavaClassFolder() {
        return javaClassFolder;
    }

    @ManagedOperation
    public void setTestResourceFolder(String testResourceFolder) {
        this.testResourceFolder = testResourceFolder;
    }

    @ManagedOperation
    public void setJavaClassFolder(String javaClassFolder) {
        this.javaClassFolder = javaClassFolder;
    }

    // One instantiation to benefit from database metadata caching on several HTTP calls
    @Bean
    public QuickSqlTestData sqlTestDataGenerator(DataSource dataSource) {
        return QuickSqlTestData.buildFrom(dataSource);
    }

}
