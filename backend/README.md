# SplitEven backend

The backend is a Spring Boot REST API. It contains the HTTP controllers, application logic implemented through services, DTOs and mappers for API data, JPA entities and repositories, and the balance calculation domain logic.

## Structure

- `src/main/java/se/hem/spliteven/controller/` - REST endpoints and exception handling
- `src/main/java/se/hem/spliteven/service/` - application logic and workflows
- `src/main/java/se/hem/spliteven/domain/` - balance calculation rules
- `src/main/java/se/hem/spliteven/dto/` - request and response data transfer objects
- `src/main/java/se/hem/spliteven/mapper/` - maps between DTOs and domain objects
- `src/main/java/se/hem/spliteven/model/` - JPA entities
- `src/main/java/se/hem/spliteven/repository/` - database repositories
- `src/main/java/se/hem/spliteven/dto/` - request and response objects
- `src/main/resources/` - application configuration
- `sql/` - PostgreSQL schema
- `src/test/` - backend tests

## Requirements

- Java 21
- PostgreSQL

## Database setup

Create a PostgreSQL database named `spliteven`, then apply the schema:

```bash
psql -h localhost -U <username> -d spliteven -f sql/schema.sql
```

Update the database URL and credentials in `src/main/resources/application.properties`. The application uses `spring.jpa.hibernate.ddl-auto=validate`, so Hibernate checks the schema but does not create or update tables.

Mail settings use these environment variables:

```bash
export MAIL_USERNAME=<mail-address>
export MAIL_PASSWORD=<mail-password>
```

## Run

```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080`.

## Test

```bash
./mvnw test
```

To create a packaged application:

```bash
./mvnw package
```
