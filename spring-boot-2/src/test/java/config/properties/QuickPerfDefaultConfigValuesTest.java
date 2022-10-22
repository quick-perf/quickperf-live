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
package config.properties;

import org.junit.jupiter.api.Test;
import org.quickperf.web.spring.config.DatabaseConfig;
import org.quickperf.web.spring.config.DatabaseHttpConfig;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class QuickPerfDefaultConfigValuesTest {

    @Test public void
    default_database_config_values() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withPropertyValues("quickperf.enabled=true")
                .withBean(DatabaseConfig.class);

        applicationContextRunner.run(context -> {
            DatabaseConfig databaseConfig = context.getBean(DatabaseConfig.class);
            assertThat(databaseConfig.isNPlusOneSelectDetected()).isFalse();
            assertThat(databaseConfig.getNPlusOneSelectDetectionThreshold()).isEqualTo(3);
            assertThat(databaseConfig.isSelectedColumnsDisplayed()).isFalse();
            assertThat(databaseConfig.isSqlDisplayed()).isFalse();
            assertThat(databaseConfig.isDatabaseConnectionProfiled()).isFalse();
                }
        );

    }

    @Test public void
    default_database_http_config_values() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withPropertyValues("quickperf.enabled=true")
                .withBean(DatabaseHttpConfig.class);

        applicationContextRunner.run(context -> {
            DatabaseHttpConfig databaseHttpConfig = context.getBean(DatabaseHttpConfig.class);
            assertThat(databaseHttpConfig.isSynchronousHttpCallBetweenDbConnectionGottenAndClosedDetected())
                    .isFalse();
                }
        );

    }

    @Test public void
    default_test_generation_config_values() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withPropertyValues("quickperf.enabled=true")
                .withBean(TestGenerationConfig.class)
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class));

        applicationContextRunner.run(context -> {
            TestGenerationConfig testGenerationConfig = context.getBean(TestGenerationConfig.class);
            assertThat(testGenerationConfig.isJunit4GenerationEnabled()).isFalse();
            assertThat(testGenerationConfig.isJunit5GenerationEnabled()).isFalse();
                }
        );

    }

}
