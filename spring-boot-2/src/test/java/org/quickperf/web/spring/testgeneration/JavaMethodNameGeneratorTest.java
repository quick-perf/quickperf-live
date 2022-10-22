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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaMethodNameGeneratorTest {

    @Test public void
    base_test() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/api/owners"))
                .isEqualTo("get_api_owners");
    }

    @Test public void
    url_ends_with_equal() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/owners?lastName="))
                .isEqualTo("get_owners_lastName");
    }

    @Test public void
    url_with_star() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/api/owners/*/lastname/e"))
                .isEqualTo("get_api_owners_lastname_e");
    }

    @Test public void
    html_url() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/vets.html"))
                .isEqualTo("get_vets_html");
    }

    @Test public void
    url_with_dash() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/external-call"))
                .isEqualTo("get_external_call");
    }

    @Test public void
    url_finishing_with_dash() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/owners/"))
                .isEqualTo("get_owners");
    }

    @Test public void
    root_url() {
        assertThat(JavaMethodNameGenerator.INSTANCE.generateGetTestMethodFrom("/"))
                .isEqualTo("get_root_url");
    }

}