#!/bin/bash

# Script to standardize Docker and Kubernetes configurations
# This will remove duplicate files and ensure consistent configuration

echo "Starting Docker & Kubernetes configuration cleanup..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Identify authoritative Docker files
echo "Identifying and standardizing Dockerfiles..."

# Standard Dockerfile template
STANDARD_DOCKERFILE="FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT [\"java\",\"-jar\",\"/app.jar\"]"

# Development Dockerfile template 
STANDARD_DOCKERFILE_DEV="FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY \${JAR_FILE} app.jar
ENTRYPOINT [\"java\",\"-Dspring.profiles.active=dev\",\"-jar\",\"/app.jar\"]"

# Standardize Dockerfiles across services
for service_dir in $(find $ROOT_DIR/services -type d -name "*-service"); do
    service_name=$(basename "$service_dir")
    
    # Check if Dockerfile exists with wrong capitalization
    if [ ! -f "$service_dir/Dockerfile" ] && [ -f "$service_dir/DockerFile" ]; then
        echo "  Fixing capitalization: $service_dir/DockerFile -> Dockerfile"
        mv "$service_dir/DockerFile" "$service_dir/Dockerfile"
    fi
    
    # Create or standardize the main Dockerfile
    if [ ! -f "$service_dir/Dockerfile" ]; then
        echo "  Creating standard Dockerfile for $service_name"
        echo "$STANDARD_DOCKERFILE" > "$service_dir/Dockerfile"
    else
        # Check if we can simplify the existing Dockerfile
        if grep -q "openjdk" "$service_dir/Dockerfile" && ! grep -q "multistage" "$service_dir/Dockerfile"; then
            # Only standardize simple Dockerfiles, leave complex ones alone
            lines=$(wc -l < "$service_dir/Dockerfile")
            if [ "$lines" -lt 10 ]; then
                echo "  Standardizing Dockerfile for $service_name"
                echo "$STANDARD_DOCKERFILE" > "$service_dir/Dockerfile"
            fi
        fi
    fi
    
    # Create or standardize the dev Dockerfile
    if [ ! -f "$service_dir/Dockerfile.dev" ]; then
        echo "  Creating standard Dockerfile.dev for $service_name"
        echo "$STANDARD_DOCKERFILE_DEV" > "$service_dir/Dockerfile.dev"
    else
        # Check if we can simplify the existing Dockerfile.dev
        if grep -q "openjdk" "$service_dir/Dockerfile.dev" && ! grep -q "multistage" "$service_dir/Dockerfile.dev"; then
            # Only standardize simple Dockerfiles, leave complex ones alone
            lines=$(wc -l < "$service_dir/Dockerfile.dev")
            if [ "$lines" -lt 10 ]; then
                echo "  Standardizing Dockerfile.dev for $service_name"
                echo "$STANDARD_DOCKERFILE_DEV" > "$service_dir/Dockerfile.dev"
            fi
        fi
    fi
    
    # Remove any other non-standard Dockerfile variants
    find "$service_dir" -name "Dockerfile.*" | grep -v "Dockerfile.dev" | while read file; do
        echo "  Removing non-standard Dockerfile variant: $file"
        rm -f "$file"
    done
done

# Standardize docker-compose files
echo "Standardizing docker-compose files..."

# Remove individual docker-compose files from services as they're now consolidated
for compose_file in $(find $ROOT_DIR/services -name "docker-compose*.yml"); do
    # Don't delete if it has special configurations we want to keep
    if grep -q "custom-config" "$compose_file" || grep -q "special-service" "$compose_file"; then
        echo "  Keeping special docker-compose file: $compose_file"
    else
        echo "  Removing redundant docker-compose file: $compose_file"
        rm -f "$compose_file"
    fi
done

# Clean up Kubernetes configurations
echo "Cleaning up Kubernetes configurations..."

# Remove duplicated service definitions
duplicate_services=$(find $ROOT_DIR/infrastructure/kubernetes/deployments/services -name "*.yaml" | 
                    awk -F/ '{print $NF}' | 
                    sort | 
                    uniq -d)

for svc in $duplicate_services; do
    files=$(find $ROOT_DIR/infrastructure/kubernetes/deployments/services -name "$svc" | sort)
    keep=$(echo "$files" | head -1)
    echo "  Keeping: $keep"
    
    for file in $files; do
        if [ "$file" != "$keep" ]; then
            echo "  Removing duplicate: $file"
            rm -f "$file"
        fi
    done
done

echo "Docker and Kubernetes configuration cleanup completed!"