#!/bin/bash

# Bu script, POM dosyalarındaki eksik sürüm numaralarını doğrudan düzeltecek

echo "Fixing missing version numbers in POM files using direct edits..."

# Finance Service
echo "Fixing finance-service POM..."
FINANCE_POM="./services/finance/finance-service/pom.xml"
if [ -f "$FINANCE_POM" ]; then
    # springdoc-openapi ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>" "$FINANCE_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$FINANCE_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\				<version>2.3.0</version>" "$FINANCE_POM"
            echo "Added springdoc version to finance-service POM"
        fi
    fi
    
    # jacoco plugin ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>jacoco-maven-plugin</artifactId>" "$FINANCE_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$FINANCE_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\					<version>0.8.10</version>" "$FINANCE_POM"
            echo "Added jacoco plugin version to finance-service POM"
        fi
    fi
    
    # owasp plugin ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>dependency-check-maven</artifactId>" "$FINANCE_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$FINANCE_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\					<version>8.2.1</version>" "$FINANCE_POM"
            echo "Added owasp plugin version to finance-service POM"
        fi
    fi
fi

# Employee Service
echo "Fixing employee-service POM..."
EMPLOYEE_POM="./services/hr/employee-service/pom.xml"
if [ -f "$EMPLOYEE_POM" ]; then
    # springdoc-openapi ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>" "$EMPLOYEE_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$EMPLOYEE_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\				<version>2.3.0</version>" "$EMPLOYEE_POM"
            echo "Added springdoc version to employee-service POM"
        fi
    fi
    
    # jacoco plugin ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>jacoco-maven-plugin</artifactId>" "$EMPLOYEE_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$EMPLOYEE_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\					<version>0.8.10</version>" "$EMPLOYEE_POM"
            echo "Added jacoco plugin version to employee-service POM"
        fi
    fi
    
    # owasp plugin ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>dependency-check-maven</artifactId>" "$EMPLOYEE_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$EMPLOYEE_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\					<version>8.2.1</version>" "$EMPLOYEE_POM"
            echo "Added owasp plugin version to employee-service POM"
        fi
    fi
fi

# Company Service
echo "Fixing company-service POM..."
COMPANY_POM="./services/organization/company-service/pom.xml"
if [ -f "$COMPANY_POM" ]; then
    # springdoc-openapi ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>" "$COMPANY_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$COMPANY_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\				<version>2.3.0</version>" "$COMPANY_POM"
            echo "Added springdoc version to company-service POM"
        fi
    fi
    
    # jacoco plugin ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>jacoco-maven-plugin</artifactId>" "$COMPANY_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$COMPANY_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\					<version>0.8.10</version>" "$COMPANY_POM"
            echo "Added jacoco plugin version to company-service POM"
        fi
    fi
    
    # owasp plugin ekleme işlemi
    LINE_NUM=$(grep -n "<artifactId>dependency-check-maven</artifactId>" "$COMPANY_POM" | cut -d: -f1)
    if [ ! -z "$LINE_NUM" ]; then
        NEXT_LINE=$((LINE_NUM + 1))
        # Önce sürüm satırı var mı kontrol et
        VERSION_CHECK=$(sed -n "${NEXT_LINE}p" "$COMPANY_POM" | grep "<version>")
        if [ -z "$VERSION_CHECK" ]; then
            # Sürüm satırı yoksa ekle
            sed -i "${LINE_NUM}a\\					<version>8.2.1</version>" "$COMPANY_POM"
            echo "Added owasp plugin version to company-service POM"
        fi
    fi
fi

echo "All POM version fixes complete!"