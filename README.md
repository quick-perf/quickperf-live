# QuickPerf live

<strong>A tool to ease your work as a developer</strong>, not only regarding performance. <em>Make your developer job easier.</em></strong>

---
<p>
    <a  href="https://www.youtube.com/watch?v=4Sbvaewrm6A&t=913s">
    <strong>📺 <em>QuickPerf live</em> demo</strong> (in French)
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

## Main features

### Diagnose performance-related properties
```
GET 200 http://localhost:8080/owners?lastName=
* [WARNING] N+1 select suspicion - 453 SELECT
```

```
GET 200 http://localhost:8080/owners/2/edit
* [WARNING] Heap allocation is greater than 10 000 000 bytes: 17 509 888 bytes
```
👉 [Learn more](./doc/performance_diagnostics.md)

💡 You can use the performance diagnostic features during development or to help you diagnose the performance of a deployed application.

### Generate non-regression tests

The generated tests ensure a non-regression on:
* ***functional behavior***
* ***performance-related properties***

👉 [Learn more](./doc/test_generation.md)

## Some use cases

You have many things to do or think about in your daily job.
You may not have enough time to improve the following issues that would ease your daily job or improve the user experience. _QuickPerf live_ could help you!

### You work on an application lacking automated tests on business behavior
Are you not confident that your new development will not break another feature? _QuickPerf live_ allows you to generate automated tests!
More [here](./doc/test_generation.md).

### You want to fix an N+1 select
_QuickPerf live_ can generate an automatic test reproducing the N+1 select. You can use it to investigate the cause of the N+1 select. The produced test will also ensure non-regression on the absence of N+1 select as well as on the functional behavior.

### Your application allocates a lot
_QuickPerf live_ allows you to measure the heap allocation generated by each HTTP call.

### You want to know how a web service handles the business data
Finding out what types of business data a web service handles may be challenging.
Sometimes, as a developer, we may also be tempted to load more data than necessary to implement a business behavior, potentially impacting performance. In both cases, _QuickPerf live_ can help you by displaying the selected columns from the database (cf. *Selected columns* part in [Database diagnostics](./doc/database_diagnostics.md)), or  [creating the database dataset
of a generated web service test](./doc/test_generation.md).


## Add _QuickPerf live_ to your project 

### Current limitations
_QuickPerf live_ works today for web applications developed with Spring Boot 2.

⚠ _Today, we don't recommend enabling the tool in a production environment._

### Add _QuickPerf live_ dependency
```xml
<dependency>
    <groupId>org.quickperf</groupId>
    <artifactId>quick-perf-live-springboot2</artifactId>
    <version>0.1</version>
</dependency>
```

### Enable _QuickPerf live_

From a _yaml_ file:

```yaml
quickperf:
  enabled: true
```

From a _.properties_ file:

```
quickperf.enabled=true
```

## Enable _QuickPerf live_ features

### Enable the performance diagnostic and test generation features

You can configure the performance diagnostics and the test generation with properties.
You can also update the configuration after the application startup with the help of MBeans.

👉 [**Performance diagnostics**](./doc/performance_diagnostics.md)

👉 [**Test generation**](./doc/test_generation.md)

### Tip
QuickPerf MBeans are unavailable? Read [this](./doc/faq.md).

### Configuration examples

👉 [A _yaml_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.yml)

👉 [A _.properties_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.properties)


## Export _QuickPerf live_ information

### Display info on application logs

```properties
logging.level.org.quickperf.*=INFO
```

### Implement custom exports
Create Spring beans implementing ```QuickPerfHttpCallInfoWriter``` or ```QuickPerfHttpCallWarningWriter``` interfaces.
You can use this feature to generate a performance diagnostic report or record the history of performance-related properties.
