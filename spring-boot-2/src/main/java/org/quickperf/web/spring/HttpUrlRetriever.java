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
package org.quickperf.web.spring;

import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

class HttpUrlRetriever {

    static HttpUrlRetriever INSTANCE = new HttpUrlRetriever();

    private HttpUrlRetriever() { }

    String findRelativeUrlFrom(HttpServletRequest httpServletRequest) {

        String urlWithQueryParams = findUrlWithQueryParamsFrom(httpServletRequest);

        String contextPath = httpServletRequest.getServletContext().getContextPath();
        if(!contextPath.isEmpty()) {
            return urlWithQueryParams.split(contextPath)[1];
        }

        String servletPath = httpServletRequest.getServletPath();
        boolean parametersExist = !urlWithQueryParams.endsWith(servletPath);
        if(!parametersExist) {
            return servletPath;
        }
        return servletPath + (urlWithQueryParams.split(servletPath)[1]);

    }

    String findUrlWithQueryParamsFrom(HttpServletRequest request) {
        return request.getQueryString() == null ? request.getRequestURL().toString() :
                request.getRequestURL().append("?").append(request.getQueryString()).toString();
    }

    String findUrlWithQueryParamsFrom(HttpRequest request) {
        return request.getURI().toString();
    }

}
