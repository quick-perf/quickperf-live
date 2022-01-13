# QuickPerf live

<strong>Make your developer job easier</strong>

<div>
<blockquote>
    <p><strong>A tool to ease your work as a developer</strong>, not only regarding performance.
        <br/><em><strong>It generates functional non-regression tests</strong> to help you to work on legacy applications<strong>, diagnoses performance, and generates non-regression tests on performance-related properties.</em></strong></p>
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

## Use cases

### Diagnose performance-related properties

:mag_right: Examples
// Add log screen

:bulb: *You can use these features during development or for a performance diagnostic audit of the application.*

### Generate tests

The generated test can check the functional behavior and performance-related properties.

// copie écran fichiers générés
// Bout de code fichier Java générés

// Why Prendre/reprendre contrôle application Etre confiant sur non reg fonctionnelle et 
// sur des propriétés de performance

// Infos outils rassemblés par appel HTTP

## Use cases

You have many things to do or to think in your daily job. 
You may have not enough time to improve the following issues that will ease 
your daily job or make the user experience better? _QuickPerf live_ may help you!

### You work on an application lacking automated tests on the functional behavior
You are not confident that your new development does not break another functional feature? You can generate automated tests with QuickPerf live!
More [here](./doc/Test generation.md).

### Your application is slow or allocates a lot of memory
You can audit performance-related properties and detect performance anti-patterns with QuickPerf live. You can generate non-regression tests on N+1 select.
More [here](./doc/Performance diagnostics.md).

### You want to know what business data is handled by a web service
Find out what types of business data a web service handles may be hard.
Sometimes, we, as a developer, we also may be tempted to load more data 
than necessary to implement a business behavior. This has sometimes an impact
on performance. In both cases, _QuickPerf live_ can help you by 
displaying the selected columns from the database (cf. *Selected columns* part in [Database diagnostics](./doc/Database diagnostics.md)), or by [creating the database dataset
of a generated web service test](./doc/Test generation.md).


###  Automatically generate QuickPerf and functional non-regression tests

## Add QuickPerf live dependency

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

## Enable QuickPerf live

From a _yaml_ file:

```yaml
quickperf:
  enabled: true
```

From a _.properties_ file:

```properties
quickperf.enabled=true
```

## Export QuickPerf live info
### Display info on application logs
```properties
logging.level.org.quickperf.*=INFO
```

### Implement custom exports
Create Spring beans implementing ```QuickPerfHttpCallInfoWriter``` and ```QuickPerfHttpCallWarningWriter``` interfaces.
This feature can be useful to generate a performance diagnostic report or to record the history of performance-related properties.

## Configure the diagnostic of performance-related properties and the test generation
You can configure the performance diagnostic and the test generation with properties.
You can update the configuration after the application startup with the help of MBeans.

💡 QuickPerf MBeans are unavailable? Read [this](./doc/faq.md).

:point_right: [Performance diagnostics](./doc/Performance diagnostics.md)

:point_right: [Test generation](./doc/Test generation.md)

:point_right: [A _yaml_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.yml)

:point_right: [A _.properties_ file example](./spring-boot-2/src/test/resources/quickperf-properties-test.properties)
