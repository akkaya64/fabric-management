#!/bin/bash

set -e

# Configuration
REGISTRY="your-registry.io"
VERSION="1.0.0"
NAMESPACE="fabric"

# Apply namespace
kubectl apply -f kubernetes/config-maps/namespace.yaml

# Apply ConfigMaps and Secrets
kubectl apply -f kubernetes/config-maps/
kubectl apply -f kubernetes/secrets/

# Apply infrastructure components
echo "Deploying infrastructure components..."
kubectl apply -f kubernetes/deployments/infra/

# Wait for infrastructure to be ready
echo "Waiting for infrastructure to be ready..."
kubectl wait --namespace=$NAMESPACE \
  --for=condition=ready pod \
  --selector=app=postgres \
  --timeout=300s

kubectl wait --namespace=$NAMESPACE \
  --for=condition=ready pod \
  --selector=app=consul \
  --timeout=300s

kubectl wait --namespace=$NAMESPACE \
  --for=condition=ready pod \
  --selector=app=kafka \
  --timeout=300s

# Apply services
echo "Deploying application services..."
kubectl apply -f kubernetes/deployments/services/

# Apply API Gateway
echo "Deploying API Gateway..."
kubectl apply -f kubernetes/deployments/api-gateway.yaml

# Apply Ingress
echo "Configuring Ingress..."
kubectl apply -f kubernetes/ingress/

echo "Deployment completed successfully"
echo "Access the application at: https://fabric.example.com"