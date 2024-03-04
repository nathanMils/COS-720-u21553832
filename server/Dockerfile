FROM maven:3.9.6-eclipse-temurin-21 AS builder
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM eclipse-temurin:21-jre
COPY --from=build /usr/src/app/target/server-0.0.1-SNAPSHOT.jar /usr/app/server-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/server-0.0.1-SNAPSHOT.jar"]