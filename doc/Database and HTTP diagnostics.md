# Database and HTTP diagnostics

## Synchronous HTTP call while the application maintains the database connection

A synchronous HTTP happening during the time the application maintains the database connection will increase this time. You may then be tempted to increase the size of the connection pool to allow the
applications to manage many concurrent users.
However, an important pool size has a negative impact on performance ([HikariCP documentation](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing),
[Database Connections: Less is More](https://kwahome.medium.com/database-connections-less-is-more-86c406b6fad)).
So, synchronous HTTP calls happening during the time the application retain the database connection can decrease performance. It is a performance antipattern.

[Hikari CP provides a ```leakDetectionThreshold``` property allowing to log an alert if a connection is out of the pool for too long. 
The antipattern mentioned above may cause this.

```QuickPerf live```  considers the application maintains the database connection between the calls to ```javax.sql.DataSource. getConnection()``` (or ```javax.sql.DataSource. getConnection(String username,
String password)```) and ```java.sql.Connection.close()```.

:wrench: _.properties_ configuration example
```properties
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
	Time:158, Success:True, Type:Prepared, Batch:False, QuerySize:1, BatchSize:0, Query:["
    select
        distinct owner0_.id as id1_0_0_,
        pets1_.id as id1_1_1_,
        owner0_.first_name as first_na2_0_0_,
        owner0_.last_name as last_nam3_0_0_,
        owner0_.address as address4_0_0_,
        owner0_.city as city5_0_0_,
        owner0_.telephone as telephon6_0_0_,
        pets1_.name as name2_1_1_,
        pets1_.birth_date as birth_da3_1_1_,
        pets1_.owner_id as owner_id4_1_1_,
        pets1_.type_id as type_id5_1_1_,
        pets1_.owner_id as owner_id4_1_0__,
        pets1_.id as id1_1_0__ 
    from
        owners owner0_ 
    left outer join
        pets pets1_ 
            on owner0_.id=pets1_.owner_id 
    where
        owner0_.last_name like ?"], Params:[(%)]
	* [WARNING] Synchronous HTTP call while the application maintains the DB connection (between the time the DB connection is gotten from the data source and closed)
	* Synchronous HTTP calls
		* GET 200 http://localhost:8080/external-call- Execution time: 306 ms
```