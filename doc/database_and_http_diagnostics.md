# Database and HTTP diagnostics

## Synchronous HTTP call while the application maintains the database connection

If a synchronous HTTP call happens when the application is maintaining the database connection, the retention time of the connection will increase. Therefore, you may increase the connection pool size to allow the applications to manage many concurrent users.
However, an important pool size has a negative impact on performance ([HikariCP documentation](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing),
[Database Connections: Less is More](https://kwahome.medium.com/database-connections-less-is-more-86c406b6fad)).
So, synchronous HTTP calls happening when the application retains the database connection can decrease performance. Therefore, it is a performance antipattern.

Hikari CP provides a ```leakDetectionThreshold``` property allowing to log an alert if a connection is out of the pool for too long. 
The antipattern mentioned above may cause this.

_QuickPerf live_ considers the application maintains the database connection between the calls to ```javax.sql.DataSource. getConnection()``` (or ```javax.sql.DataSource. getConnection(String username,
String password)```) and ```java.sql.Connection.close()``` methods.

:wrench: _.properties_ configuration example
```
quickperf.synchronous-http-call.while-db-connection-maintained.detected=true
```

:wrench: YAML configuration example

```yaml
quickperf:
    enabled: true
    synchronous-http-call:
      while-db-connection-maintained:
            detected: true
```

:wrench: MBean configuration
```
QuickPerf
  -- Test generation
      -- Operations
           -- void  void setSynchronousHttpCallWhileDbConnectionMaintainedDetected(boolean)
           -- boolean isSynchronousHttpCallWhileDbConnectionMaintainedDetected()
```

:mag_right: Log example

```
2021-10-27 11:16:35.945  WARN 6020 --- [nio-8080-exec-8] s.QuickPerfHttpCallHttpCallWarningLogger : 
GET 200 http://localhost:8080/vets.html
	* [WARNING] Synchronous HTTP call while the application maintains the DB connection (between the time the DB connection is gotten from the data source and closed)
	* Synchronous HTTP calls
		* GET 200 http://localhost:8080/external-call- Execution time: 306 ms
```
