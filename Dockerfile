FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./agents_backend.jks .
COPY ./local_ssl.cer .
ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-jar","/app.jar"]