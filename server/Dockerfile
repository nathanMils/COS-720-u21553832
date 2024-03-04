FROM eclipse-temurin:21.0.2_13-jdk
ARG JAR_FILE=target/server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} server.jar
ENTRYPOINT ["java","-jar","/server.jar"]
EXPOSE 8080