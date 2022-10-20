# Frequently Asked Questions

## QuickPerf MBeans are unavailable
[From Spring Boot 2.2, JMX is disabled by default](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.2-Release-Notes#jmx-now-disabled-by-default). You can enable it:
* From a _JVM property_ 
```
-Dspring.jmx.enabled=true
```
* From a _.properties file_ 
```
spring.jmx.enabled=true
```
* From a _YAML file_
 ```yaml
 spring:
    jmx:
        enabled: true
  ```


