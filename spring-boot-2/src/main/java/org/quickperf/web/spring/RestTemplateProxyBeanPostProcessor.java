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
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestTemplateProxyBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final Log logger = LogFactory.getLog(this.getClass());

    RestTemplateProxyBeanPostProcessor() {
        logger.debug(this.getClass().getSimpleName() + "is created");
    }

    private static class QuickPerfRestTemplateInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            long start = System.currentTimeMillis();

            ClientHttpResponse response = execution.execute(request, body);

            long execTime = System.currentTimeMillis() - start;

            String urlResponseReport = HttpResponseReportRetriever.INSTANCE.findHttpCallReport(request, response);

            String description = urlResponseReport + "- Execution time: " + execTime + " ms";
            HttpCall httpCall = new HttpCall(description);
            SynchronousHttpCallsRegistry.INSTANCE.register(httpCall);

            PerfEventsRegistry.INSTANCE.registerPerfEvent(PerfEvent.SYNCHRONOUS_HTTP_CALL);

            return response;

        }

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof RestTemplate) {
            RestTemplate restTemplate = (RestTemplate) bean;
            List<ClientHttpRequestInterceptor> interceptors
                    = new ArrayList<>(restTemplate.getInterceptors());
            QuickPerfRestTemplateInterceptor interceptor = new QuickPerfRestTemplateInterceptor();
            interceptors.add(interceptor);
            restTemplate.setInterceptors(interceptors);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
