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

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class TextServletRegistration {

    private static class TextServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
            response.setHeader("Content-Type", "text/plain");
            PrintWriter writer = response.getWriter();
            writer.write("hello");
            writer.close();
        }
    }

    @Bean
    public ServletRegistrationBean exampleServletBean() {
        TextServlet textServlet = new TextServlet();
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(textServlet, HttpUrl.TEXT_WITHOUT_SQL);
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }

}
