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
import org.quickperf.spring.springboottest.jpa.entity.Player;
import org.quickperf.spring.springboottest.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnboundParamsQueryController {

    private final PlayerService playerService;

    public UnboundParamsQueryController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(HttpUrl.FIND_PLAYER_WITH_BIND_PARAMS)
    public List<Player> findBindParam(@RequestParam("name") String lastName) {
        return playerService.findPlayerByBindedParamsQuery(lastName);
    }

    @GetMapping(HttpUrl.FIND_PLAYER_WITH_UNBOUNDED_PARAMS)
    public List<Player> findUnbound(@RequestParam("name") String lastName) {
        return playerService.findPlayerByUnboundParamsQuery(lastName);
    }

}
