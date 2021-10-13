# QuickPerf live

<div>
<blockquote>
    <p><strong>A tool to ease your work as a developer</strong>, not only regarding performance.
        <br/><em><strong>It generates functional non-regression tests, diagnoses performance, and generates non-regression tests on performance-related properties.</em></strong></p>
</blockquote>
</div>

---

<p>
    <a  href="https://www.youtube.com/watch?v=4Sbvaewrm6A&t=913s">
    <strong>📺 QuickPerf live demo</strong> (in French)
    </a>
    &nbsp;&nbsp;
   <a href="https://twitter.com/quickperf">       
        <img alt="@QuickPerf" src="https://img.shields.io/twitter/url?label=Twitter&style=social&url=https%3A%2F%2Ftwitter.com%2Fquickperf">
   </a>
    &nbsp;&nbsp;
    <a href="https://github.com/quick-perf/quickperf-live/blob/master/LICENSE.txt">
        <img src="https://img.shields.io/badge/license-Apache2-blue.svg"
             alt = "License">
    </a>
    &nbsp;&nbsp;
    <a href="https://github.com/quick-perf/quickperf-live/actions?query=workflow%3ACI">
    <img src="https://img.shields.io/github/workflow/status/quick-perf/quickperf-live/CI"
         alt = "Build Status">
    </a>
</p>

---

_QuickPerf live_ works today for web applications developed with Spring Boot 2.

⚠ _We do not recommend enabling the tool in a production environment today._

## Why use _QuickPerf live_?

### Diagnose performance-related properties
Examples: 
* Detect long database queries 
* Detect high heap allocation
* Detect n+1 select 
* Detect a synchronous HTTP call between the time the DB connection is gotten from the data source and closed
* ...

:bulb: *You can use this feature during development or for a performance diagnostic audit of the application.*

###  Automatically generate QuickPerf and functional non-regression tests

Today, for GET HTTP calls done with a Spring RestTemplate, the project allows **generating JUnit 4 and JUnit 5 tests**:
1) **Reproducing N+1 select with a non-regression on N+1 select** thanks to the [**QuickPerf testing library**](https://github.com/quick-perf/quickperf).

2) **Ensuring a non-regression on the functional behavior.**
It works for both  HTML or JSON response. The project uses the [JSONassert library](https://github.com/skyscreamer/JSONassert) to compare the current JSON response with the expected one.

The generated tests execute a SQL file produced with the help of [**SQL test data generator library**](https://github.com/quick-perf/sql-test-data-generator#sql-test-data-generator). 

## Configuration
### Maven dependency

```xml
<dependency>
    <groupId>org.quickperf</groupId>
    <artifactId>quick-perf-live-springboot2</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```
Maven central contains a snapshot version. To use it, your projects need to access to Maven central snapshots:
```xml
    <repositories>
        <repository>
            <id>maven-snapshots</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            <layout>default</layout>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
```

You can also build the library:
```bash
git clone https://github.com/quick-perf/quickperf-live.git
```
```bash
mvn clean install
```

### Enable QuickPerf live

From a _yaml_ file:

```yaml
quickperf:
  enabled: true
```

From a _.properties_ file:

```properties
quickperf.enabled=true
```

### Export the diagnostic of performance-related properties
To have the results on application logs:
```properties
logging.level.org.quickperf.*=INFO
```
You also have the possibility to implement custom exports.
To do this, create Spring beans implementing ```QuickPerfHttpCallInfoWriter``` and ```QuickPerfHttpCallWarningWriter``` interfaces.

### Configure the diagnostic of performance-related properties and the test generation
The configuration can be done from properties.

:point_right: [A _yaml_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.yml)

:point_right: [A _.properties_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.properties)

💡 ***Test generation tip*** If you use the library in your local environment, configure the generation in ```src/test/java``` and ```src/test/resources```. So that, you can execute the generated tests in your IDE just after the generation!
```yaml
quickperf:
    test-generation:
        java-folder-path: ".\\src\\test\\java"
        resource-folder-path: ".\\src\\test\\resources"
```
The configuration can be updated after the application startup with MBeans:

![](./doc/MBeans.JPG)

💡 QuickPerf MBeans are unavailable? Read [this](./doc/faq.md).
