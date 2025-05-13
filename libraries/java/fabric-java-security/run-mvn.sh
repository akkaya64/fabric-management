#!/bin/bash

# Folder for Java temp files
mkdir -p /tmp/java-build

# Run maven with temp dir to avoid long paths
cd /Users/user/Coding/fabric-management/libraries/java/fabric-java-security
JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=/tmp/java-build" mvn clean install -DskipTests