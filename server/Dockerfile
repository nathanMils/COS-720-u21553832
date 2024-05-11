## Build stage: Builds and runs each deployment
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /usr/src/app
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests -Pprod

## Production stage
FROM eclipse-temurin:21-jre
WORKDIR /usr/app

# Copy the JAR file
COPY --from=build /usr/src/app/target/server-0.0.1-SNAPSHOT.jar /usr/app/server-0.0.1-SNAPSHOT.jar

# Copy the Logback configuration file
COPY src/main/resources/logback-spring.xml /usr/app/logback-spring.xml

# Create log directory
RUN mkdir /usr/app/logs

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/usr/app/server-0.0.1-SNAPSHOT.jar"]
