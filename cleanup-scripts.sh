#!/bin/bash

# Script to remove unnecessary one-time fix scripts
echo "Removing one-time fix scripts..."

# One-time fix scripts
rm -f /Users/user/Coding/fabric-management/fix-pom-names.sh
rm -f /Users/user/Coding/fabric-management/fix-api-response.sh
rm -f /Users/user/Coding/fabric-management/fix-maven-issues.sh
rm -f /Users/user/Coding/fabric-management/fix-lombok-issue.sh
rm -f /Users/user/Coding/fabric-management/fix-user-service-dependency.sh
rm -f /Users/user/Coding/fabric-management/fix-name-tags.sh
rm -f /Users/user/Coding/fabric-management/fix-findbugs-quick.sh
rm -f /Users/user/Coding/fabric-management/quick-fix-findbugs.sh

# Duplicative scripts that could be consolidated
# Note: We're not removing these yet since they should be consolidated first
# rm -f /Users/user/Coding/fabric-management/fix-poms.sh
# rm -f /Users/user/Coding/fabric-management/fix-maven-all.sh
# rm -f /Users/user/Coding/fabric-management/fix-findbugs-issue.sh
# rm -f /Users/user/Coding/fabric-management/clean-findbugs-imports.sh
# rm -f /Users/user/Coding/fabric-management/scripts/fix-pom-versions-direct.sh
# rm -f /Users/user/Coding/fabric-management/scripts/fix-pom-versions-mac.sh
# rm -f /Users/user/Coding/fabric-management/fix-maven-complete.sh
# rm -f /Users/user/Coding/fabric-management/fix-maven-project.sh

echo "Cleanup completed. One-time fix scripts have been removed."
echo "Note: Duplicative scripts that could be consolidated were not removed."
echo "Consider consolidating these scripts before removing the redundant ones."