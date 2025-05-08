#!/bin/bash

set -e

# Root directory of the project
ROOT_DIR=$(git rev-parse --show-toplevel)
cd $ROOT_DIR

# Docker registry (replace with your actual registry if needed)
REGISTRY="localhost:5000"

# Current version (using git commit hash)
VERSION=$(git rev-parse --short HEAD)

# Credentials setup (modify as needed)
# docker login $REGISTRY -u username -p password

# Build and push services
build_and_push_service() {
    local SERVICE_PATH=$1
    local SERVICE_NAME=$2
    
    echo "Building $SERVICE_NAME..."
    cd $SERVICE_PATH
    
    # Build with Maven
    ./mvnw clean package -DskipTests
    
    # Build Docker image
    docker build -t $REGISTRY/fabric/$SERVICE_NAME:$VERSION -t $REGISTRY/fabric/$SERVICE_NAME:latest .
    
    # Push Docker image
    docker push $REGISTRY/fabric/$SERVICE_NAME:$VERSION
    docker push $REGISTRY/fabric/$SERVICE_NAME:latest
    
    echo "$SERVICE_NAME build and push completed."
    cd $ROOT_DIR
}

# Infrastructure services
build_and_push_service "$ROOT_DIR/infrastructure/api-gateway" "api-gateway"

# Identity services
build_and_push_service "$ROOT_DIR/services/identity/auth-service" "auth-service"
build_and_push_service "$ROOT_DIR/services/identity/user-service" "user-service"

# Organization services
build_and_push_service "$ROOT_DIR/services/organization/company-service" "company-service"

# HR services
build_and_push_service "$ROOT_DIR/services/hr/employee-service" "employee-service"

# Finance services
build_and_push_service "$ROOT_DIR/services/finance/finance-service" "finance-service"

# Contact services
build_and_push_service "$ROOT_DIR/services/contact/contact-service" "contact-service"

# Logistics services
build_and_push_service "$ROOT_DIR/services/logistics/shipping-service" "shipping-service"
build_and_push_service "$ROOT_DIR/services/logistics/transportation-service" "transportation-service"

# Production services
build_and_push_service "$ROOT_DIR/services/production/yarn-service" "yarn-service"
build_and_push_service "$ROOT_DIR/services/production/weaving-service" "weaving-service"
build_and_push_service "$ROOT_DIR/services/production/dyeing-finishing-service" "dyeing-finishing-service"
build_and_push_service "$ROOT_DIR/services/production/quality-control-service" "quality-control-service"

# Inventory services
build_and_push_service "$ROOT_DIR/services/inventory/stock-service" "stock-service"
build_and_push_service "$ROOT_DIR/services/inventory/warehouse-service" "warehouse-service"

# Notification services
build_and_push_service "$ROOT_DIR/services/notification/notification-service" "notification-service"

echo "All services built and pushed successfully."