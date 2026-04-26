# WinWin.travel Backend Test Task

This project consists of two Spring Boot services (Service A and Service B) that demonstrate authentication, inter-service communication, and data processing.

## Architecture

- **Service A (auth-api)**: Handles user registration, login (JWT), and protected processing.
- **Service B (data-api)**: Performs text transformation (reverse + uppercase).
- **Postgres**: Stores user data and processing logs.

## Prerequisites

- Docker and Docker Compose
- Maven (if building locally)
- Java 25 (project configured for Java 25)

## Getting Started

1.  **Environment Setup**: Create a `.env` file in the root directory (one is provided by default).
2.  **Build and Run**:
    ```bash
    # Build the services
    mvn -f auth-api/pom.xml clean package -DskipTests
    mvn -f data-api/pom.xml clean package -DskipTests

    # Start the infrastructure
    docker compose up -d --build
    ```

## Testing the API

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com","password":"password123"}'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com","password":"password123"}'
```
*Note: Copy the `token` from the response.*

### 3. Process Text (Protected)
```bash
curl -X POST http://localhost:8080/api/process \
     -H "Authorization: Bearer <YOUR_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{"text":"hello world"}'
```
**Expected Response:** `{"result":"DLROW OLLEH"}`

## Environment Variables

| Variable | Description |
|----------|-------------|
| `POSTGRES_DB` | Database name |
| `POSTGRES_USER` | Database user |
| `POSTGRES_PASSWORD` | Database password |
| `POSTGRES_URL` | JDBC URL for auth-api |
| `JWT_SECRET` | Secret for JWT signing |
| `INTERNAL_TOKEN` | Shared secret between services |
