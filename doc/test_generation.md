# Test generation

Today, for GET HTTP calls done with a Spring RestTemplate, the project allows **generating JUnit 4 and JUnit 5 tests**:
1) **Reproducing N+1 select with a non-regression on N+1 select** thanks to the [**QuickPerf testing library**](https://github.com/quick-perf/quickperf).

2) **Ensuring a non-regression on the functional behavior.**
   It works for both  HTML or JSON response. The project uses the [JSONassert library](https://github.com/skyscreamer/JSONassert) to compare the current JSON response with the expected one.

The generated tests execute an SQL file produced with the help of [**SQL test data generator library**](https://github.com/quick-perf/sql-test-data-generator#sql-test-data-generator).

```
INFO  QuickPerfHttpCallHttpCallInfoLogger - 
GET 200 http://localhost:9966/petclinic/api/owners
* TEST GENERATION
	* JUnit 5 test class: .\src\test\java\ApiOwnersTest.java
	* SQL script file: .\src\test\resources\api-owners.sql
	* Expected response: .\src\test\resources\api-owners-expected.json
```

## How the test generation works


The schema below show how the test generation works.




_QuickPerf live_ intercepts the SQL queries executed from an HTTP call. They are used together the [Quick SQL test data](https://github.com/quick-perf/quick-sql-test-data) library to generate an SQL file.

_QuickPerf live_ generates an expected response file (JSON, HTML or text) from the HTTP response.

QuickPerf generates a test Java class verifying the functional behavior of the HTTP method call and the absence of N+1 select. Firstly, the test loads the previously generated SQL file and executes the SQL statements. Then, the test performs the HTTP call. Finally, the test compares the content of the response to an expected response, loaded from the previously generated expected response file.

:mag_right: Test generation log example



## How to configure the test generation

### Configure the generation folders

💡 ***Tip*** If you use the library in your local environment, configure the generation in ```src/test/java``` and ```src/test/resources```. So that, you can execute the generated tests in your IDE just after the generation!

:wrench: _.properties_ configuration example
```properties
quickperf.test-generation.java-folder-path=.\\src\\test\\resources
quickperf.test-generation.resource-folder-path=.\\src\\test\\java
```

:wrench: YAML configuration example
```yaml
quickperf:
  test-generation:
    java-folder-path: ".\\src\\test\\java"
    resource-folder-path: ".\\src\\test\\resources"
```

:wrench: MBean configuration
```
QuickPerf
  -- Test generation
      -- Operations
           -- String getJavaClassFolder()
           -- void setJavaClassFolder(String)
           -- String getTestResourceFolder()
           -- void setTestResourceFolder(String)
```

### Add dependencies to make the generated tests compile

To compile, you need to add the following dependencies:
* For REST API
```xml
<dependency>
  <groupId>org.skyscreamer</groupId>
  <artifactId>jsonassert</artifactId>
  <version>1.5.0</version>
  <scope>test</scope>
</dependency>
```

* For JUnit 5
```xml
<dependency>
  <groupId>org.quickperf</groupId>
  <artifactId>quick-perf-junit5</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
```

* For JUnit 4 & Spring 4
```xml
<dependency>
  <groupId>org.quickperf</groupId>
  <artifactId>quick-perf-junit4-spring4</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
```
* For JUnit 4 & Spring 5
```xml
<dependency>
  <groupId>org.quickperf</groupId>
  <artifactId>quick-perf-junit4-spring5</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
```

### Enable test generation for JUnit 4

:wrench: _.properties_ configuration
```properties
quickperf.test-generation.junit4.enabled=true
```

:wrench: YAML configuration
```yaml
quickperf:
  test-generation:
    junit4:
      enabled: true
```

:wrench: MBean configuration
```
QuickPerf
  -- Test generation
      -- Operations
           -- boolean isJUnit4GenerationEnabled()
           -- void setJUnit4GenerationEnabled(true)
```

### Enable the test generation for JUnit 5

:wrench: _.properties_ configuration example
```properties
quickperf.test-generation.junit5.enabled=true
```

:wrench: YAML configuration
```yaml
quickperf:
  test-generation:
    junit5:
      enabled: true
```

:wrench: MBean configuration
```
QuickPerf
  -- Test generation
      -- Operations
           -- boolean isJUnit5GenerationEnabled()
           -- void setJUnit5GenerationEnabled(true)
```

