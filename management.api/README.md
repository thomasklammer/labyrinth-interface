# management.api

## Overview

management.api is a small Spring Boot web API to manage and register game server instances. The application provides endpoints to register, update (heartbeat), discover and unregister game servers. Persistence is implemented with JPA using an in-memory H2 database by default.

## Technologies

- Java 21
- Spring Boot 3.5.6 (declared in parent pom)
- Spring Web, Spring Data JPA
- H2 (in-memory, runtime)
- Lombok (compile-time)
- springdoc-openapi (Swagger UI)

## Requirements

- Java 21 (JDK)
- Git (optional)
- Maven is not strictly required because the project contains the Maven Wrapper (`mvnw.cmd`) — on Windows use `mvnw.cmd`.

## Quickstart (Windows - cmd.exe)

### 1) Build the project (including tests):

```bat
mvnw.cmd test
```

### 2) Start the application:

```bat
mvnw.cmd spring-boot:run
```

Alternatively: build a package and run the JAR

```bat
mvnw.cmd package -DskipTests
java -jar target\management.api-0.0.1-SNAPSHOT.jar
```

## Default URL

After startup the API is available at: http://localhost:8081

## Configuration (key settings)

The most important settings are in `src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:h2:mem:testdb` (in-memory H2 DB)
- `spring.h2.console.enabled=true` (H2 console enabled)
- `springdoc.swagger-ui.path=/swagger-ui.html` (Swagger UI path)
- `springdoc.api-docs.path=/v3/api-docs`

## H2 Console

- URL: http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (empty)

## OpenAPI / Swagger

- OpenAPI JSON: http://localhost:8081/v3/api-docs
- Swagger UI: http://localhost:8081/swagger-ui.html
- The application also exposes `/v3/api-docs.yaml` which the Swagger UI can use.


## Notes

- The project uses Lombok; IDEs may require the Lombok plugin to recognize generated getters/setters.
- The in-memory H2 database is reset on application restart. For persistent storage configure an external database in `application.properties`.
