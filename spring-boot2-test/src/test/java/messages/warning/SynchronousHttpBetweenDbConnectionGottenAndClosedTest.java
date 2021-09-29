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
package messages.warning;

import org.junit.jupiter.api.Test;
import org.quickperf.spring.springboottest.controller.HttpUrl;
import messages.util.TestQuickPerfMessages;
import messages.util.TestableQuickPerfMessage;

import static java.lang.System.lineSeparator;
import static messages.util.QuickPerfMessagesAssert.assertThat;

public class SynchronousHttpBetweenDbConnectionGottenAndClosedTest extends TestQuickPerfMessages {

    @Test public void
    should_detect_a_synchronous_http_call_between_a_database_connection_is_gotten_from_the_connection_pool_and_closed() {

        // GIVEN
        databaseHttpConfig.setSynchronousHttpCallBetweenDbConnectionGottenAndClosedDetected(true);
        startOtherApplication();

        // WHEN
        performGetOnUrlReturningJson(HttpUrl.HTTP_CALL_BETWEEN_CONNECTION_GOTTEN_AND_CLOSED);

        // THEN
        TestableQuickPerfMessage quickPerfWarnings = warningInterceptor.getWarnings();

        assertThat(quickPerfWarnings)
                .hasAMessageContaining(
                        "GET 200 http://localhost/http-call-between-connection-gotten-and-closed" + lineSeparator() +
                                "\t* [WARNING] Synchronous HTTP call between the time the DB connection is gotten from the data source and closed" + lineSeparator() +
                                "\t* Synchronous HTTP calls" + lineSeparator() +
                                "\t\t* GET 200 http://localhost:");

    }

}
