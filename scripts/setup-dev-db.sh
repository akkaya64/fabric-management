#!/bin/bash

# Renk tanımlamaları
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Fabric Management Development Setup ===${NC}"

# Tüm servis dizinlerini bul
SERVICES=$(find ./domains -type d -name "*-service" -o -name "api-gateway")

for SERVICE in $SERVICES; do
    echo -e "${YELLOW}Configuring $SERVICE...${NC}"

    # application.properties kontrol et
    PROP_FILE="$SERVICE/src/main/resources/application.properties"
    if [ ! -f "$PROP_FILE" ]; then
        echo -e "${RED}$PROP_FILE not found. Creating...${NC}"
        mkdir -p "$SERVICE/src/main/resources"
        cat > "$PROP_FILE" << EOL
# Veritabanı Bağlantısı
spring.datasource.url=jdbc:postgresql://localhost:5432/fabric
spring.datasource.username=fabric
spring.datasource.password=fabric123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Yapılandırması
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Uygulama Portu
server.port=8080

# Profil Ayarları
spring.profiles.active=dev
EOL
        echo -e "${GREEN}Created $PROP_FILE${NC}"
    else
        echo -e "${GREEN}$PROP_FILE already exists${NC}"
    fi

    # H2 için dev profili
    DEV_PROP_FILE="$SERVICE/src/main/resources/application-dev.properties"
    if [ ! -f "$DEV_PROP_FILE" ]; then
        echo -e "${RED}$DEV_PROP_FILE not found. Creating...${NC}"
        cat > "$DEV_PROP_FILE" << EOL
# Development için H2
spring.datasource.url=jdbc:h2:mem:devdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
EOL
        echo -e "${GREEN}Created $DEV_PROP_FILE${NC}"
    else
        echo -e "${GREEN}$DEV_PROP_FILE already exists${NC}"
    fi

    # H2 bağımlılığının pom.xml dosyasına eklenip eklenmediğini kontrol et
    POM_FILE="$SERVICE/pom.xml"
    if [ -f "$POM_FILE" ]; then
        if ! grep -q "h2database" "$POM_FILE"; then
            echo -e "${YELLOW}Adding H2 dependency to $POM_FILE...${NC}"
            sed -i.bak '/<\/dependencies>/i \
        <!-- H2 Database (Development) --> \
        <dependency> \
            <groupId>com.h2database</groupId> \
            <artifactId>h2</artifactId> \
            <scope>runtime</scope> \
        </dependency>' "$POM_FILE"
            rm "$POM_FILE.bak"
            echo -e "${GREEN}Added H2 dependency to $POM_FILE${NC}"
        else
            echo -e "${GREEN}H2 dependency already exists in $POM_FILE${NC}"
        fi
    else
        echo -e "${RED}$POM_FILE not found${NC}"
    fi
done

echo -e "${GREEN}=== Setup Complete ===${NC}"
echo -e "${YELLOW}To run a specific service, use:${NC}"
echo -e "${GREEN}cd [service-directory] && mvn spring-boot:run -Dspring-boot.run.profiles=dev${NC}"