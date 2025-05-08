#!/bin/bash

set -e

# Root directory of the project
ROOT_DIR=$(git rev-parse --show-toplevel)
cd $ROOT_DIR/infrastructure/kubernetes

# Current version (using git commit hash)
VERSION=$(git rev-parse --short HEAD)
REGISTRY="localhost:5000"  # Replace with your actual registry

# Create namespace if it doesn't exist
kubectl apply -f config-maps/namespace.yaml

# Apply ConfigMaps and Secrets
kubectl apply -f config-maps/application-config.yaml
kubectl apply -f secrets/fabric-secrets.yaml

# Deploy infrastructure services first
echo "Deploying infrastructure services..."
kubectl apply -f deployments/monitoring/prometheus-deployment.yaml
kubectl apply -f deployments/monitoring/grafana-deployment.yaml
kubectl apply -f deployments/monitoring/elk-stack-deployment.yaml
kubectl apply -f deployments/monitoring/zipkin-deployment.yaml

# Wait for infrastructure to be ready
echo "Waiting for infrastructure services to be ready..."
kubectl rollout status deployment/prometheus -n fabric
kubectl rollout status deployment/grafana -n fabric
kubectl rollout status deployment/elasticsearch -n fabric
kubectl rollout status deployment/zipkin -n fabric

# Deploy API Gateway
echo "Deploying API Gateway..."
envsubst < deployments/api-gateway.yaml | kubectl apply -f -
kubectl rollout status deployment/api-gateway -n fabric

# Deploy Auth Service
echo "Deploying Auth Service..."
envsubst < deployments/auth-service.yaml | kubectl apply -f -
kubectl rollout status deployment/auth-service -n fabric

# Deploy microservices
echo "Deploying microservices..."

# Identity services
envsubst < deployments/services/user-service.yaml | kubectl apply -f -

# Organization services
envsubst < deployments/services/company-service.yaml | kubectl apply -f -

# HR services
envsubst < deployments/services/employee-service.yaml | kubectl apply -f -

# Finance services
envsubst < deployments/services/finance-service.yaml | kubectl apply -f -

# Contact services
envsubst < deployments/services/contact-service.yaml | kubectl apply -f -

# Logistics services
envsubst < deployments/services/shipping-service.yaml | kubectl apply -f -
envsubst < deployments/services/transportation-service.yaml | kubectl apply -f -

# Other microservices
for service in deployments/services/*.yaml; do
  if [[ "$service" != *user-service* && 
        "$service" != *company-service* && 
        "$service" != *employee-service* && 
        "$service" != *finance-service* && 
        "$service" != *contact-service* && 
        "$service" != *shipping-service* && 
        "$service" != *transportation-service* ]]; then
    echo "Deploying $service..."
    envsubst < $service | kubectl apply -f -
  fi
done

# Wait for all deployments to complete
echo "Waiting for all services to be ready..."
kubectl get deployments -n fabric | tail -n +2 | awk '{print $1}' | xargs -I{} kubectl rollout status deployment/{} -n fabric

# Apply ingress rules
echo "Deploying ingress rules..."
kubectl apply -f ingress/fabric-ingress.yaml

echo "Deployment completed successfully!"
echo "You can access the application at: fabric.example.com (after setting up DNS or adding to your /etc/hosts)"