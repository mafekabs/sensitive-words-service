**Implementation Details:**
 - A Docarized Spring-Boot 3.3.3 RESTFul API with
 - Java 17
 - Maven
 - MSSQL as Persistence Provider
 - Flyway as a DB Migration Tool
 - jacoco as a Test Coverage Viewer

 **Dockerized Components:**
  - The Application and 
  - The MSSQL

**Local Environment Setup: **
  - Ensure you have Docker Installed
  - Clone this Repo
  - Run "mvn clean install"

**How to Build and Deploy:**
  - Run "docker compose up -d --build"
  - The Application should start-up on port 8080
  - The swagger/openapi Specification accessisable here: http://localhost:8080/swagger-ui/index.html

**Running Tests**
 - cd into the project root and
 - Run "mvn clean test"
 - Goto the target/jacoco/site/ directory and open the index page for ease of coverage viewing.
  





