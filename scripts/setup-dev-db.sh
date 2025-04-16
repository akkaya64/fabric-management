#!/bin/bash

# Fail on any error
set -e

echo "Setting up development database..."

# Check if Postgres container is running
if ! docker ps | grep -q fabric-postgres; then
  echo "Error: PostgreSQL container is not running. Please start Docker Compose first."
  exit 1
fi

echo "Creating schemas and tables..."

# Execute init SQL script
docker exec -i fabric-postgres psql -U fabric -d fabric < ./infrastructure/docker/postgres/init/init-db.sql

echo "Database setup completed successfully!"