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
package messages.util;

import org.assertj.core.api.AbstractAssert;

import static java.lang.System.lineSeparator;

public class QuickPerfMessagesAssert extends AbstractAssert<QuickPerfMessagesAssert, TestableQuickPerfMessage> {

    private final TestableQuickPerfMessage testableQuickPerfMessage;

    public QuickPerfMessagesAssert(TestableQuickPerfMessage testableQuickPerfMessage, Class<?> selfType) {
        super(testableQuickPerfMessage, selfType);
        this.testableQuickPerfMessage = testableQuickPerfMessage;
    }

    public static QuickPerfMessagesAssert assertThat(TestableQuickPerfMessage quickPerfWarnings) {
        return new QuickPerfMessagesAssert(quickPerfWarnings, QuickPerfMessagesAssert.class);
    }

    public QuickPerfMessagesAssert hasAMessageContaining(String aString) {
        if(!testableQuickPerfMessage.hasAWarningContaining(aString)) {
            failWithMessage("No message contains <" + aString + ">. "
                           + lineSeparator()
                           + currentWarnings());
        }
        return this;
    }

    public QuickPerfMessagesAssert isEmpty() {
        if(!testableQuickPerfMessage.isEmpty()) {
            failWithMessage("No QuickPerf message expected"
                           + lineSeparator()
                           + currentWarnings());
        }
        return this;
    }

    public QuickPerfMessagesAssert hasNoWarningContaining(String aString) {
        if(testableQuickPerfMessage.hasAWarningContaining(aString)) {
            failWithMessage("A warning contains <" + aString + ">. "
                           + lineSeparator()
                           + currentWarnings());
        }
        return this;
    }

    private String currentWarnings() {
        return "The QuickPerf messages are: " + testableQuickPerfMessage;
    }

}
