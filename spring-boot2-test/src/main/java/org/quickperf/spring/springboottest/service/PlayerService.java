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
package org.quickperf.spring.springboottest.service;

import org.quickperf.spring.springboottest.OtherApplicationPort;
import org.quickperf.spring.springboottest.controller.HttpUrl;
import org.quickperf.spring.springboottest.dto.PlayerWithTeamName;
import org.quickperf.spring.springboottest.jpa.entity.Player;
import org.quickperf.spring.springboottest.jpa.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final RestTemplate restTemplate;

    private final OtherApplicationPort otherApplicationPort;

    public PlayerService(PlayerRepository playerRepository
                       , RestTemplate restTemplate
                       , OtherApplicationPort otherApplicationPort) {
        this.playerRepository = playerRepository;
        this.restTemplate = restTemplate;
        this.otherApplicationPort = otherApplicationPort;
    }

    @Transactional(readOnly = true)
    public List<PlayerWithTeamName> findPlayersWithTeamName() {
        List<Player> players = playerRepository.findAll();
        return  players
               .stream()
               .map(player -> new PlayerWithTeamName(
                                    player.getFirstName()
                                  , player.getLastName()
                                  , player.getTeam().getName()
                              )
                    )
               .collect(Collectors.toList());
    }

    @Transactional
    public void performSynchronousHttpCallDuringDbConnection() {
        Player newPlayer = new Player();
        performSynchronousHttpCall();
        playerRepository.save(newPlayer);
    }

    private void performSynchronousHttpCall() {
        String otherApplicationGetUrl = "http://localhost:"
                                      + otherApplicationPort.getPortValue()
                                      + HttpUrl.OTHER_APPLICATION;
        restTemplate.getForEntity(otherApplicationGetUrl, String.class);
    }

    @Transactional(readOnly = true)
    public Player performSynchronousHttpCallBetweenConnectionGottenAndClosed() {
        performSynchronousHttpCall();
        return playerRepository.findById(1L).get();
    }

}
