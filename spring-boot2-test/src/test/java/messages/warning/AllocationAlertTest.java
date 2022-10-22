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
package messages.warning;

import messages.util.TestQuickPerfMessages;
import messages.util.TestableQuickPerfMessage;
import org.junit.jupiter.api.Test;
import org.quickperf.spring.springboottest.controller.HttpUrl;

import static messages.util.QuickPerfMessagesAssert.assertThat;

public class AllocationAlertTest extends TestQuickPerfMessages {

    @Test public void
    should_alert_if_allocation_exceeds_threshold() {

        jvmConfig.setHeapAllocationThresholdDetected(true);
        jvmConfig.setHeapAllocationThresholdValueInBytes(10_000_000);

        performGetOnUrlReturningJson(HttpUrl.HUGE_ALLOCATION);

        TestableQuickPerfMessage quickPerfWarnings = warningInterceptor.getWarnings();

        assertThat(quickPerfWarnings)
                .hasAMessageContaining("GET 200 http://localhost/huge-allocation")
                .hasAMessageContaining("[WARNING] Heap allocation is greater than");


    }

}
