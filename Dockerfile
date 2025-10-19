FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar /app/app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
