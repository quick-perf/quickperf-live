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

import net.ttddyy.dsproxy.QueryInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickperf.sql.SqlExecutions;
import org.quickperf.sql.SqlRecorderRegistry;
import org.quickperf.sql.connection.ConnectionListenerRegistry;
import org.quickperf.sql.select.analysis.SelectAnalysis;
import org.quickperf.sql.select.analysis.SelectAnalysisExtractor;
import org.quickperf.web.spring.config.DatabaseConfig;
import org.quickperf.web.spring.config.DatabaseHttpConfig;
import org.quickperf.web.spring.config.JvmConfig;
import org.quickperf.web.spring.config.TestGenerationConfig;
import org.quickperf.web.spring.jvm.ByteWatcherSingleThread;
import org.quickperf.web.spring.jvm.ByteWatcherSingleThreadRegistry;
import org.quickperf.web.spring.testgeneration.JUnitVersion;
import org.quickperf.web.spring.testgeneration.TestGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.stdg.SqlTestDataGenerator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static org.quickperf.web.spring.QuickPerfBeforeRequestServletFilter.DiagnosticConnectionProfiler;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class QuickPerfAfterRequestServletFilter implements Filter {

    private final DatabaseConfig databaseConfig;

    private final DatabaseHttpConfig databaseHttpConfig;

    private final TestGenerationConfig testGenerationConfig;

    private final SqlTestDataGenerator sqlTestDataGenerator;

    private final Set<QuickPerfHttpCallWarningWriter> quickPerfHttpCallWarningWriters;

    private final Set<QuickPerfHttpCallInfoWriter> quickPerfHttpCallInfoWriters;

    private final Log logger = LogFactory.getLog(this.getClass());

    private final ApplicationContext context;

    private final JvmConfig jvmConfig;

    public QuickPerfAfterRequestServletFilter(DatabaseConfig databaseConfig
            , DatabaseHttpConfig databaseHttpConfig
            , JvmConfig jvmConfig
            , TestGenerationConfig testGenerationConfig
            , SqlTestDataGenerator sqlTestDataGenerator
            , ApplicationContext context
            , Set<QuickPerfHttpCallWarningWriter> quickPerfHttpCallWarningWriters
            , Set<QuickPerfHttpCallInfoWriter> quickPerfHttpCallInfoWriters) {
        this.jvmConfig = jvmConfig;
        this.databaseConfig = databaseConfig;
        this.databaseHttpConfig = databaseHttpConfig;
        this.testGenerationConfig = testGenerationConfig;
        this.sqlTestDataGenerator = sqlTestDataGenerator;
        this.quickPerfHttpCallWarningWriters = quickPerfHttpCallWarningWriters;
        this.quickPerfHttpCallInfoWriters = quickPerfHttpCallInfoWriters;
        logger.debug(this.getClass().getSimpleName() + "is created");
        this.context = context;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Throwable problem = null;

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (testGenerationConfig.isTestGenerationEnabled()) {
            httpServletResponse = new CopyHttpServletResponse(servletResponse);
        }

        try {
            filterChain.doFilter(servletRequest, httpServletResponse);
        } catch (Throwable t) {
            problem = t;
        }

        //if (servletResponse.isCommitted()) { // Some test generation tests may fail
                                               // if we add a isCommitted condition
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String contentTypeAsString = httpServletResponse.getContentType();
        HttpContentType httpContentType = new HttpContentType(contentTypeAsString);

        try {
            if (httpContentType.isHtml() || httpContentType.isJson() || httpContentType.isText()
                    || httpContentType.isPdf() || httpContentType.isPdf()) {
                quickPerfProcessing(httpServletRequest, httpServletResponse);
            }
        } catch (Exception e) {
            // Propose to create QuickPerfIssue
            logger.warn("Unexpected QuickPerf issue", e);
        } finally {
             unregisterListeners();
        }
        //}

        handleProblem(problem);
    }

    private void unregisterListeners() {
        ByteWatcherSingleThreadRegistry.INSTANCE.unregister();
        SqlRecorderRegistry.INSTANCE.clear();
        // Petclinic bug
        // ConnectionListenerRegistry.unregister(PerfEventConnectionListener.INSTANCE);
        ConnectionListenerRegistry.INSTANCE.clear();
        SynchronousHttpCallsRegistry.INSTANCE.unregisterHttpCalls();
        PerfEventsRegistry.INSTANCE.unregisterPerfEvents();
    }

    @Override
    public void destroy() {

    }

    private void handleProblem(Throwable problem) throws ServletException, IOException {
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            //sendProcessingError(problem, response);
        }
    }

    private void appendHttpCallAtFirstPosition(HttpServletRequest servletRequest, HttpServletResponse servletResponse, StringBuilder logMessage) {
        String urlResponseReport = HttpResponseReportRetriever.INSTANCE.findHttpCallReport(servletRequest, servletResponse);
        logMessage.insert(0, lineSeparator() + urlResponseReport);
    }

    private void quickPerfProcessing(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        StringBuilder warnLogMessage = new StringBuilder();

        StringBuilder infoReport = new StringBuilder();

        if (jvmConfig.isHeapAllocationMeasured() || jvmConfig.isHeapAllocationThresholdDetected()) {
            ByteWatcherSingleThread byteWatcherSingleThread = ByteWatcherSingleThreadRegistry.INSTANCE.get();
            long allocationInBytes = byteWatcherSingleThread.calculateAllocations();

            if (jvmConfig.isHeapAllocationThresholdDetected()) {
                int thresholdInBytes = jvmConfig.getHeapAllocationThresholdValueInBytes();
                if (allocationInBytes > thresholdInBytes) {
                    NumberFormat allocationFormat = buildNumberFormatWithGrouping();
                    String heapAllocationExceededMessage = "\t* [WARNING] Heap allocation is greater than "
                            + allocationFormat.format(thresholdInBytes) + " bytes: "
                            + allocationFormat.format(allocationInBytes) + " bytes";
                    warnLogMessage.append(lineSeparator() + heapAllocationExceededMessage);
                }
            }

            if(jvmConfig.isHeapAllocationMeasured()) {
                NumberFormat allocationFormat = buildNumberFormatWithGrouping();
                infoReport.append(lineSeparator() + "* HEAP ALLOCATION: " + allocationFormat.format(allocationInBytes) + " bytes");
            }

        }

        SqlExecutionsRecorder sqlExecutionsRecorder = SqlRecorderRegistry.INSTANCE.getSqlRecorderOfType(SqlExecutionsRecorder.class);

        SqlExecutions sqlExecutions = null;
        if (databaseConfig.isSqlDisplayed() || databaseConfig.isNPlusOneSelectDetected()) {
            sqlExecutions = sqlExecutionsRecorder.findRecord(null);
        }

        if (databaseConfig.isSelectedColumnsDisplayed()) {
            SelectStatsListener selectStatsListener =
                    SqlRecorderRegistry.INSTANCE.getSqlRecorderOfType(SelectStatsListener.class);
            String selectedColumns = selectStatsListener.getColumnsByTableBySchemaAsString();
            infoReport.append(lineSeparator());
            infoReport.append("* SELECTED COLUMNS");
            infoReport.append(lineSeparator());
            infoReport.append(selectedColumns);
        }

        if (databaseConfig.isSqlDisplayed()) {
            infoReport.append(lineSeparator());
            infoReport.append("* SQL");
            infoReport.append(lineSeparator());
            String sqlExecutionsAsString = sqlExecutions.toString();
            String sqlExecutionsWithoutThreeLastLineBreaks = sqlExecutionsAsString.substring(0, sqlExecutionsAsString.length() - 3);
            infoReport.append(sqlExecutionsWithoutThreeLastLineBreaks);
        }

        if (databaseConfig.isDatabaseConnectionProfiled()) {
            DiagnosticConnectionProfiler diagnosticConnectionProfiler =
                    ConnectionListenerRegistry.INSTANCE.getConnectionListenerOfType(DiagnosticConnectionProfiler.class);
            diagnosticConnectionProfiler.stop();

            infoReport.append(lineSeparator());
            String profilingConnectionResult = diagnosticConnectionProfiler.getProfilingResult();
            infoReport.append("* DATABASE CONNECTION PROFILING");
            infoReport.append(lineSeparator());
            infoReport.append(profilingConnectionResult);
        }

        //printHttpCall(httpServletRequest, httpServletResponse, warnLogMessage);

        List<HttpCall> externalHttpCalls = SynchronousHttpCallsRegistry.INSTANCE.getHttpCalls();

        if (databaseConfig.isSqlExecutionTimeDetected()) {
            int sqlExecutionThresholdInMilliseconds = databaseConfig.getSqlExecutionTimeThresholdInMilliseconds();
            LongDbRequestsListener longDbRequestsListener = SqlRecorderRegistry.INSTANCE.getSqlRecorderOfType(LongDbRequestsListener.class);
            SqlExecutions sqlExecutionsGreaterOrEqualToThreshold = longDbRequestsListener.getSqlExecutionsGreaterOrEqualToThreshold();
            if (!sqlExecutionsGreaterOrEqualToThreshold.isEmpty()) {
                warnLogMessage.append(lineSeparator() + "\t* [WARNING] At least one SQL query has an execution time greater than " + sqlExecutionThresholdInMilliseconds + " ms");
                String longQueriesAsString = sqlExecutionsGreaterOrEqualToThreshold.toString();
                String longQueriesAsStringWithoutThreeLastLineBreaks = longQueriesAsString.substring(0, longQueriesAsString.length() - 3);
                warnLogMessage.append(lineSeparator() + longQueriesAsStringWithoutThreeLastLineBreaks);
            }
        }

        if (databaseConfig.isNPlusOneSelectDetected()) {

            SelectAnalysis selectAnalysis = SelectAnalysisExtractor.INSTANCE.extractPerfMeasureFrom(sqlExecutions);

            if (selectAnalysis.getSameSelectTypesWithDifferentParamValues().evaluate()) {
                Long selectNumber = selectAnalysis.getSelectNumber().getValue();
                if (selectNumber.shortValue() >= databaseConfig.getNPlusOneSelectDetectionThreshold()) {
                    warnLogMessage.append(lineSeparator() + "\t* [WARNING] N+1 select suspicion" + " - " + selectNumber + " SELECT");
                }
            }

        }

        detectPerfAntiPatterns(externalHttpCalls, warnLogMessage);

        String contentType = httpServletResponse.getContentType();
        HttpContentType httpContentType = new HttpContentType(contentType);

        // Filter HTTP content type as soon as possible
        if (   testGenerationConfig.isTestGenerationEnabled()
           && (httpContentType.isJson() || httpContentType.isText() || httpContentType.isHtml())
        ) {

            SelectListener selectListener = SqlRecorderRegistry.INSTANCE.getSqlRecorderOfType(SelectListener.class);

            CopyHttpServletResponse copyHttpServletResponse = (CopyHttpServletResponse) httpServletResponse;
            String content = copyHttpServletResponse.extractContentAsString();
            List<QueryInfo> selectQueries = selectListener.getSelectQueries();

            String relativeHttUrl = HttpUrlRetriever.INSTANCE.findRelativeUrlFrom(httpServletRequest);

            Application application = Application.from(context);

            String httpCallReport = HttpResponseReportRetriever.INSTANCE.findHttpCallReport(httpServletRequest, httpServletResponse);

            if (testGenerationConfig.isJunit5GenerationEnabled()) {
                TestGenerator.INSTANCE.generateJUnitTestForGet(selectQueries
                                                             , relativeHttUrl
                                                             , application
                                                             , contentType
                                                             , content
                                                             , testGenerationConfig
                                                             , httpCallReport
                                                             , sqlTestDataGenerator
                                                             , JUnitVersion.VERSION_5
                );

            }

            if (testGenerationConfig.isJunit4GenerationEnabled()) {
                TestGenerator.INSTANCE.generateJUnitTestForGet(selectQueries
                                                             , relativeHttUrl
                                                             , application
                                                             , contentType
                                                             , content
                                                             , testGenerationConfig
                                                             , httpCallReport
                                                             , sqlTestDataGenerator
                                                             , JUnitVersion.VERSION_4
                );

            }

        }

        unregisterListeners();

        String warnLogMessageAsString = warnLogMessage.toString();
        if (!warnLogMessageAsString.isEmpty()) {
            appendHttpCallAtFirstPosition(httpServletRequest, httpServletResponse, warnLogMessage);
            String warningAsString = warnLogMessage.toString();
            writeWarning(warningAsString);
        }

        if (!infoReport.toString().isEmpty()) {
            appendHttpCallAtFirstPosition(httpServletRequest, httpServletResponse, infoReport);
            for (QuickPerfHttpCallInfoWriter quickPerfHttpCallInfoWriter : quickPerfHttpCallInfoWriters) {
                quickPerfHttpCallInfoWriter.write(infoReport.toString());
            }
        }

    }

    private NumberFormat buildNumberFormatWithGrouping() {
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        return numberFormat;
    }

    private void writeWarning(String warningAsString) throws Exception {
        for (QuickPerfHttpCallWarningWriter quickPerfHttpCallWarningWriter : quickPerfHttpCallWarningWriters) {
            try (QuickPerfHttpCallWarningWriter writerToClose = quickPerfHttpCallWarningWriter) {
                writerToClose.write(warningAsString);
            }
        }
    }

    private void detectPerfAntiPatterns(List<org.quickperf.web.spring.HttpCall> externalHttpCalls, StringBuilder warnLogMessage) {
        Deque<PerfEvent> perfEvents = PerfEventsRegistry.INSTANCE.getPerfEvents();

        Deque<PerfEvent> httpCallBetweenConnectionGetAndClosePattern = new ArrayDeque<>();
        httpCallBetweenConnectionGetAndClosePattern.add(PerfEvent.GET_DB_CONNECTION);
        httpCallBetweenConnectionGetAndClosePattern.add(PerfEvent.SYNCHRONOUS_HTTP_CALL);
        httpCallBetweenConnectionGetAndClosePattern.add(PerfEvent.CLOSE_DB_CONNECTION);

        Deque<PerfEvent> httpCallBetweenConnectionGetAndCommitPattern = new ArrayDeque<>();
        httpCallBetweenConnectionGetAndCommitPattern.add(PerfEvent.GET_DB_CONNECTION);
        httpCallBetweenConnectionGetAndCommitPattern.add(PerfEvent.SYNCHRONOUS_HTTP_CALL);
        httpCallBetweenConnectionGetAndCommitPattern.add(PerfEvent.DB_COMMIT);

        while (!perfEvents.isEmpty()) {
            PerfEvent perfEvent = perfEvents.poll();
            processPattern(httpCallBetweenConnectionGetAndClosePattern, perfEvent);
            processPattern(httpCallBetweenConnectionGetAndCommitPattern, perfEvent);
        }

        if (databaseHttpConfig.isSynchronousHttpCallBetweenDbConnectionGottenAndClosedDetected()
                && httpCallBetweenConnectionGetAndClosePattern.isEmpty()) {
            warnLogMessage.append(lineSeparator() + "\t* [WARNING] Synchronous HTTP call while the application maintains the DB connection (between the time the DB connection is gotten from the data source and closed)");
        }

        if (httpCallBetweenConnectionGetAndClosePattern.isEmpty()
         || httpCallBetweenConnectionGetAndCommitPattern.isEmpty()) {
            warnLogMessage.append(lineSeparator() + "\t* Synchronous HTTP calls");
            for (HttpCall httpCall : externalHttpCalls) {
                warnLogMessage.append(lineSeparator() + "\t\t* " + httpCall);
            }
        }

    }

    private void processPattern(Deque<PerfEvent> pattern, PerfEvent perfEvent) {
        if (perfEvent.equals(pattern.peek())) {
            pattern.poll();
        }
    }

}
