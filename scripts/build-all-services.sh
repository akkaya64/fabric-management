#!/bin/bash

# First build common libraries
echo "Building common libraries..."
./scripts/build-libraries.sh

if [ $? -ne 0 ]; then
    echo "❌ Failed to build common libraries. Cannot continue."
    exit 1
fi

# Build all microservices
echo "Building all microservices..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Infrastructure services
echo "Building API Gateway..."
cd $ROOT_DIR/infrastructure/api-gateway
./mvnw clean package -DskipTests

# Identity services
echo "Building Auth Service..."
cd $ROOT_DIR/services/identity/auth-service
./mvnw clean package -DskipTests

echo "Building User Service..."
cd $ROOT_DIR/services/identity/user-service
./mvnw clean package -DskipTests

# Organization services
echo "Building Company Service..."
cd $ROOT_DIR/services/organization/company-service
./mvnw clean package -DskipTests

# HR services
echo "Building Employee Service..."
cd $ROOT_DIR/services/hr/employee-service
./mvnw clean package -DskipTests

# Finance services
echo "Building Finance Service..."
cd $ROOT_DIR/services/finance/finance-service
./mvnw clean package -DskipTests

# Contact services
echo "Building Contact Service..."
cd $ROOT_DIR/services/contact/contact-service
./mvnw clean package -DskipTests

# Logistics services
echo "Building Shipping Service..."
cd $ROOT_DIR/services/logistics/shipping-service
./mvnw clean package -DskipTests

echo "Building Transportation Service..."
cd $ROOT_DIR/services/logistics/transportation-service
./mvnw clean package -DskipTests

# Production services
echo "Building Yarn Service..."
cd $ROOT_DIR/services/production/yarn-service
./mvnw clean package -DskipTests

echo "Building Weaving Service..."
cd $ROOT_DIR/services/production/weaving-service
./mvnw clean package -DskipTests

echo "Building Dyeing-Finishing Service..."
cd $ROOT_DIR/services/production/dyeing-finishing-service
./mvnw clean package -DskipTests

echo "Building Quality Control Service..."
cd $ROOT_DIR/services/production/quality-control-service
./mvnw clean package -DskipTests

# Inventory services
echo "Building Stock Service..."
cd $ROOT_DIR/services/inventory/stock-service
./mvnw clean package -DskipTests

echo "Building Warehouse Service..."
cd $ROOT_DIR/services/inventory/warehouse-service
./mvnw clean package -DskipTests

# Notification services
echo "Building Notification Service..."
cd $ROOT_DIR/services/notification/notification-service
./mvnw clean package -DskipTests

echo "All services built successfully!"