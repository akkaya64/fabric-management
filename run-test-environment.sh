#!/bin/bash

# Script to run a minimal test environment to verify configuration

echo "Starting minimal test environment..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Stop any running containers from previous runs
echo "Stopping any existing containers..."
docker-compose -f docker-compose.dev.yml down || true

# Start the core infrastructure services
echo "Starting core infrastructure services..."
docker-compose -f docker-compose.dev.yml up -d postgres redis consul kafka

# Wait for infrastructure to be ready
echo "Waiting for infrastructure services to be ready..."
sleep 10

# Check if services are running
echo "Checking service status..."
docker-compose -f docker-compose.dev.yml ps

# Verify Postgres is responding
echo "Testing PostgreSQL connection..."
docker exec fabric-postgres pg_isready -U fabric

# Verify Consul is responding
echo "Testing Consul API..."
curl -s http://localhost:8500/v1/status/leader

# Verify Kafka is responding
echo "Testing Kafka status..."
docker exec fabric-kafka kafka-topics.sh --bootstrap-server localhost:9092 --list

echo ""
echo "Test environment is running!"
echo "You can now test your microservices against this environment."
echo ""
echo "To stop the test environment:"
echo "  docker-compose -f docker-compose.dev.yml down"
echo ""
echo "To view logs:"
echo "  docker-compose -f docker-compose.dev.yml logs -f"