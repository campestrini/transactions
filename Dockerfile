FROM openjdk:17-jdk

WORKDIR /app

COPY target/transactions-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]