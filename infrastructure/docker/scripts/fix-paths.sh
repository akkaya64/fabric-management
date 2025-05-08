#!/bin/bash

# Fix paths in Docker Compose and Kubernetes files
echo "Fixing paths in configuration files..."

# Root directory of the project
ROOT_DIR=$(git rev-parse --show-toplevel)
cd $ROOT_DIR

# Fix API Gateway path in docker-compose-all.yml
sed -i.bak 's|./infrastructure/api-gateway|./infrastructure/api-gateway|g' docker-compose-all.yml
sed -i.bak 's|java-services/user-service|services/identity/user-service|g' docker-compose.yml

# Fix file paths for Auth and User services
sed -i.bak 's|auth-service/Dockerfile|services/identity/auth-service/Dockerfile|g' docker-compose.yml
sed -i.bak 's|java-services/user-service|services/identity/user-service|g' docker-compose.yml

# Fix paths in kubernetes deployment scripts
sed -i.bak 's|\${REGISTRY}/fabric/|${REGISTRY}/fabric-management/|g' infrastructure/kubernetes/deployments/*.yaml
sed -i.bak 's|\${REGISTRY}/fabric/|${REGISTRY}/fabric-management/|g' infrastructure/kubernetes/deployments/services/*.yaml

# Ensure consistency in environment variables
for compose_file in $(find . -name "docker-compose*.yml"); do
  echo "Standardizing environment variables in $compose_file"
  sed -i.bak 's|SPRING_PROFILES_ACTIVE=.*|SPRING_PROFILES_ACTIVE=dev|g' $compose_file
done

# Clean up backup files
find . -name "*.bak" -delete

echo "Path fixes completed!"