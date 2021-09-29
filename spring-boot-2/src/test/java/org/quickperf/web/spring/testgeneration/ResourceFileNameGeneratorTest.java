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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceFileNameGeneratorTest {

    @Test public void
    base_test() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/api/owners"))
                .isEqualTo("api-owners");
    }

    @Test public void
    url_question_mark_and_equal() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/owners?lastName="))
                .isEqualTo("owners-lastName");
    }

    @Test public void
    url_with_star() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/api/owners/*/lastname/e"))
                .isEqualTo("api-owners-lastname-e");
    }

    @Test public void
    html() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/vets.html"))
                .isEqualTo("vets-html");
    }

    @Test public void
    url_with_dash() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/external-call"))
                .isEqualTo("external-call");
    }

    @Test public void
    url_finishing_with_dash() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/owners/"))
                .isEqualTo("owners");
    }

    @Test public void
    root_url() {
        assertThat(ResourceFileNameGenerator.INSTANCE.buildFileNameWithoutExtension("/"))
                .isEqualTo("root-url");
    }

}
