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
package org.quickperf.spring.springboottest;

import org.springframework.stereotype.Component;

@Component
public class OtherApplicationPort {

    private final int portValue;

    public OtherApplicationPort() {
        int min = 1;
        int max = 50_000;
        portValue = (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public int getPortValue() {
        return portValue;
    }

}
