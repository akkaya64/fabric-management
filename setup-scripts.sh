#!/bin/bash

# Make scripts executable
chmod +x infrastructure/docker/scripts/build-all-services.sh
chmod +x infrastructure/kubernetes/scripts/deploy-all.sh
chmod +x scripts/dev/cleanup.sh
chmod +x scripts/setup-dev-db.sh

echo "All scripts have been made executable."
echo "Docker and Kubernetes setup is complete."
echo ""
echo "To build all services:"
echo "  ./infrastructure/docker/scripts/build-all-services.sh"
echo ""
echo "To deploy to Kubernetes:"
echo "  ./infrastructure/kubernetes/scripts/deploy-all.sh"
echo ""
echo "To run all services with Docker Compose:"
echo "  docker-compose -f docker-compose-all.yml up -d"
echo ""
echo "To run in development mode:"
echo "  docker-compose -f docker-compose.dev.yml up -d"