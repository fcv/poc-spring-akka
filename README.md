## About

Proof of Concept project which aims to intregrate Spring and Akka using both languages, Scala and Java.

## Build

Project is built using Maven. Example:

    $ mvn package

## Execute

System may be started up either by using Spring Boot's Maven plugin or by executing `java` command on package .war file. Example:

    $ mvn spring-boot:run

Or 

    $ java -jar ./target/poc-spring-akka-1.0.0-SNAPSHOT.war

System is started up using an embedded Tomcat 8 instance and may be reached thought http://localhost:8080/ address. Example:

    $ curl "localhost:8080/api/rest/v1/instant"
    {
      "provider": "poc-spring-akka-akka.actor.default-dispatcher-8",
      "trace": [
        {
          "className": "br.fcv.poc.web.JavaController",
          "instanceId": "1504716407",
          "threadName": "http-nio-8080-exec-10"
        },
        {
          "className": "br.fcv.poc.core.MyJavaActor",
          "instanceId": "1198113276",
          "threadName": "poc-spring-akka-akka.actor.default-dispatcher-8"
        },
        {
          "className": "br.fcv.poc.core.ClockServiceBean",
          "instanceId": "623022533",
          "threadName": "poc-spring-akka-akka.actor.default-dispatcher-8"
        },
        {
          "className": "br.fcv.poc.web.JavaController",
          "instanceId": "1504716407",
          "threadName": "poc-spring-akka-akka.actor.default-dispatcher-8"
        },
        {
          "className": "br.fcv.poc.web.SerializationConverter",
          "instanceId": "848640841",
          "threadName": "http-nio-8080-exec-1"
        }
      ],
      "info": {
        "nano": 377000000,
        "epochSecond": 1477411634
      }
    }

The main purpose of such REST endpoint is to show how process is handled by different Threads in different parts of request's workflow.