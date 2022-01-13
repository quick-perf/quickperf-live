# Database diagnostics

## N+1 select detection
To spot N+1 select, _QuickPerf live_ verifies if the application emits several times the same type
of select statement with different parameter values. The same select type executed in a loop will also result in a positive detection.

The _quickperf.database.n+1.detected_ property allows to enable the detection.
_quickperf.database.n+1.threshold_ properties is optional. If the number of generated SELECT statements is less than the configured threshold, 
when no warning will be raised.

:wrench: _.properties_ configuration example

```properties
quickperf.database.n+1.detected=true
quickperf.database.n+1.threshold=20
```
:wrench: YAML configuration example

```yaml
quickperf:
  database:
    n+1:
      detected: true
      threshold: 10
```

:wrench: MBean configuration
```
QuickPerf
  -- Database
      -- Operations
           -- boolean isNPlusOneSelectDetected()
           -- void setNPlusOneSelectDetected(boolean)
           -- int getNPlusOneDetectionThreshold()
           -- void setNPlusOneDetectionThreshold(int)
```

:mag_right: Log example

```
2021-10-18 19:27:18.903  WARN 21632 --- [nio-8080-exec-1] s.QuickPerfHttpCallHttpCallWarningLogger : 
GET 200 http://localhost:8080/owners?lastName=
* [WARNING] N+1 select suspicion - 453 SELECT]
```

## Database connection profiling

:wrench: _.properties_ configuration

```properties
quickperf.database.sql.displayed=true
```
:wrench: YAML configuration

```yaml
quickperf:
    database:
        sql:
            displayed: true
```
:wrench: MBean configuration
```
QuickPerf
  -- Database
      -- Operations
           -- boolean isDatabaseConnectionProfiled()
           -- void setDatabaseConnectionProfiled(boolean) 
```

:mag_right: Log example

```
2021-10-27 14:51:48.595  INFO 6020 --- [nio-8080-exec-7] .w.s.QuickPerfHttpCallHttpCallInfoLogger : 
GET 200 http://localhost:8080/owners?lastName=
* DATABASE CONNECTION PROFILING
connection 905860618 - javax.sql.DataSource.getConnection()
	com.zaxxer.hikari.HikariDataSource$$EnhancerBySpringCGLIB$$830fe3d5.getConnection(<generated>)
	org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:122)
	org.hibernate.internal.NonContextualJdbcConnectionAccess.obtainConnection(NonContextualJdbcConnectionAccess.java:38)
	org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.acquireConnectionIfNeeded(LogicalConnectionManagedImpl.java:108)
```

```
connection 905860618 - java.sql.Connection.setReadOnly(boolean readOnly) [readOnly: true]
	org.springframework.jdbc.datasource.DataSourceUtils.prepareConnectionForTransaction(DataSourceUtils.java:188)
	org.springframework.orm.jpa.vendor.HibernateJpaDialect.beginTransaction(HibernateJpaDialect.java:153)
	org.springframework.orm.jpa.JpaTransactionManager.doBegin(JpaTransactionManager.java:421)
	org.springframework.transaction.support.AbstractPlatformTransactionManager.startTransaction(AbstractPlatformTransactionManager.java:400)
```

```
connection 905860618 - java.sql.Connection.setAutoCommit(boolean autoCommit) [autoCommit: false]
	org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.begin(AbstractLogicalConnectionImplementor.java:72)
	org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.begin(LogicalConnectionManagedImpl.java:283)
	org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.begin(JdbcResourceLocalTransactionCoordinatorImpl.java:246)
	org.hibernate.engine.transaction.internal.TransactionImpl.begin(TransactionImpl.java:83)
```

```
connection 905860618 - java.sql.Connection.commit() [isolation: transaction_read_committed]
	org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.commit(AbstractLogicalConnectionImplementor.java:86)
	org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.commit(JdbcResourceLocalTransactionCoordinatorImpl.java:282)
	org.hibernate.engine.transaction.internal.TransactionImpl.commit(TransactionImpl.java:101)
	org.springframework.orm.jpa.JpaTransactionManager.doCommit(JpaTransactionManager.java:562)
```
	
```
connection 905860618 - java.sql.Connection.setAutoCommit(boolean autoCommit) [autoCommit: true]
	org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.resetConnection(AbstractLogicalConnectionImplementor.java:106)
	org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.afterCompletion(LogicalConnectionManagedImpl.java:288)
	org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.commit(AbstractLogicalConnectionImplementor.java:95)
	org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.commit(JdbcResourceLocalTransactionCoordinatorImpl.java:282)
```

