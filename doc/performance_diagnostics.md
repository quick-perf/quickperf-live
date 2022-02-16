# Performance diagnostics


_QuickPerf live_ provides you with information on properties that impact performance.

The information is displayed at INFO or WARNING levels and grouped by HTTP call.

You can see below an example of diagnostic with a WARNING level:
```
2021-10-18 19:27:18.903  WARN 21632 --- [nio-8080-exec-1] s.QuickPerfHttpCallHttpCallWarningLogger : 
GET 200 http://localhost/page.html
	* [WARNING] At least one SQL query has an execution time greater than 30 ms
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

The following example displays information at INFO level: 
```
2021-10-27 11:02:23.500  INFO 6020 --- [nio-8080-exec-3] .w.s.QuickPerfHttpCallHttpCallInfoLogger :
GET 200 http://localhost:8080/owners/2
* HEAP ALLOCATION: 1 316 984 bytes
```

:bulb:
You can display the diagnostic data with logs or implement a custom export to a file, database, or storage. To do this, create Spring beans implementing ```QuickPerfHttpCallInfoWriter``` or ```QuickPerfHttpCallWarningWriter``` interface.

The tool supplies different types of diagnostics:

:point_right: [**JVM diagnostics**](./jvm_diagnostics.md)

:point_right: [**Database diagnostics**](./database_diagnostics.md)

:point_right: [**Database and HTTP diagnostics**](./database_and_http_diagnostics.md)
