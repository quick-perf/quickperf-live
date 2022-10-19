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
package config.mbean;

import org.junit.jupiter.api.Test;
import org.quickperf.web.spring.config.DatabaseConfig;
import org.quickperf.web.spring.config.DatabaseHttpConfig;
import org.quickperf.web.spring.config.JvmConfig;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import static org.assertj.core.api.Assertions.assertThat;

public class QuickPerfMBeansTest {

    private final ApplicationContextRunner applicationContextRunnerWithQuickPerfAndJmxEnabled = new ApplicationContextRunner()
            .withPropertyValues("spring.jmx.enabled=true"
                              , "quickperf.enabled=true")
            .withConfiguration(AutoConfigurations.of(JmxAutoConfiguration.class));

    @Test public void
    should_register_jvm_config_m_bean() {

        applicationContextRunnerWithQuickPerfAndJmxEnabled
                .withBean(JvmConfig.class)
                .run(context -> {

                    MBeanServer mBeanServer = context.getBean(MBeanServer.class);

                    ObjectName databaseConfig = new ObjectName("QuickPerf:category=JVM");
                    assertThat(mBeanServer.isRegistered(databaseConfig)).isTrue();

                    MBeanInfo jvmConfigMBeanInfo = mBeanServer.getMBeanInfo(databaseConfig);

                    MBeanAttributeInfo[] jvmConfigAttributes = jvmConfigMBeanInfo.getAttributes();

                    assertThat(jvmConfigAttributes).extracting("name")
                            .containsOnly("HeapAllocationMeasured"
                                        , "HeapAllocationThresholdDetected"
                                        , "HeapAllocationThresholdValueInBytes");

                    assertThat(jvmConfigMBeanInfo.getOperations()).isNotNull();

                });

    }

    @Test public void
    should_register_database_config_m_bean() {

        applicationContextRunnerWithQuickPerfAndJmxEnabled
                .withBean(DatabaseConfig.class)
                .run(context -> {

                    MBeanServer mBeanServer = context.getBean(MBeanServer.class);

                    ObjectName databaseConfig = new ObjectName("QuickPerf:category=Database");
                    assertThat(mBeanServer.isRegistered(databaseConfig)).isTrue();

                    MBeanInfo databaseConfigMBeanInfo = mBeanServer.getMBeanInfo(databaseConfig);

                    MBeanAttributeInfo[] databaseConfigAttributes = databaseConfigMBeanInfo.getAttributes();

                    assertThat(databaseConfigAttributes).extracting("name")
                            .containsOnly("NPlusOneSelectDetected"
                                        , "NPlusOneSelectDetectionThreshold"
                                        , "DatabaseConnectionProfiled"
                                        , "SelectedColumnsDisplayed"
                                        , "SqlDisplayed"
                                        , "SqlExecutionTimeDetected"
                                        , "SqlExecutionTimeThresholdInMilliseconds"
                                        , "SqlExecutionDetected"
                                        , "SqlExecutionThreshold");

                    assertThat(databaseConfigMBeanInfo.getOperations()).isNotNull();

                });

    }

    @Test public void
    should_register_database_http_config_m_bean() {

        applicationContextRunnerWithQuickPerfAndJmxEnabled
                .withBean(DatabaseHttpConfig.class)
                .run(context -> {

                    MBeanServer mBeanServer = context.getBean(MBeanServer.class);

                    ObjectName databaseConfig = new ObjectName("QuickPerf:category=Database - HTTP");
                    assertThat(mBeanServer.isRegistered(databaseConfig)).isTrue();

                    MBeanInfo databaseConfigMBeanInfo = mBeanServer.getMBeanInfo(databaseConfig);

                    MBeanAttributeInfo[] databaseConfigAttributes = databaseConfigMBeanInfo.getAttributes();

                    assertThat(databaseConfigAttributes).extracting("name")
                            .containsOnly("SynchronousHttpCallBetweenDbConnectionGottenAndClosedDetected");

                    assertThat(databaseConfigMBeanInfo.getOperations()).isNotNull();

                });

    }

    @Test public void
    should_register_test_generation_config_m_bean() {

        applicationContextRunnerWithQuickPerfAndJmxEnabled
                .withBean(TestGenerationConfig.class)
                .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
                .run(context -> {

                    MBeanServer mBeanServer = context.getBean(MBeanServer.class);

                    ObjectName databaseConfig = new ObjectName("QuickPerf:category=Test generation");
                    assertThat(mBeanServer.isRegistered(databaseConfig)).isTrue();

                    MBeanInfo databaseConfigMBeanInfo = mBeanServer.getMBeanInfo(databaseConfig);

                    MBeanAttributeInfo[] databaseConfigAttributes = databaseConfigMBeanInfo.getAttributes();

                    assertThat(databaseConfigAttributes).extracting("name")
                            .containsOnly( "JavaClassFolder"
                                         , "Junit4GenerationEnabled"
                                         , "Junit5GenerationEnabled"
                                         , "TestResourceFolder");

                    assertThat(databaseConfigMBeanInfo.getOperations()).isNotNull();

                });

    }

}
