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

    $ curl "localhost:8080/"
    {"provider":"poc-spring-akka-akka.actor.default-dispatcher-2","info":{"nano":418000000,"epochSecond":1474574940}}
