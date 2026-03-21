# Build Stage
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

RUN mvn clean install -DskipTests
RUN ls -lh /app/target

# Run Stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar /app/sensitive-words-0.0.1.jar

EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "sensitive-words-0.0.1.jar"]