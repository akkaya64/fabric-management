FROM maven:3.8.7-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy dependencies first (for better caching)
COPY pom.xml .
COPY fabric-parent/pom.xml fabric-parent/
COPY libraries/java/fabric-java-security/pom.xml libraries/java/fabric-java-security/
COPY services/identity/user-service/pom.xml services/identity/user-service/

# Build dependencies without the actual source code
RUN mvn -B dependency:go-offline -pl services/identity/user-service -am

# Copy source code
COPY libraries/java/fabric-java-security/src/ libraries/java/fabric-java-security/src/
COPY services/identity/user-service/src/ services/identity/user-service/src/

# Build the application
RUN mvn -B clean package -pl services/identity/user-service -am -DskipTests

# Create final lightweight image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add Spring Boot health check agent
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.4.10/applicationinsights-agent-3.4.10.jar /app/applicationinsights-agent.jar
RUN chmod 644 /app/applicationinsights-agent.jar

# Copy the built JAR
COPY --from=build /app/services/identity/user-service/target/*.jar /app/user-service.jar

# Set environment variables
ENV JAVA_TOOL_OPTIONS="-javaagent:/app/applicationinsights-agent.jar"
ENV SPRING_PROFILES_ACTIVE=prod

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]