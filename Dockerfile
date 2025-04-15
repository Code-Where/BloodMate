# First stage: build the jar
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# Copy everything (your code, pom.xml, etc.)
COPY . .

# Build the project (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Second stage: run the jar
FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

# Copy the jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
