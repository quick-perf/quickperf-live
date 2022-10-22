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

import org.junit.jupiter.api.AfterEach;
import org.quickperf.otherapp.OtherApplication;
import org.quickperf.spring.springboottest.ASpringBootApplication;
import org.quickperf.spring.springboottest.OtherApplicationPort;
import org.quickperf.spring.springboottest.RestTemplateConfig;
import org.quickperf.web.spring.config.DatabaseConfig;
import org.quickperf.web.spring.config.DatabaseHttpConfig;
import org.quickperf.web.spring.config.JvmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(
          properties = {"quickperf.enabled=true"}
        , classes = { ASpringBootApplication.class
                    , RestTemplateConfig.class
                    , OtherApplicationPort.class
                    , QuickPerfHttpCallWarningInterceptor.class
                    , QuickPerfHttpCallInfoInterceptor.class
                    }
)
@AutoConfigureMockMvc
@ActiveProfiles("db-mem")
@DirtiesContext
public abstract class TestQuickPerfMessages {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected QuickPerfHttpCallInfoInterceptor quickPerfHttpCallInfoInterceptor;

    @Autowired
    protected QuickPerfHttpCallWarningInterceptor warningInterceptor;

    @Autowired
    protected DatabaseConfig databaseConfig;

    @Autowired
    protected JvmConfig jvmConfig;

    @Autowired
    protected DatabaseHttpConfig databaseHttpConfig;

    @Autowired
    private OtherApplicationPort otherApplicationPort;

    protected ConfigurableApplicationContext applicationContext;

    protected void performGetOnUrlReturningJson(String url) {
        try {
            mockMvc.perform(get(url));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected void performPostOnUrl(String url) {
        try {
            mockMvc.perform(post(url));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected void startOtherApplication() {

        SpringApplication otherApplication = new SpringApplication(OtherApplication.class);

        Map<String, Object> serverProperties = new HashMap<>();
        serverProperties.put("server.port", "" + otherApplicationPort.getPortValue());
        serverProperties.put("spring.main.banner-mode", "off");
        otherApplication.setDefaultProperties(serverProperties);

        applicationContext = otherApplication.run();

    }

    @AfterEach
    void closeApplicationContextOfOtherAppIfExists() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

}
