#
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file and download dependencies (for layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY . .

# Package the application into a JAR file
RUN mvn clean install -DskipTests
RUN ls -lh /app/target

# Use a slimmer JRE image to run the application
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/*.jar /app/sensitive-words-0.0.1.jar

EXPOSE 8080
# Command to run the application
ENTRYPOINT ["java", "-jar", "sensitive-words-0.0.1.jar"]
