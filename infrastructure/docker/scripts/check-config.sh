#!/bin/bash

# Check and validate Docker and Kubernetes configuration
echo "Checking Docker and Kubernetes configuration..."

# Check if Docker is running
if ! docker info >/dev/null 2>&1; then
  echo "Error: Docker is not running or not installed"
  echo "Please start Docker and try again"
  exit 1
else
  echo "✓ Docker is running"
fi

# Check if Docker Compose files are valid
for file in $(find . -name "docker-compose*.yml"); do
  echo "Validating $file"
  if ! docker-compose -f $file config >/dev/null 2>&1; then
    echo "Error: $file is not valid"
    docker-compose -f $file config
  else
    echo "✓ $file is valid"
  fi
done

# Check if Kubernetes is available (if kubectl is installed)
if command -v kubectl >/dev/null 2>&1; then
  if ! kubectl version --client >/dev/null 2>&1; then
    echo "Warning: kubectl is installed but may not be configured correctly"
  else
    echo "✓ kubectl is configured"
    
    # Check Kubernetes YAML files syntax
    for file in $(find ./infrastructure/kubernetes -name "*.yaml"); do
      echo "Validating $file"
      if ! kubectl apply --dry-run=client -f $file >/dev/null 2>&1; then
        echo "Error: $file has syntax errors"
        kubectl apply --dry-run=client -f $file
      else
        echo "✓ $file is valid"
      fi
    done
  fi
else
  echo "Warning: kubectl is not installed, skipping Kubernetes checks"
fi

# Check if all Dockerfiles exist for services
missing_dockerfiles=0
for service_dir in $(find ./services -type d -name "*-service"); do
  if [ ! -f "$service_dir/Dockerfile" ]; then
    echo "Warning: $service_dir is missing a Dockerfile"
    missing_dockerfiles=$((missing_dockerfiles+1))
  fi
done

if [ $missing_dockerfiles -eq 0 ]; then
  echo "✓ All services have Dockerfiles"
else
  echo "Warning: $missing_dockerfiles services are missing Dockerfiles"
fi

echo "Configuration check completed!"