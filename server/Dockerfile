## Can't use this until i figure out how to work with the spring data jpa ddl-auto: create/update/so on
# FROM maven:3.9.6-eclipse-temurin-21 AS build
# COPY src /usr/src/app/src
# COPY pom.xml /usr/src/app
# RUN mvn -f /usr/src/app/pom.xml clean package

# FROM eclipse-temurin:21-jre
# COPY --from=build /usr/src/app/target/server-0.0.1-SNAPSHOT.jar /usr/app/server-0.0.1-SNAPSHOT.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/usr/app/server-0.0.1-SNAPSHOT.jar"]

# This will work for now => once images are all hosted on docker hub this wont be needed
FROM maven:3.9.6-eclipse-temurin-21
COPY /src/target/server-0.0.1-SNAPSHOT.jar /usr/app/server-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/server-0.0.1-SNAPSHOT.jar"]