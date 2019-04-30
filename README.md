# pg-interval-test

This is a test application to try and find why a type PGInterval field will not deserialize.

### Problem statement:
When trying to implement a data model including a PGInterval type (https://github.com/pgjdbc/pgjdbc/blob/master/pgjdbc/src/main/java/org/postgresql/util/PGInterval.java), the program fails with a hibernate Serialization exception.  Why?


#### Database Creation:
-- Database: intervaltest

-- DROP DATABASE intervaltest;

CREATE DATABASE intervaltest
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE intervaltest
    IS 'Database to test interval deserialization issue
';

####Schema Creation:
-- SCHEMA: tester

-- DROP SCHEMA tester ;

CREATE SCHEMA tester
    AUTHORIZATION postgres;

####Table Creation:
-- Table: tester.tb_testinterval

-- DROP TABLE tester.tb_testinterval;

CREATE TABLE tester.tb_testinterval
(
    test_id integer NOT NULL,
    test_interval interval NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE tester.tb_testinterval
    OWNER to postgres;


####Table Data Insert:
INSERT INTO tester.tb_testinterval
VALUES (1,'01:00:00'),
       (2,'02:00:00'),
       (3,'03:00:00')


### Result with PGInterval field commented out in the Tester model:
[{"testId": 1},{"testId": 2},{"testId": 3}]

### Result with PGInterval field enabled in the Tester model:
{
"timestamp": "2019-04-30T15:47:24.565+0000",
"message": "could not deserialize; nested exception is org.hibernate.type.SerializationException: could not deserialize",
"details": "uri=/api/testrecords"
}

#### console output:
2019-04-30 11:49:10.899  INFO 38658 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-04-30 11:49:10.899  INFO 38658 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-04-30 11:49:10.899 DEBUG 38658 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Detected StandardServletMultipartResolver
2019-04-30 11:49:10.905 DEBUG 38658 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : enableLoggingRequestDetails='false': request parameters and headers will be masked to prevent unsafe logging of potentially sensitive data
2019-04-30 11:49:10.905  INFO 38658 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 6 ms
2019-04-30 11:49:10.912 DEBUG 38658 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : GET "/api/testrecords", parameters={}
2019-04-30 11:49:10.915 DEBUG 38658 --- [nio-8080-exec-1] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to public java.util.List<com.example.pgintervaltest.model.Tester> com.example.pgintervaltest.controller.TesterController.getAllCategories()
2019-04-30 11:49:10.956  INFO 38658 --- [nio-8080-exec-1] o.h.h.i.QueryTranslatorFactoryInitiator  : HHH000397: Using ASTQueryTranslatorFactory
2019-04-30 11:49:11.012 DEBUG 38658 --- [nio-8080-exec-1] org.hibernate.SQL                        : select tester0_.test_id as test_id1_0_, tester0_.test_interval as test_int2_0_ from tester.tb_testinterval tester0_
Hibernate: select tester0_.test_id as test_id1_0_, tester0_.test_interval as test_int2_0_ from tester.tb_testinterval tester0_
2019-04-30 11:49:11.033 DEBUG 38658 --- [nio-8080-exec-1] .m.m.a.ExceptionHandlerExceptionResolver : Using @ExceptionHandler public org.springframework.http.ResponseEntity<?> com.example.pgintervaltest.exception.GlobalExceptionHandler.globleExcpetionHandler(java.lang.Exception,org.springframework.web.context.request.WebRequest)
2019-04-30 11:49:11.067 DEBUG 38658 --- [nio-8080-exec-1] o.s.w.s.m.m.a.HttpEntityMethodProcessor  : Using 'application/json;q=0.8', given [text/html, application/xhtml+xml, image/webp, image/apng, application/signed-exchange;v=b3, application/xml;q=0.9, */*;q=0.8] and supported [application/json, application/*+json, application/json, application/*+json]
2019-04-30 11:49:11.068 DEBUG 38658 --- [nio-8080-exec-1] o.s.w.s.m.m.a.HttpEntityMethodProcessor  : Writing [com.example.pgintervaltest.exception.ErrorDetails@6b93eefd]
2019-04-30 11:49:11.082 DEBUG 38658 --- [nio-8080-exec-1] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.orm.jpa.JpaSystemException: could not deserialize; nested exception is org.hibernate.type.SerializationException: could not deserialize]
2019-04-30 11:49:11.082 DEBUG 38658 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed 500 INTERNAL_SERVER_ERROR


