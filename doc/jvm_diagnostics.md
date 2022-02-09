# JVM diagnostics

## Heap allocation measurement

:wrench: _.properties_ configuration example

```properties
quickperf.jvm.heap-allocation.measured=true
```

:wrench: YAML configuration example

```yaml
quickperf:
  jvm:
    heap-allocation:
      measured: true
```

:wrench: MBean configuration
```
QuickPerf
  -- JVM
      -- Operations
           -- boolean isHeapAllocationMeasured()
           -- void setHeapAllocationMeasured(boolean)
```

:mag_right: Log example

```
2021-10-27 11:02:23.500  INFO 6020 --- [nio-8080-exec-3] .w.s.QuickPerfHttpCallHttpCallInfoLogger : 
GET 200 http://localhost:8080/owners/2
* HEAP ALLOCATION: 1 316 984 bytes
```

## Heap allocation threshold 

:wrench: _.properties_ configuration example

```properties
quickperf.jvm.heap-allocation.threshold.detected=true
quickperf.jvm.heap-allocation.threshold.value-in-bytes=100 000
```

:wrench: YAML configuration example

```yaml
quickperf:
        heap-allocation:
            threshold:
                detected: true
                value-in-bytes: 100 000
```

:wrench: MBean configuration

```
QuickPerf
  -- JVM
      -- Operations
           -- boolean isHeapAllocationThresholdDetected()
           -- void setHeapAllocationThresholdDetected(boolean)
```

:mag_right: Log example

```
2021-10-27 11:06:31.309  WARN 6020 --- [nio-8080-exec-4] s.QuickPerfHttpCallHttpCallWarningLogger : 
GET 200 http://localhost:8080/owners/2/edit
	* [WARNING] Heap allocation is greater than 10 000 000 bytes: 17 509 888 bytes
```

