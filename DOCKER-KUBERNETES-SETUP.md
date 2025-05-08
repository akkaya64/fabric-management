# Docker and Kubernetes Setup for Fabric Management

This document explains how to use Docker and Kubernetes with the Fabric Management platform.

## Docker Setup

The project includes multiple Docker Compose files:

- `docker-compose.yml` - Main production Docker Compose file
- `docker-compose.dev.yml` - Development environment with essential services
- `docker-compose.test.yml` - Testing environment
- `docker-compose-all.yml` - All services included

### Development Environment

To start the development environment with the necessary infrastructure:

```bash
docker-compose -f docker-compose.dev.yml up -d
```

This will start:
- PostgreSQL database
- Redis
- Consul for service discovery
- Kafka for messaging
- Monitoring tools (Prometheus, Grafana)
- Distributed tracing with Zipkin
- ELK stack for logging

### Individual Services

Each microservice also includes its own Docker Compose file that can be used to run the service with its dependencies:

```bash
cd services/logistics/shipping-service
docker-compose up -d
```

### Building All Services

To build Docker images for all services:

```bash
# First make the script executable
chmod +x infrastructure/docker/scripts/build-all-services.sh

# Then run it
./infrastructure/docker/scripts/build-all-services.sh
```

## Kubernetes Setup

The project includes Kubernetes configuration files in the `infrastructure/kubernetes` directory:

- `config-maps/` - Configuration maps
- `deployments/` - Deployment definitions for services
- `ingress/` - Ingress rules
- `secrets/` - Secret management
- `scripts/` - Deployment scripts

### Deploying to Kubernetes

To deploy the entire application to Kubernetes:

```bash
# First make the script executable
chmod +x infrastructure/kubernetes/scripts/deploy-all.sh

# Then run it
./infrastructure/kubernetes/scripts/deploy-all.sh
```

This will:
1. Create the namespace
2. Apply ConfigMaps and Secrets
3. Deploy infrastructure services
4. Deploy the API Gateway
5. Deploy all microservices
6. Configure ingress rules

### Accessing the Application

After deployment, the application can be accessed at:
- `fabric.example.com` (configure DNS or add to `/etc/hosts`)

## Dockerfiles

Each microservice includes:
- `Dockerfile` - Production build
- `Dockerfile.dev` - Development build with extras for debugging

## Environment Variables

The Docker Compose and Kubernetes configurations use environment variables for configuration:

- `SPRING_PROFILES_ACTIVE` - Active Spring profile
- `SPRING_DATASOURCE_*` - Database connection details
- `SPRING_CLOUD_CONSUL_HOST` - Consul host for service discovery
- `SPRING_ZIPKIN_BASE_URL` - Zipkin URL for distributed tracing
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` - Kafka bootstrap servers

## Monitoring and Observability

The setup includes:
- Prometheus for metrics
- Grafana for dashboards
- ELK stack for logging
- Zipkin for distributed tracing

Access these tools at:
- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090
- Kibana: http://localhost:5601
- Zipkin: http://localhost:9411