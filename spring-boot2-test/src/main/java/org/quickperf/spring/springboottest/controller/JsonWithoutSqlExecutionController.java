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

import org.quickperf.spring.springboottest.dto.PlayerWithTeamName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class JsonWithoutSqlExecutionController {

    @GetMapping(HttpUrl.JSON_WITHOUT_SQL)
    public List<PlayerWithTeamName> jsonWithoutSql() {
        PlayerWithTeamName paulPogba = new PlayerWithTeamName("Paul", "Pogba", "Manchester United");
        PlayerWithTeamName antoineGriezmann = new PlayerWithTeamName("Antoine", "Griezmann", "Barcelona" );
        return asList(paulPogba, antoineGriezmann);
    }

}