```	
connection 905860618 - java.sql.Connection.setReadOnly(boolean readOnly) [readOnly: false]
	org.springframework.jdbc.datasource.DataSourceUtils.resetConnectionAfterTransaction(DataSourceUtils.java:252)
	org.springframework.orm.jpa.vendor.HibernateJpaDialect$SessionTransactionData.resetSessionState(HibernateJpaDialect.java:371)
	org.springframework.orm.jpa.vendor.HibernateJpaDialect.cleanupTransaction(HibernateJpaDialect.java:214)
	org.springframework.orm.jpa.JpaTransactionManager.doCleanupAfterCompletion(JpaTransactionManager.java:642)
```

```	
connection 905860618 - java.sql.Connection.close()
	org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.closeConnection(DatasourceConnectionProviderImpl.java:127)
	org.hibernate.internal.NonContextualJdbcConnectionAccess.releaseConnection(NonContextualJdbcConnectionAccess.java:49)
	org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.releaseConnection(LogicalConnectionManagedImpl.java:217)
	org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.close(LogicalConnectionManagedImpl.java:259)
```

## SQL query

### Display

:wrench: _.properties_ configuration

```properties
quickperf.database.sql.displayed=true
```
:wrench: YAML configuration

```yaml
quickperf:
    database:
        sql:
            displayed: true
```
:wrench: MBean configuration
```
QuickPerf
  -- Database
      -- Operations
           -- boolean isSqlDisplayed()
           -- void setSqlDisplayed(boolean) 
```

:mag_right: Log example

```
2021-10-14 20:35:07.849  INFO 5068 --- [nio-8080-exec-3] .w.s.QuickPerfHttpCallHttpCallInfoLogger :
GET 200 http://localhost:8080/owners?lastName=
* SQL
  Time:25, Success:True, Type:Prepared, Batch:False, QuerySize:1, BatchSize:0, Query:["
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

  Time:9, Success:True, Type:Prepared, Batch:False, QuerySize:1, BatchSize:0, Query:["
  select
  pettype0_.id as id1_3_0_,
  pettype0_.name as name2_3_0_
  from
  types pettype0_
  where
  pettype0_.id=?"], Params:[(1)]
```

### Selected columns

:wrench: _.properties_ configuration

```properties
quickperf.database.sql.displayed.selected-columns=true
```

:wrench: YAML configuration

```yaml
quickperf:
  database:
    sql:
      displayed.selected-columns: true
```

:wrench: MBean configuration
```
QuickPerf
  -- Database
      -- Operations
           -- void setSelectedColumnsDisplayed(boolean)
           -- boolean setSelectedColumnsDisplayed()
```

:mag_right: Log example
```
2021-10-14 20:19:57.504  INFO 5068 --- [nio-8080-exec-5] .w.s.QuickPerfHttpCallHttpCallInfoLogger :
GET 200 http://localhost:8080/owners?lastName=
* SELECTED COLUMNS
  ╔════════╤══════════════════════════════════════════════════════════╤════╗
  ║ Table  │ Selected columns                                         │ Nb ║
  ╠════════╪══════════════════════════════════════════════════════════╪════╣
  ║ TYPES  │ ID * NAME                                                │ 2  ║
  ╟────────┼──────────────────────────────────────────────────────────┼────╢
  ║ PETS   │ TYPE_ID * BIRTH_DATE * ID * NAME * OWNER_ID              │ 5  ║
  ╟────────┼──────────────────────────────────────────────────────────┼────╢
  ║ OWNERS │ TELEPHONE * CITY * ADDRESS * ID * LAST_NAME * FIRST_NAME │ 6  ║
  ╚════════╧══════════════════════════════════════════════════════════╧════╝
```

### Execution time
:wrench: _.properties_ configuration example

You have to configure two properties to spot long SQL queries.

The
```properties
quickperf.database.sql.execution-time.detected
```


```properties
quickperf.database.sql.execution-time.detected=true
#ping value included in execution time threshold
quickperf.database.sql.execution-time.thresholdInMs=50
```

:wrench: YAML configuration example

```yaml
quickperf:
      execution-time:
        detected: true
        thresholdInMs: 50
```

:wrench: MBean configuration
```
QuickPerf
  -- Database
      -- Operations
           -- boolean isSqlDisplayed()
           -- void setSqlExecutionTimeThresholdInMilliseconds(int) 
```

:mag_right: Log example
```
2021-10-18 19:27:18.903  WARN 21632 --- [nio-8080-exec-1] s.QuickPerfHttpCallHttpCallWarningLogger : 
GET 200 http://localhost:8080/owners?lastName=
	* [WARNING] At least one SQL query has an execution time greater than 50 ms
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
```