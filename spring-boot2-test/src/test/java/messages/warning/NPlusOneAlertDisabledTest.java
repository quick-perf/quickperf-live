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

import org.junit.jupiter.api.Test;
import messages.util.TestQuickPerfMessages;
import messages.util.TestableQuickPerfMessage;

import static org.quickperf.spring.springboottest.controller.HttpUrl.JSON_WITH_N_PLUS_ONE_SELECT;
import static messages.util.QuickPerfMessagesAssert.assertThat;

public class NPlusOneAlertDisabledTest extends TestQuickPerfMessages {

    @Test public void
    should_not_alert_of_n_plus_one_select() {

        performGetOnUrlReturningJson(JSON_WITH_N_PLUS_ONE_SELECT);

        TestableQuickPerfMessage quickPerfWarnings = warningInterceptor.getWarnings();

        assertThat(quickPerfWarnings)
                .hasNoWarningContaining("GET 200 http")
                .hasNoWarningContaining("N+1 select suspicion");

    }

}