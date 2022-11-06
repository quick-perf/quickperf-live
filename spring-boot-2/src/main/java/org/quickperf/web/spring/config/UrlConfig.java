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
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@ManagedResource(
        objectName="QuickPerf:category=Url",
        description="QuickPerf Live MBeans")
public class UrlConfig {

    @Value("${quickperf.exclude-urls:/actuator}")
    private String excludedUrls;


    @ManagedAttribute
    public String getExcludedUrls() {
        return excludedUrls;
    }

    @ManagedOperation
    public void setExcludedUrls(String excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

    private Set<String> extractedExcludedUrlsSet(){
        return Arrays.stream(this.getExcludedUrls().split(",")).collect(Collectors.toSet());
    }

    public boolean checkIfExcluded(String url) {
        Predicate<Set<String>> checkExcludedUrls = excludedUrls -> {
             return   excludedUrls.stream().anyMatch(excludedUrl -> url.contains(excludedUrl));
        };
     return checkExcludedUrls.test(extractedExcludedUrlsSet());
    }
}
