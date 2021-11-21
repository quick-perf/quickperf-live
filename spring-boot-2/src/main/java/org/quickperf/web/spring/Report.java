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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static java.lang.System.lineSeparator;

class Report {

    private final StringBuilder reportBuilder = new StringBuilder();
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    public Report(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    void append(String reportPart) {
        reportBuilder.append(reportPart);
    }

    void writeWith(Collection<? extends QuickPerfHttpCallWriter> writers) throws Exception {
        if (!hasReportContent()) {
            appendHttpCallAtFirstPosition();
            writeAndClose(writers);
        }
    }

    private boolean hasReportContent() {
        String reportAsString = reportBuilder.toString();
        return reportAsString.isEmpty();
    }

    private void appendHttpCallAtFirstPosition() {
        String urlResponseReport = HttpResponseReportRetriever.INSTANCE.findHttpCallReport(httpServletRequest, httpServletResponse);
        reportBuilder.insert(0, lineSeparator() + urlResponseReport);
    }

    private void writeAndClose(Collection<? extends QuickPerfHttpCallWriter> writers) throws Exception {
        for (QuickPerfHttpCallWriter writer : writers) {
            try (QuickPerfHttpCallWriter writerToClose = writer) {
                String reportAsString = reportBuilder.toString();
                writerToClose.write(reportAsString);
            }
        }
    }

}
