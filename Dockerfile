FROM gradle:8.13-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle --no-daemon bootJar

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run with proper memory settings
ENTRYPOINT ["java", "-Xms512m", "-Xmx512m", "-jar", "app.jar"]
