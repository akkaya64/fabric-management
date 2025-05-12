#!/bin/bash

# Find all pom.xml files and fix <n> tags to <name> tags
find . -name "pom.xml" -type f -exec sed -i'.bak' 's/<n>/<name>/g; s/<\/n>/<\/name>/g' {} \;

# Remove backup files
find . -name "*.bak" -type f -delete

echo "Fixed all pom.xml files with correct <name> tags"