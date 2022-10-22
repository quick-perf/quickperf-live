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

import org.springframework.context.ApplicationContext;

import java.util.Map;

public class Application {

    private final String className;

    private final String canonicalName;

    private Application(String className, String canonicalName) {
        this.className = className;
        this.canonicalName = canonicalName;
    }

    public static Application from(ApplicationContext context) {
        // Example: PetClinicApplication$$EnhancerBySpringCGLIB$$86fd2bb
        Class<?> springBootApplicationClass = extractSpringBootApplicationClassFrom(context);
        String className = springBootApplicationClass.getSimpleName().split("\\$")[0];
        String canonicalName = springBootApplicationClass.getCanonicalName().split("\\$")[0];
        return new Application(className, canonicalName);
    }

    private static Class<?> extractSpringBootApplicationClassFrom(ApplicationContext context) {
        Map<String, Object> annotatedBeans = context.getBeansWithAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
        return annotatedBeans.values().toArray()[0].getClass();
    }

    public String getClassName() {
        return className;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

}
