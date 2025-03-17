#!/bin/bash

set -e

# Configuration
REGISTRY="your-registry.io"
VERSION="1.0.0"
SERVICES=(
  "api-gateway"
  "auth-service"
  "java-services/user-service"
  "java-services/address-service"
  "java-services/base-person-service"
  "java-services/company-service"
  "java-services/contact-service"
  "java-services/email-service"
  "java-services/employee-service"
  "java-services/finance-service"
  "java-services/mail-service"
  "java-services/performance-service"
  "java-services/phone-service"
)

# Ensure we're in the project root
cd "$(dirname "$0")"

# Build and push each service
for SERVICE in "${SERVICES[@]}"; do
  echo "Building $SERVICE..."

  # Extract service name for Docker tag
  SERVICE_NAME=$(basename "$SERVICE")

  # Build the Docker image
  docker build -t "$REGISTRY/fabric/$SERVICE_NAME:$VERSION" \
               -t "$REGISTRY/fabric/$SERVICE_NAME:latest" \
               --build-arg SPRING_PROFILES_ACTIVE=prod \
               "$SERVICE"

  # Push the image to the registry
  echo "Pushing $SERVICE_NAME to registry..."
  docker push "$REGISTRY/fabric/$SERVICE_NAME:$VERSION"
  docker push "$REGISTRY/fabric/$SERVICE_NAME:latest"

  echo "$SERVICE_NAME completed successfully"
  echo "------------------------------------------------"
done

echo "All services built and pushed successfully"