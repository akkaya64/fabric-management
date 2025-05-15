Docker Configuration

This document explains how to use the Docker and Docker Compose setup for the fabric-management project.

1. Prerequisites

Docker Engine (version ≥ 20.10)

Docker Compose (version ≥ 1.29) or Docker Compose V2

Java 17 SDK (for local development)

Maven (for local builds)

2. Directory Structure

fabric-management/
├── pom.xml                          ← Parent Maven POM
├── .env                             ← Shared environment variables
├── docker-compose.yml              ← Bring up all infrastructure and services
├── docker-compose.dev.yml          ← Development overrides (hot-reload)
├── docker-compose.test.yml         ← Containers for testing environment
├── libraries/                       ← Shared libraries
│   ├── common/
│   ├── security/
│   └── messaging/
└── services/
└── identity/
├── auth-service/
│   ├── pom.xml
│   └── Dockerfile
└── user-service/
├── pom.xml
└── Dockerfile

3. Environment Variables (.env)

Place a .env file at the project root with the following content:

POSTGRES_USER=fabric
POSTGRES_PASSWORD=fabric123
POSTGRES_DB=fabric
SPRING_PROFILES_ACTIVE=dev

This file is automatically loaded by docker-compose.yml and all services.

4. Dockerfile (Multi-Stage Build)

Each service module uses the following template in its Dockerfile:

# Build stage
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=/app/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar

# Health check to ensure the service is up
HEALTHCHECK --interval=30s --timeout=3s \
CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","/app.jar"]

Multi-Stage: Reduces final image size and isolates build tools.

HEALTHCHECK: Monitors service health and allows Docker Compose to wait for readiness.

5. docker-compose.yml

This file defines all infrastructure components (Postgres, Consul, Kafka, Prometheus, Grafana, Zipkin, ELK) and application services (auth-service, user-service). Example snippet:

version: '3.8'

services:
postgres:
image: postgres:16-alpine
env_file: .env
volumes:
- postgres-data:/var/lib/postgresql/data
networks:
- fabric-network
healthcheck:
test: ["CMD-SHELL", "pg_isready -U $${POSTGRES_USER}"]
interval: 10s
timeout: 5s
retries: 5

auth-service:
build:
context: ./services/identity/auth-service
ports:
- "8081:8081"
env_file: .env
depends_on:
postgres:
condition: service_healthy
consul:
condition: service_healthy
healthcheck:
test: ["CMD-SHELL", "curl -f http://localhost:8081/actuator/health"]
      interval: 30s
timeout: 5s
retries: 3

networks:
fabric-network:
driver: bridge

volumes:
postgres-data:

6. Development Overrides (docker-compose.dev.yml)

To enable hot-reload without rebuilding the image:

version: '3.8'

services:
auth-service:
volumes:
- ./services/identity/auth-service/src:/app/src
command: mvn spring-boot:run

user-service:
volumes:
- ./services/identity/user-service/src:/app/src
command: mvn spring-boot:run

Run in development mode:

docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

7. Testing Environment (docker-compose.test.yml)

Define containers needed for integration and end-to-end tests:

version: '3.8'

services:
postgres-test:
image: postgres:16-alpine
environment:
POSTGRES_USER: fabric_test
POSTGRES_PASSWORD: fabric_test
POSTGRES_DB: fabric_test
healthcheck:
test: ["CMD-SHELL", "pg_isready -U fabric_test"]
interval: 10s
timeout: 5s
retries: 5

wiremock:
image: wiremock/wiremock:latest
ports:
- "8888:8080"

8. Common Commands

Build and start all services:

docker-compose up --build

Start in development mode with hot-reload:

docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

Follow logs for a specific service:

docker-compose logs -f auth-service

Connect to Postgres container:

docker-compose exec postgres psql -U $POSTGRES_USER -d $POSTGRES_DB

With this setup, you can quickly spin up your local Docker environment, develop with hot-reload, and run tests in isolated containers. Good luck!