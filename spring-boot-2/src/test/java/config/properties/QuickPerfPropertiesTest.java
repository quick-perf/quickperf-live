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
package config.properties;

import org.junit.jupiter.api.Test;
import org.quickperf.web.spring.config.DatabaseConfig;
import org.quickperf.web.spring.config.DatabaseHttpConfig;
import org.quickperf.web.spring.config.JvmConfig;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class QuickPerfPropertiesTest {

    @Test public void
    should_initialize_jvm_config_from_a_properties_file() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withInitializer(new ConfigFileApplicationContextInitializer())
                .withPropertyValues("spring.config.location=classpath:quickperf-properties-test.properties")
                .withBean(JvmConfig.class);

        applicationContextRunner.run(context -> {
            JvmConfig jvmConfig = context.getBean(JvmConfig.class);
                    assertThat(jvmConfig.isHeapAllocationMeasured()).isTrue();
            assertThat(jvmConfig.isHeapAllocationThresholdDetected()).isTrue();
            assertThat(jvmConfig.getHeapAllocationThresholdValueInBytes()).isEqualTo(100_000);

        }
        );

    }

    @Test public void
    should_initialize_database_config_from_a_properties_file() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withInitializer(new ConfigFileApplicationContextInitializer())
                .withPropertyValues("spring.config.location=classpath:quickperf-properties-test.properties")
                .withBean(DatabaseConfig.class);

        applicationContextRunner.run(context -> {

            DatabaseConfig databaseConfig = context.getBean(DatabaseConfig.class);
            assertThat(databaseConfig.isNPlusOneSelectDetected()).isTrue();
            assertThat(databaseConfig.getNPlusOneSelectDetectionThreshold()).isEqualTo(20);
            assertThat(databaseConfig.isDatabaseConnectionProfiled()).isTrue();
            assertThat(databaseConfig.isSqlDisplayed()).isTrue();
            assertThat(databaseConfig.isSelectedColumnsDisplayed()).isTrue();
            assertThat(databaseConfig.isSqlExecutionTimeDetected()).isTrue();
            assertThat(databaseConfig.getSqlExecutionTimeThresholdInMilliseconds()).isEqualTo(50);

                }
        );

    }

    @Test public void
    should_initialize_database_http_config_from_a_properties_file() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withInitializer(new ConfigFileApplicationContextInitializer())
                .withPropertyValues("spring.config.location=classpath:quickperf-properties-test.properties")
                .withBean(DatabaseHttpConfig.class);

        applicationContextRunner.run(context -> {

                DatabaseHttpConfig databaseHttpConfig = context.getBean(DatabaseHttpConfig.class);
                assertThat(databaseHttpConfig.isSynchronousHttpCallBetweenDbConnectionGottenAndClosedDetected()).isTrue();

                }
        );

    }

    @Test public void
    should_initialize_test_generation_config_from_a_properties_file() {

        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
                .withInitializer(new ConfigFileApplicationContextInitializer())
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .withPropertyValues("spring.config.location=classpath:quickperf-properties-test.properties")
                .withBean(TestGenerationConfig.class);

        applicationContextRunner.run(context -> {

            TestGenerationConfig testGenerationConfig = context.getBean(TestGenerationConfig.class);
            assertThat(testGenerationConfig.isJunit4GenerationEnabled()).isTrue();
            assertThat(testGenerationConfig.isJunit5GenerationEnabled()).isTrue();
            assertThat(testGenerationConfig.getTestResourceFolder()).isEqualTo(".\\src\\test\\resources");
            assertThat(testGenerationConfig.getJavaClassFolder()).isEqualTo(".\\src\\test\\java");

                }
        );

    }


}
