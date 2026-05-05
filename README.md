# User Management Backend Demo

This project is a production-like Java Spring Boot demo for a vulnerable user management system. It uses Spring Boot, Maven, Java 21, H2, JPA/Hibernate, Spring Security, Actuator, and Swagger/OpenAPI.

## Run locally

Requirements:

- Java 21
- Maven 3.9+

Build the application:

```bash
mvn clean package
```

After packaging, Maven copies the runnable jar to the repository root as `app.jar`.

Run the packaged jar:

```bash
java -jar app.jar
```

Run with a custom port on PowerShell:

```bash
$env:PORT=9090
java -jar app.jar
```

Run with a custom port on bash:

```bash
PORT=9090 java -jar app.jar
```

Use the application after startup:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`
- H2 Console: `http://localhost:8080/h2-console`
- Actuator Health: `http://localhost:8080/actuator/health`

## API summary

User CRUD:

- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `PUT /users/{id}`
- `DELETE /users/{id}`

Demo endpoints:

- `GET /demo/sql/users?name=Demo Admin`
- `GET /demo/xss?input=<script>alert('xss')</script>`
- `GET /demo/slow`
- `GET /demo/sensitive-data`
- `GET /demo/admin/credentials` with Basic Auth `demo-admin` / `demo123`

## Known vulnerabilities

The application is intentionally insecure and must only be used for demos or training.

- Hardcoded Basic Auth credentials in Spring Security configuration
- No password encryption for stored user passwords
- CSRF protection disabled
- Almost all endpoints are open without proper authorization
- Reflected XSS endpoint at `/demo/xss`
- SQL injection endpoint at `/demo/sql/users`
- Sensitive data exposed without authentication at `/demo/sensitive-data`
- Broad actuator exposure

## Intentional technical debt

- Field injection instead of constructor injection
- Duplicate validation logic in the service layer
- Long service methods with no refactoring
- No proper exception handling or error mapping
- TODO comments left in code to mark debt

## Docker

Build the jar first, then build the container image:

```bash
mvn clean package
docker build -t user-management-demo .
```

Run the container:

```bash
docker run -p 8080:8080 -e PORT=8080 user-management-demo
```

## Sample payload

```json
{
  "name": "Jordan Example",
  "email": "jordan@example.com",
  "password": "plaintext123",
  "role": "USER"
}
```