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


## How to exclude some urls from QuickPerf diagnosis
It is possible to exclude some urls like this :

* From a _.properties file_
```
quickperf.exclude-urls=/manage,/actuator
```
* From a _YAML file_
 ```yaml
 quickperf:
   exclude-urls: /manage,/actuator
  ```
urls should be comma seperated.
By default, /actuator is excluded but if you want to add new urls, you should add /actuator path as mentioned above.
