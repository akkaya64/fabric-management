#!/bin/bash

# Script to clean up unnecessary files in the fabric-management project

echo "Starting project cleanup..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Remove temporary and backup files
echo "Removing temporary and backup files..."
find $ROOT_DIR -name "*.bak" -type f -delete
find $ROOT_DIR -name "*.tmp" -type f -delete
find $ROOT_DIR -name "*~" -type f -delete
find $ROOT_DIR -name "*.swp" -type f -delete

# Clean up target directories (build artifacts)
echo "Cleaning up build artifacts..."
find $ROOT_DIR -name "target" -type d -exec rm -rf {} \; 2>/dev/null || true

# Remove duplicate property files where YML files exist
echo "Removing duplicate property files..."
for yml_file in $(find $ROOT_DIR -name "application*.yml"); do
    dir_name=$(dirname "$yml_file")
    base_name=$(basename "$yml_file" .yml)
    prop_file="$dir_name/$base_name.properties"
    
    if [ -f "$prop_file" ]; then
        echo "Found duplicate: $prop_file will be removed (yml version exists)"
        rm -f "$prop_file"
    fi
done

# Remove unused Docker Compose files
echo "Cleaning up individual Docker Compose files..."
find $ROOT_DIR/services -name "docker-compose*.yml" | while read file; do
    if grep -q "This file has been deprecated" "$file" || grep -q "DO NOT USE" "$file"; then
        echo "Removing deprecated Docker Compose file: $file"
        rm -f "$file"
    fi
done

# Remove .idea directory (IntelliJ IDEA files)
echo "Removing IDE-specific files..."
rm -rf $ROOT_DIR/.idea

# Clean up .DS_Store files (macOS)
echo "Removing .DS_Store files..."
find $ROOT_DIR -name ".DS_Store" -type f -delete

# Remove any empty directories
echo "Removing empty directories..."
find $ROOT_DIR -type d -empty -delete

# Create a .gitignore file if it doesn't exist
if [ ! -f "$ROOT_DIR/.gitignore" ]; then
    echo "Creating .gitignore file..."
    cat > "$ROOT_DIR/.gitignore" << EOF
# Maven
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

# Gradle
.gradle
build/
!gradle/wrapper/gradle-wrapper.jar
!**/src/main/**/build/
!**/src/test/**/build/

# IntelliJ IDEA
.idea
*.iws
*.iml
*.ipr

# Eclipse
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

# NetBeans
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/

# VS Code
.vscode/

# Mac OS
.DS_Store

# Docker
docker-compose.override.yml

# Logs
logs/
*.log

# Secrets - never commit these!
*.pem
*.key
*secret*
*password*
EOF
fi

# Cleanup duplicate Dockerfiles
echo "Checking for unused or duplicate Dockerfiles..."
for dir in $(find $ROOT_DIR -type d -name "*-service"); do
    if [ -f "$dir/Dockerfile" ] && [ -f "$dir/Dockerfile.dev" ]; then
        if [ -f "$dir/Dockerfile.prod" ]; then
            echo "Removing redundant Dockerfile.prod (we already have Dockerfile and Dockerfile.dev): $dir/Dockerfile.prod"
            rm -f "$dir/Dockerfile.prod"
        fi
    fi
done

# List all property files still using the old format instead of YAML
echo "Listing property files that should be converted to YAML format..."
find $ROOT_DIR -name "application*.properties" | while read file; do
    echo "  Consider converting to YAML: $file"
done

# Standardize file permissions
echo "Standardizing file permissions..."
find $ROOT_DIR -name "*.sh" -type f -exec chmod +x {} \;

echo "Cleanup completed successfully!"