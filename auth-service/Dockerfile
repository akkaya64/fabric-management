FROM eclipse-temurin:17-jdk
WORKDIR /app

# Projeyi kopyala
COPY pom.xml .
COPY src ./src

# Maven ile build et
RUN apt-get update && apt-get install -y maven
RUN mvn package -DskipTests

# Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=0 /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=dev

HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]