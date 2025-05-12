#!/bin/bash

# Fix the POMs with incorrect XML tags
echo "Fixing POM files..."

# Fix main pom.xml
sed -i.bak 's/<n>/<name>/g' /Users/user/Coding/fabric-management/pom.xml
sed -i.bak 's/<\/n>/<\/name>/g' /Users/user/Coding/fabric-management/pom.xml

# Fix fabric-parent pom.xml
sed -i.bak 's/<n>/<name>/g' /Users/user/Coding/fabric-management/fabric-parent/pom.xml
sed -i.bak 's/<\/n>/<\/name>/g' /Users/user/Coding/fabric-management/fabric-parent/pom.xml

# Fix fabric-java-security pom.xml
sed -i.bak 's/<n>/<name>/g' /Users/user/Coding/fabric-management/libraries/java/fabric-java-security/pom.xml
sed -i.bak 's/<\/n>/<\/name>/g' /Users/user/Coding/fabric-management/libraries/java/fabric-java-security/pom.xml

# Install the fabric-parent POM
echo "Installing fabric-parent POM..."
cd /Users/user/Coding/fabric-management/fabric-parent
mvn clean install -N

# Install the fabric-java-security library
echo "Building and installing fabric-java-security..."
cd /Users/user/Coding/fabric-management/libraries/java/fabric-java-security
mvn clean install

# Display usage instructions
echo ""
echo "Project fixed. To run the ApiResponse example, use:"
echo "cd /Users/user/Coding/fabric-management/"
echo "mvn -pl libraries/java/fabric-java-security spring-boot:run -Dspring-boot.run.main-class=com.fabric.fabric_java_security.ApiResponseExample"
echo ""
echo "Alternatively, you can try:"
echo "cd /Users/user/Coding/fabric-management/libraries/java/fabric-java-security"
echo "mvn exec:java -Dexec.mainClass=\"com.fabric.fabric_java_security.ApiResponseExample\""