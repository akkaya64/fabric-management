#!/bin/bash

# Script to optimize and deduplicate configuration files

echo "Starting configuration optimization..."

# Root directory of the project
ROOT_DIR=$(pwd)

# Create a clean configurations directory structure
echo "Creating standardized configurations..."
mkdir -p $ROOT_DIR/infrastructure/config/base
mkdir -p $ROOT_DIR/infrastructure/config/dev
mkdir -p $ROOT_DIR/infrastructure/config/prod
mkdir -p $ROOT_DIR/infrastructure/config/test

# Create base application configuration template
cat > $ROOT_DIR/infrastructure/config/base/application.yml << EOF
# Base application configuration shared by all services
spring:
  application:
    name: \${service.name}
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: false
  kafka:
    bootstrap-servers: kafka:9092
    consumer:
      auto-offset-reset: earliest
  cloud:
    consul:
      host: consul
      port: 8500
      discovery:
        prefer-ip-address: true
        instance-id: \${spring.application.name}:\${server.port}
  zipkin:
    base-url: http://zipkin:9411
  sleuth:
    sampler:
      probability: 1.0

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
  metrics:
    tags:
      application: \${spring.application.name}
    export:
      prometheus:
        enabled: true

logging:
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} %highlight(%-5level) [%thread] %cyan(%logger{36}) - %msg%n"
  logstash:
    enabled: true
    host: logstash
    port: 5000
  level:
    root: INFO
    org.springframework: INFO
    com.fabric: INFO
EOF

# Create development environment configuration
cat > $ROOT_DIR/infrastructure/config/dev/application-dev.yml << EOF
# Development environment configuration
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  datasource:
    driver-class-name: org.postgresql.Driver

logging:
  level:
    root: INFO
    org.springframework: INFO
    com.fabric: DEBUG
    org.hibernate.SQL: DEBUG

management:
  endpoints:
    web:
      exposure:
        include: "*"
EOF

# Create production environment configuration
cat > $ROOT_DIR/infrastructure/config/prod/application-prod.yml << EOF
# Production environment configuration
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  datasource:
    driver-class-name: org.postgresql.Driver

logging:
  level:
    root: WARN
    org.springframework: WARN
    com.fabric: INFO

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
EOF

# Create test environment configuration
cat > $ROOT_DIR/infrastructure/config/test/application-test.yml << EOF
# Test environment configuration
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  datasource:
    driver-class-name: org.postgresql.Driver

logging:
  level:
    root: INFO
    org.springframework: INFO
    com.fabric: DEBUG
    org.hibernate.SQL: DEBUG

management:
  endpoints:
    web:
      exposure:
        include: "*"
EOF

# Copy configuration to each service
echo "Applying optimized configurations to services..."
for service_dir in $(find $ROOT_DIR/services -type d -name "*-service"); do
    service_name=$(basename "$service_dir")
    resources_dir="$service_dir/src/main/resources"
    
    if [ -d "$resources_dir" ]; then
        echo "Optimizing configuration for $service_name..."
        
        # Create a customized application.yml for this service
        mkdir -p "$resources_dir/config"
        cp $ROOT_DIR/infrastructure/config/base/application.yml "$resources_dir/config/"
        cp $ROOT_DIR/infrastructure/config/dev/application-dev.yml "$resources_dir/config/"
        cp $ROOT_DIR/infrastructure/config/prod/application-prod.yml "$resources_dir/config/"
        cp $ROOT_DIR/infrastructure/config/test/application-test.yml "$resources_dir/config/"
        
        # Create service-specific yml (only if it doesn't exist)
        if [ ! -f "$resources_dir/application.yml" ]; then
            # Extract port from existing files or use a default
            port=8080
            if grep -q "server.port" "$resources_dir"/*.properties 2>/dev/null; then
                port=$(grep "server.port" "$resources_dir"/*.properties | head -1 | cut -d'=' -f2 | tr -d ' ')
            elif grep -q "port:" "$resources_dir"/*.yml 2>/dev/null; then
                port=$(grep "port:" "$resources_dir"/*.yml | head -1 | cut -d':' -f2 | tr -d ' ')
            fi
            
            # Create minimal application.yml that imports the base config
            cat > "$resources_dir/application.yml" << EOF
service:
  name: $service_name

server:
  port: $port

spring:
  config:
    import: classpath:config/application.yml
  profiles:
    active: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/fabric_${service_name//-/_}_db
    username: fabric
    password: fabric123
EOF
        fi
    fi
done

echo "Configuration optimization completed!"
echo "New standardized configurations are in infrastructure/config/"
echo "Each service now has optimized configurations."