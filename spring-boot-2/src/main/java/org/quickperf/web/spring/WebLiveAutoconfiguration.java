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
package org.quickperf.web.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickperf.web.spring.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({ DatabaseConfig.class
        , JvmConfig.class
        , DatabaseHttpConfig.class
        , TestGenerationConfig.class
        , QuickPerfBeforeRequestServletFilter.class
        , QuickPerfAfterRequestServletFilter.class
        , QuickPerfHttpCallHttpCallWarningLogger.class
        , QuickPerfHttpCallHttpCallInfoLogger.class
        , ExternalHttCallInterceptionConfig.class
		, UrlConfig.class
})
@ConditionalOnProperty(value = "quickperf.enabled")
public class WebLiveAutoconfiguration {

	private static final Log LOGGER = LogFactory.getLog(WebLiveAutoconfiguration.class);

	private final UrlConfig urlConfig;


	{
		LOGGER.info("Quickperf Live is enabled");
	}

	public WebLiveAutoconfiguration(UrlConfig urlConfig) {
		this.urlConfig = urlConfig;
		LOGGER.info("Quickperf excluded urls: "  + urlConfig.getExcludedUrls());
	}


}
