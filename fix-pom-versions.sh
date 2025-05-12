#!/bin/bash

echo "Updating dependency versions in POM files..."

# Fix user-service pom.xml - update fabric-java-security version
sed -i.bak 's/<artifactId>fabric-java-security<\/artifactId>\n\s*<version>0.0.1-SNAPSHOT<\/version>/<artifactId>fabric-java-security<\/artifactId>\n\t\t<version>1.0.0-SNAPSHOT<\/version>/g' /Users/user/Coding/fabric-management/services/identity/user-service/pom.xml

# Fix any invalid name tags
find /Users/user/Coding/fabric-management -name "pom.xml" -type f -exec sed -i.bak 's/<n>/<name>/g' {} \;
find /Users/user/Coding/fabric-management -name "pom.xml" -type f -exec sed -i.bak 's/<\/n>/<\/name>/g' {} \;

# Install the fabric-parent POM
echo "Installing fabric-parent POM..."
cd /Users/user/Coding/fabric-management/fabric-parent
mvn clean install -N

# Install the fabric-java-commons library
echo "Building and installing fabric-java-commons..."
cd /Users/user/Coding/fabric-management/libraries/java/fabric-java-commons
mvn clean install -DskipTests

# Install the fabric-java-security library
echo "Building and installing fabric-java-security..."
cd /Users/user/Coding/fabric-management/libraries/java/fabric-java-security
mvn clean install -DskipTests

echo "Done! Now try building the services again."