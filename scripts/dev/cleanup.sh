#!/bin/bash

# Fail on any error
set -e

echo "Cleaning up development environment..."

# Stop and remove Docker containers
echo "Stopping Docker containers..."
docker-compose -f docker-compose.dev.yml down

# Remove Docker volumes (optional, comment out if you want to keep your data)
echo "Removing Docker volumes..."
docker volume rm fabric-management_postgres-data fabric-management_redis-data fabric-management_consul-data fabric-management_kafka-data fabric-management_prometheus-data fabric-management_grafana-data fabric-management_elasticsearch-data || true

# Clean Maven build artifacts
echo "Cleaning Maven artifacts..."
./mvnw clean -f pom.xml

# Clean up subdirectories
echo "Cleaning subdirectories..."
./mvnw clean -f fabric-parent/pom.xml || true
./mvnw clean -f infrastructure/api-gateway/pom.xml || true
./mvnw clean -f services/identity/auth-service/pom.xml || true
./mvnw clean -f services/identity/user-service/pom.xml || true
./mvnw clean -f libraries/java/fabric-java-security/pom.xml || true
./mvnw clean -f libraries/java/fabric-java-commons/pom.xml || true

echo "Cleanup completed!"