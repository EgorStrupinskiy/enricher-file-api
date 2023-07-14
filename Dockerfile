FROM adoptopenjdk:17-jre-hotspot

WORKDIR /app

COPY target/file-api-0.0.1-SNAPSHOT.jar .

EXPOSE 8084

CMD ["java", "-jar", "file-api-0.0.1-SNAPSHOT.jar"]
