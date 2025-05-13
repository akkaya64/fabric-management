#!/bin/bash

set -e

echo "=== Quick fix for FindBugs issues ==="

cd /Users/user/Coding/fabric-management/libraries/java/fabric-java-security

# Update the name property in pom.xml which has a typo
sed -i.bak 's/<n>fabric-java-security<\/n>/<name>fabric-java-security<\/name>/g' pom.xml

# Fix the dependencies in pom.xml - remove any duplicate entries we might have
sed -i.bak '/<groupId>com.google.code.findbugs<\/groupId>/,/<\/dependency>/d' pom.xml

# Add the correct dependency
sed -i.bak '/<!-- Lombok -->/i \
\t\t<!-- FindBugs Annotations -->\n\t\t<dependency>\n\t\t\t<groupId>com.github.spotbugs</groupId>\n\t\t\t<artifactId>spotbugs-annotations</artifactId>\n\t\t\t<version>4.7.3</version>\n\t\t</dependency>\n\t\t<dependency>\n\t\t\t<groupId>com.google.code.findbugs</groupId>\n\t\t\t<artifactId>jsr305</artifactId>\n\t\t\t<version>3.0.2</version>\n\t\t</dependency>\n' pom.xml

echo "Fix completed!"