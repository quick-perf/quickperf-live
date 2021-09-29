# QuickPerf _Live_
<a href="https://github.com/quick-perf/quickperf-live/blob/master/LICENSE.txt">
    <img src="https://img.shields.io/badge/license-Apache2-blue.svg"
         alt = "License">
</a>

_QuickPerf live_ works today for web applications developed with Spring Boot 2.

⚠ _We do not recommend enabling the tool in a production environment today._

## Why using _QuickPerf Live_?
### Evaluate and investigate performance-related properties
Examples: long database queries, heap allocation, n+1 select or the detection of a synchronous HTTP call between the time the DB connection is gotten from the data source and closed

_You can use this feature during development or for helping you to a do a performance diagnostic audit._

###  Automatically generate QuickPerf and functional non-regression tests

Today, for GET HTTP calls done with a Spring RestTemplate, the project allows generating tests:
1) Reproducing N+1 select with a non-regression on N+1 select thanks to the [**QuickPerf testing library**](https://github.com/quick-perf/quickperf/Readme.md).

2) Ensuring a non-regression on the functionnal behavior
It works for both an HTML or a JSON response. The project uses the [JSONassert library](https://github.com/skyscreamer/JSONassert) to compare the current JSON response with the expected one.

:bulb: The generated tests executes a SQL file produced with the help of [**SQL test data generator library**](https://github.com/quick-perf/sql-test-data-generator#sql-test-data-generator). 

## Configuration
### Maven dependency
The library is not deployed on Maven central yet.
To use it:
1) Clone the project
```bash
git clone https://github.com/quick-perf/quickperf-live.git
```
2) Build the library
```bash
mvn clean install
```
3) Add the library to your project
```xml
<dependency>
    <groupId>org.quickperf</groupId>
    <artifactId>quick-perf-live</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

### Enable QuickPerf Live
From a _.properties_ file:

```properties
quickperf.enabled=true
```

From a _yaml_ file:

```yaml
quickperf:
  enabled: true
```

### Export the diagnostic of performance-related properties
To have the results on application logs:
```properties
logging.level.org.quickperf.*=INFO
```
You have also the possibility to implement custom exports.
To do this, create Spring beans implementing ```QuickPerfHttpCallInfoWriter``` and ```QuickPerfHttpCallWarningWriter``` interfaces.

### Configure the diagnostic of performance-related properties and the test generation
The configuration can be done from properties.

:point_right: [A _.properties_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.properties)

:point_right: [A _yaml_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.yml)

💡 Test generation tip: if you use the library in your local environment, configure the generation in ```src/test/java``` and ```src/test/resources```. So that, you can execute the generated tests in your IDE just after the generation!
```yaml
quickperf:
    test-generation:
        java-folder-path: "C:\\Users\\MyUser\\IdeaProjects\\quickperf-live-demo\\src\\test\\java"
        resource-folder-path: "C:\\Users\\MyUser\\IdeaProjects\\quickperf-live-demo\\src\\test\\resources"
```

The configuration can be updated after the application startup with MBeans:

![](./doc/MBeans.JPG)

💡 QuickPerf MBeans are unavailable? Read [this](./doc/faq.md).
