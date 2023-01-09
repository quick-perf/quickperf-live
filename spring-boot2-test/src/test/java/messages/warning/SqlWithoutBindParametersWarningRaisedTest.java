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
import org.springframework.test.annotation.DirtiesContext;

import static messages.util.QuickPerfMessagesAssert.assertThat;
import static org.quickperf.spring.springboottest.controller.HttpUrl.*;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SqlWithoutBindParametersWarningRaisedTest extends TestQuickPerfMessages {

    @Test
    public void
    should_warn_when_query_does_not_have_bind_parameters() {

        databaseConfig.setSqlWithoutBindParamDetected(true);

        performGetOnUrlReturningJson(FIND_PLAYER_WITH_UNBOUNDED_PARAMS+"?name=Ronaldo");

        TestableQuickPerfMessage quickPerfWarnings = warningInterceptor.getWarnings();

        assertThat(quickPerfWarnings)
                .hasAMessageContaining("[WARNING] Some of your SQL queries don't use bind parameters.");

    }

    @Test
    public void
    should_not_warn_when_query_does_have_bind_parameters() {

        databaseConfig.setSqlWithoutBindParamDetected(true);

        performGetOnUrlReturningJson(FIND_PLAYER_WITH_BIND_PARAMS+"?name=Ronaldo");

        TestableQuickPerfMessage quickPerfWarnings = warningInterceptor.getWarnings();

        assertThat(quickPerfWarnings).isEmpty();

    }
}
