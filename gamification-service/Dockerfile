FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/gamification-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
