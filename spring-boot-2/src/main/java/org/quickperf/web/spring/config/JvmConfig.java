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
package org.quickperf.web.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(
        objectName="QuickPerf:category=JVM",
        description="QuickPerf Live MBeans")
public class JvmConfig {

    @Value("${quickperf.jvm.heap-allocation.measured:false}")
    boolean heapAllocationMeasured;

    @Value("${quickperf.jvm.heap-allocation.threshold.detected:false}")
    boolean heapAllocationThresholdDetected;

    @Value("${quickperf.jvm.heap-allocation.threshold.value-in-bytes:10 000 000}")
    int heapAllocationThresholdValueInBytes;

    @ManagedAttribute
    public boolean isHeapAllocationThresholdDetected() {
        return heapAllocationThresholdDetected;
    }

    @ManagedOperation
    public void setHeapAllocationThresholdDetected(boolean heapAllocationThresholdDetected) {
        this.heapAllocationThresholdDetected = heapAllocationThresholdDetected;
    }

    @ManagedAttribute
    public int getHeapAllocationThresholdValueInBytes() {
        return heapAllocationThresholdValueInBytes;
    }

    @ManagedOperation
    public void setHeapAllocationThresholdValueInBytes(int heapAllocationThresholdValueInBytes) {
        this.heapAllocationThresholdValueInBytes = heapAllocationThresholdValueInBytes;
    }

    @ManagedAttribute
    public boolean isHeapAllocationMeasured() {
        return heapAllocationMeasured;
    }

    @ManagedOperation
    public void setHeapAllocationMeasured(boolean heapAllocationMeasured) {
        this.heapAllocationMeasured = heapAllocationMeasured;
    }

}
