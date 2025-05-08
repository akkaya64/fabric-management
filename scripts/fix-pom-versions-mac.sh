#!/bin/bash

# Bu script, POM dosyalarındaki eksik sürüm bilgilerini doğrudan ekler (MacOS uyumlu)
echo "Fixing missing version numbers in POM files (MacOS version)..."

# Finance Service
echo "Fixing finance-service POM..."
FINANCE_POM="/Users/user/Coding/fabric-management/services/finance/finance-service/pom.xml"
if [ -f "$FINANCE_POM" ]; then
    # springdoc sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>)(\s*</dependency>)|$1\n\t\t\t<version>2.3.0</version>$2|g' "$FINANCE_POM"
    echo "Added springdoc version to finance-service POM"
    
    # jacoco plugin sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>jacoco-maven-plugin</artifactId>)(\s*</plugin>)|$1\n\t\t\t\t\t<version>0.8.10</version>$2|g' "$FINANCE_POM"
    echo "Added jacoco plugin version to finance-service POM"
    
    # owasp plugin sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>dependency-check-maven</artifactId>)(\s*</plugin>)|$1\n\t\t\t\t\t<version>8.2.1</version>$2|g' "$FINANCE_POM"
    echo "Added owasp plugin version to finance-service POM"
fi

# Employee Service
echo "Fixing employee-service POM..."
EMPLOYEE_POM="/Users/user/Coding/fabric-management/services/hr/employee-service/pom.xml"
if [ -f "$EMPLOYEE_POM" ]; then
    # springdoc sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>)(\s*</dependency>)|$1\n\t\t\t<version>2.3.0</version>$2|g' "$EMPLOYEE_POM"
    echo "Added springdoc version to employee-service POM"
    
    # jacoco plugin sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>jacoco-maven-plugin</artifactId>)(\s*</plugin>)|$1\n\t\t\t\t\t<version>0.8.10</version>$2|g' "$EMPLOYEE_POM"
    echo "Added jacoco plugin version to employee-service POM"
    
    # owasp plugin sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>dependency-check-maven</artifactId>)(\s*</plugin>)|$1\n\t\t\t\t\t<version>8.2.1</version>$2|g' "$EMPLOYEE_POM"
    echo "Added owasp plugin version to employee-service POM"
fi

# Company Service
echo "Fixing company-service POM..."
COMPANY_POM="/Users/user/Coding/fabric-management/services/organization/company-service/pom.xml"
if [ -f "$COMPANY_POM" ]; then
    # springdoc sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>)(\s*</dependency>)|$1\n\t\t\t<version>2.3.0</version>$2|g' "$COMPANY_POM"
    echo "Added springdoc version to company-service POM"
    
    # jacoco plugin sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>jacoco-maven-plugin</artifactId>)(\s*</plugin>)|$1\n\t\t\t\t\t<version>0.8.10</version>$2|g' "$COMPANY_POM"
    echo "Added jacoco plugin version to company-service POM"
    
    # owasp plugin sürümünü doğrudan XML dosyasına ekle
    perl -i -pe 's|(<artifactId>dependency-check-maven</artifactId>)(\s*</plugin>)|$1\n\t\t\t\t\t<version>8.2.1</version>$2|g' "$COMPANY_POM"
    echo "Added owasp plugin version to company-service POM"
fi

echo "All POM version fixes complete!"

# GitHub Actions için aynı scriptin Linux/Ubuntu uyumlu versiyonunu da oluşturalım
echo "Creating Linux/GitHub Actions compatible version..."
cat > /Users/user/Coding/fabric-management/scripts/fix-pom-versions.sh << 'EOL'
#!/bin/bash

# Bu script, POM dosyalarındaki eksik sürüm bilgilerini doğrudan ekler (Linux uyumlu)
echo "Fixing missing version numbers in POM files (Linux version)..."

# Finance Service
echo "Fixing finance-service POM..."
FINANCE_POM="./services/finance/finance-service/pom.xml"
if [ -f "$FINANCE_POM" ]; then
    # springdoc sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>|<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\t\t\t<version>2.3.0</version>|g' "$FINANCE_POM"
    echo "Added springdoc version to finance-service POM"
    
    # jacoco plugin sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>jacoco-maven-plugin</artifactId>|<artifactId>jacoco-maven-plugin</artifactId>\n\t\t\t\t\t<version>0.8.10</version>|g' "$FINANCE_POM"
    echo "Added jacoco plugin version to finance-service POM"
    
    # owasp plugin sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>dependency-check-maven</artifactId>|<artifactId>dependency-check-maven</artifactId>\n\t\t\t\t\t<version>8.2.1</version>|g' "$FINANCE_POM"
    echo "Added owasp plugin version to finance-service POM"
fi

# Employee Service
echo "Fixing employee-service POM..."
EMPLOYEE_POM="./services/hr/employee-service/pom.xml"
if [ -f "$EMPLOYEE_POM" ]; then
    # springdoc sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>|<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\t\t\t<version>2.3.0</version>|g' "$EMPLOYEE_POM"
    echo "Added springdoc version to employee-service POM"
    
    # jacoco plugin sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>jacoco-maven-plugin</artifactId>|<artifactId>jacoco-maven-plugin</artifactId>\n\t\t\t\t\t<version>0.8.10</version>|g' "$EMPLOYEE_POM"
    echo "Added jacoco plugin version to employee-service POM"
    
    # owasp plugin sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>dependency-check-maven</artifactId>|<artifactId>dependency-check-maven</artifactId>\n\t\t\t\t\t<version>8.2.1</version>|g' "$EMPLOYEE_POM"
    echo "Added owasp plugin version to employee-service POM"
fi

# Company Service
echo "Fixing company-service POM..."
COMPANY_POM="./services/organization/company-service/pom.xml"
if [ -f "$COMPANY_POM" ]; then
    # springdoc sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>|<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>\n\t\t\t<version>2.3.0</version>|g' "$COMPANY_POM"
    echo "Added springdoc version to company-service POM"
    
    # jacoco plugin sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>jacoco-maven-plugin</artifactId>|<artifactId>jacoco-maven-plugin</artifactId>\n\t\t\t\t\t<version>0.8.10</version>|g' "$COMPANY_POM"
    echo "Added jacoco plugin version to company-service POM"
    
    # owasp plugin sürümünü doğrudan XML dosyasına ekle
    sed -i 's|<artifactId>dependency-check-maven</artifactId>|<artifactId>dependency-check-maven</artifactId>\n\t\t\t\t\t<version>8.2.1</version>|g' "$COMPANY_POM"
    echo "Added owasp plugin version to company-service POM"
fi

echo "All POM version fixes complete!"
EOL

chmod +x /Users/user/Coding/fabric-management/scripts/fix-pom-versions.sh
echo "Created Linux compatible script for GitHub Actions"