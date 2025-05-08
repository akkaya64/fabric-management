#!/bin/bash

# Bu script, POM dosyalarında eksik olan sürüm numaralarını düzeltir

echo "Fixing missing version numbers in POM files..."

# Finance Service
echo "Fixing finance-service POM..."
FINANCE_POM="./services/finance/finance-service/pom.xml"
if [ -f "$FINANCE_POM" ]; then
    # springdoc bağımlılığını düzelt
    sed -i 's|<dependency>\n\s*<groupId>org.springdoc</groupId>\n\s*<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\s*</dependency>|<dependency>\n\t\t\t<groupId>org.springdoc</groupId>\n\t\t\t<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\t\t\t<version>2.3.0</version>\n\t\t</dependency>|g' "$FINANCE_POM"
    
    # jacoco plugin sürümünü ekle
    sed -i 's|<plugin>\n\s*<groupId>org.jacoco</groupId>\n\s*<artifactId>jacoco-maven-plugin</artifactId>\n\s*</plugin>|<plugin>\n\t\t\t\t\t<groupId>org.jacoco</groupId>\n\t\t\t\t\t<artifactId>jacoco-maven-plugin</artifactId>\n\t\t\t\t\t<version>0.8.10</version>\n\t\t\t\t</plugin>|g' "$FINANCE_POM"
    
    # owasp plugin sürümünü ekle
    sed -i 's|<plugin>\n\s*<groupId>org.owasp</groupId>\n\s*<artifactId>dependency-check-maven</artifactId>\n\s*</plugin>|<plugin>\n\t\t\t\t\t<groupId>org.owasp</groupId>\n\t\t\t\t\t<artifactId>dependency-check-maven</artifactId>\n\t\t\t\t\t<version>8.2.1</version>\n\t\t\t\t</plugin>|g' "$FINANCE_POM"
fi

# Employee Service
echo "Fixing employee-service POM..."
EMPLOYEE_POM="./services/hr/employee-service/pom.xml"
if [ -f "$EMPLOYEE_POM" ]; then
    # springdoc bağımlılığını düzelt
    sed -i 's|<dependency>\n\s*<groupId>org.springdoc</groupId>\n\s*<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\s*</dependency>|<dependency>\n\t\t\t<groupId>org.springdoc</groupId>\n\t\t\t<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\t\t\t<version>2.3.0</version>\n\t\t</dependency>|g' "$EMPLOYEE_POM"
    
    # jacoco plugin sürümünü ekle
    sed -i 's|<plugin>\n\s*<groupId>org.jacoco</groupId>\n\s*<artifactId>jacoco-maven-plugin</artifactId>\n\s*</plugin>|<plugin>\n\t\t\t\t\t<groupId>org.jacoco</groupId>\n\t\t\t\t\t<artifactId>jacoco-maven-plugin</artifactId>\n\t\t\t\t\t<version>0.8.10</version>\n\t\t\t\t</plugin>|g' "$EMPLOYEE_POM"
    
    # owasp plugin sürümünü ekle
    sed -i 's|<plugin>\n\s*<groupId>org.owasp</groupId>\n\s*<artifactId>dependency-check-maven</artifactId>\n\s*</plugin>|<plugin>\n\t\t\t\t\t<groupId>org.owasp</groupId>\n\t\t\t\t\t<artifactId>dependency-check-maven</artifactId>\n\t\t\t\t\t<version>8.2.1</version>\n\t\t\t\t</plugin>|g' "$EMPLOYEE_POM"
fi

# Company Service
echo "Fixing company-service POM..."
COMPANY_POM="./services/organization/company-service/pom.xml"
if [ -f "$COMPANY_POM" ]; then
    # springdoc bağımlılığını düzelt
    sed -i 's|<dependency>\n\s*<groupId>org.springdoc</groupId>\n\s*<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\s*</dependency>|<dependency>\n\t\t\t<groupId>org.springdoc</groupId>\n\t\t\t<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\t\t\t<version>2.3.0</version>\n\t\t</dependency>|g' "$COMPANY_POM"
    
    # jacoco plugin sürümünü ekle
    sed -i 's|<plugin>\n\s*<groupId>org.jacoco</groupId>\n\s*<artifactId>jacoco-maven-plugin</artifactId>\n\s*</plugin>|<plugin>\n\t\t\t\t\t<groupId>org.jacoco</groupId>\n\t\t\t\t\t<artifactId>jacoco-maven-plugin</artifactId>\n\t\t\t\t\t<version>0.8.10</version>\n\t\t\t\t</plugin>|g' "$COMPANY_POM"
    
    # owasp plugin sürümünü ekle
    sed -i 's|<plugin>\n\s*<groupId>org.owasp</groupId>\n\s*<artifactId>dependency-check-maven</artifactId>\n\s*</plugin>|<plugin>\n\t\t\t\t\t<groupId>org.owasp</groupId>\n\t\t\t\t\t<artifactId>dependency-check-maven</artifactId>\n\t\t\t\t\t<version>8.2.1</version>\n\t\t\t\t</plugin>|g' "$COMPANY_POM"
fi

echo "POM version fixes complete!"