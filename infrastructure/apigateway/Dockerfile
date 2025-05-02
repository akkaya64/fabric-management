FROM maven:3.8.7-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy dependencies first (for better caching)
COPY pom.xml .
COPY fabric-parent/pom.xml fabric-parent/
COPY libraries/java/fabric-java-security/pom.xml libraries/java/fabric-java-security/
COPY infrastructure/api-gateway/pom.xml infrastructure/api-gateway/

# Build dependencies without the actual source code
RUN mvn -B dependency:go-offline -pl infrastructure/api-gateway -am

# Copy source code
COPY libraries/java/fabric-java-security/src/ libraries/java/fabric-java-security/src/
COPY infrastructure/api-gateway/src/ infrastructure/api-gateway/src/

# Build the application
RUN mvn -B clean package -pl infrastructure/api-gateway -am -DskipTests

# Create final lightweight image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add Spring Boot health check agent
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.4.10/applicationinsights-agent-3.4.10.jar /app/applicationinsights-agent.jar
RUN chmod 644 /app/applicationinsights-agent.jar

# Copy the built JAR
COPY --from=build /app/infrastructure/api-gateway/target/*.jar /app/api-gateway.jar

# Set environment variables
ENV JAVA_TOOL_OPTIONS="-javaagent:/app/applicationinsights-agent.jar"
ENV SPRING_PROFILES_ACTIVE=prod

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]