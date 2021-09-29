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
package testgen.util;

import org.quickperf.spring.springboottest.ASpringBootApplication;
import org.quickperf.spring.springboottest.OtherApplicationPort;
import org.quickperf.spring.springboottest.RestTemplateConfig;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.htmlunit.LocalHostWebClient;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(
          properties = {"quickperf.enabled=true"}
        , classes = { ASpringBootApplication.class
                    , RestTemplateConfig.class
                    , OtherApplicationPort.class
                    }
        , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("db-mem")
@DirtiesContext
public class TestGen {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    protected TestGenerationConfig testGenerationConfig;

    protected String createTestFolderAndReturnPathWithTestFolderName(String testFolderName) {
        String targetFolderPath = findTargetFolderPath();
        String testFolderPath = targetFolderPath + File.separator + testFolderName;
        File testFolder = new File(testFolderPath);
        testFolder.mkdirs();
        return testFolderPath;
    }

    private String findTargetFolderPath() {
        Path targetDirectory = Paths.get("target");
        return targetDirectory.toFile().getAbsolutePath();
    }

    protected void performGetOnUrlReturningJson(String url) {
        String httpUrl = "http://localhost:" + port + url;
        restTemplate.getForEntity(httpUrl, Object.class);
    }

    protected void performGetOnUrlReturningHtmlOrText(String url) throws IOException {
        LocalHostWebClient localHostWebClient = new LocalHostWebClient(environment);
        localHostWebClient.getPage(url);
    }

}
