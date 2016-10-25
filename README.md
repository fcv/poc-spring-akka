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

    $ curl "localhost:8080/api/rest/v1"
    {
      "provider": "poc-spring-akka-akka.actor.default-dispatcher-5",
      "trace": [
        {
          "className": "br.fcv.poc.web.ScalaController",
          "threadName": "http-nio-8080-exec-6"
        },
        {
          "className": "br.fcv.poc.core.MyScalaActor",
          "threadName": "poc-spring-akka-akka.actor.default-dispatcher-5"
        },
        {
          "className": "br.fcv.poc.core.ClockServiceBean",
          "threadName": "poc-spring-akka-akka.actor.default-dispatcher-5"
        },
        {
          "className": "br.fcv.poc.web.ScalaController",
          "threadName": "poc-spring-akka-akka.actor.default-dispatcher-4"
        },
        {
          "className": "br.fcv.poc.web.SerializationConverter",
          "threadName": "http-nio-8080-exec-7"
        }
      ],
      "info": {
        "nano": 566000000,
        "epochSecond": 1477332726
      }
    }
