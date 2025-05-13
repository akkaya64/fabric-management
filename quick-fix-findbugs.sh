#!/bin/bash

set -e

echo "=== Removing FindBugs annotations from Java files ==="

# Path to security module
SECURITY_MODULE="/Users/user/Coding/fabric-management/libraries/java/fabric-java-security"

# Find Java files in the security module
find "$SECURITY_MODULE/src" -name "*.java" -print0 | while IFS= read -r -d '' file; do
  echo "Processing: $file"
  # Remove any lines importing FindBugs annotations
  sed -i.bak '/import edu.umd.cs.findbugs.annotations/d' "$file"
  # Remove any SuppressFBWarnings annotations
  sed -i.bak '/@SuppressFBWarnings/d' "$file"
  # Remove backup files
  rm -f "$file.bak"
done

echo "=== Fix completed ==="