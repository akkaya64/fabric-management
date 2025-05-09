#!/bin/bash

# This script fixes common issues in POM files:
# 1. Replaces <n> tags with <name> tags
# 2. Adds compiler configuration to disable annotation processing

echo "Fixing POM files across the project..."

# Fix the security module first
SECURITY_POM="/Users/user/Coding/fabric-management/libraries/java/fabric-java-security/pom.xml"
FINANCE_POM="/Users/user/Coding/fabric-management/services/finance/finance-service/pom.xml"
AUTH_POM="/Users/user/Coding/fabric-management/services/identity/auth-service/pom.xml"

# Fix tags and add compiler config
for pom in $(find /Users/user/Coding/fabric-management -name "pom.xml"); do
  # Create a backup
  cp "$pom" "${pom}.bak"
  
  # Replace <n> with <name>
  perl -pi -e 's/<n>([^<]+)<\/n>/<name>$1<\/name>/g' "$pom"
done

# Add special compiler configuration to fabric-java-security pom.xml
# This is a targeted fix for the NoSuchFieldError compilation issue
perl -0777 -i -pe 's/<build>\s*<plugins>/<build>\n\t\t<plugins>\n\t\t\t<plugin>\n\t\t\t\t<groupId>org.apache.maven.plugins<\/groupId>\n\t\t\t\t<artifactId>maven-compiler-plugin<\/artifactId>\n\t\t\t\t<configuration>\n\t\t\t\t\t<source>\${java.version}<\/source>\n\t\t\t\t\t<target>\${java.version}<\/target>\n\t\t\t\t\t<compilerArgs>\n\t\t\t\t\t\t<arg>-proc:none<\/arg>\n\t\t\t\t\t<\/compilerArgs>\n\t\t\t\t<\/configuration>\n\t\t\t<\/plugin>/g' "$SECURITY_POM"

echo "Fixed POM files. Now you can run:"
echo "cd /Users/user/Coding/fabric-management"
echo "./scripts/build-libraries.sh"
echo "mvn clean install -DskipTests"