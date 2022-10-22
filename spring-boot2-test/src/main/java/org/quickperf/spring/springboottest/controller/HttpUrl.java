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

package org.quickperf.spring.springboottest.controller;

public interface HttpUrl {

    String JSON_WITH_N_PLUS_ONE_SELECT = "/json-with-n-plus-one-select";

    String JSON_WITHOUT_SQL = "/json-without-sql";

    String HTML_WITHOUT_SQL = "/html-without-sql";

    String TEXT_WITHOUT_SQL = "/text-without-sql";

    String SYNCHRONOUS_HTTP_CALL_DURING_DB_CONNECTION = "/synchronous-http-call-during-db-connection";

    String OTHER_APPLICATION = "/other-application";

    String HTTP_CALL_BETWEEN_CONNECTION_GOTTEN_AND_CLOSED = "/http-call-between-connection-gotten-and-closed";

    String HUGE_ALLOCATION = "/huge-allocation";

}
