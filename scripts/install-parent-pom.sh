#!/bin/bash

# Bu script, fabric-parent POM dosyasını direkt olarak Maven local repo'ya yükler

echo "Directly installing fabric-parent POM to local Maven repository..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Install the parent POM directly
mvn install:install-file \
  -Dfile=$ROOT_DIR/fabric-parent/pom.xml \
  -DpomFile=$ROOT_DIR/fabric-parent/pom.xml \
  -DgroupId=com.fabric \
  -DartifactId=fabric-parent \
  -Dversion=1.0.0-SNAPSHOT \
  -Dpackaging=pom

echo "fabric-parent POM installed to local Maven repository."