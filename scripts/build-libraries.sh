#!/bin/bash

# Build common libraries and install them to local Maven repository

echo "Building and installing fabric-java-commons and fabric-java-security libraries..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Build and install fabric-java-commons
echo "Building fabric-java-commons..."
cd $ROOT_DIR/libraries/java/fabric-java-commons
./mvnw clean install -DskipTests

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "❌ Failed to build fabric-java-commons"
    exit 1
else
    echo "✅ Successfully built and installed fabric-java-commons"
fi

# Build and install fabric-java-security (depends on commons)
echo "Building fabric-java-security..."
cd $ROOT_DIR/libraries/java/fabric-java-security
./mvnw clean install -DskipTests

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "❌ Failed to build fabric-java-security"
    exit 1
else
    echo "✅ Successfully built and installed fabric-java-security"
fi

echo "Libraries have been successfully built and installed to local Maven repository."
echo "You can now build the microservices that depend on these libraries."