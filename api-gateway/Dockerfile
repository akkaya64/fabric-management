# Birinci aşama: Derleme
FROM maven:3.8.6-eclipse-temurin-17-focal as build
WORKDIR /app

# Ana projeyi ve tüm modülleri kopyala
COPY pom.xml .
COPY fabric-parent/ ./fabric-parent/
COPY libraries/ ./libraries/
COPY api-gateway/ ./api-gateway/
COPY auth-service/ ./auth-service/
COPY java-services/ ./java-services/

# Önce parent ve library modüllerini derle
RUN mvn -B -f fabric-parent/pom.xml clean install -DskipTests
RUN mvn -B -f libraries/fabric-java-commons/pom.xml clean install -DskipTests
RUN mvn -B -f libraries/fabric-java-security/pom.xml clean install -DskipTests

# Şimdi ilgili servisi derle (örnek: api-gateway için)
RUN mvn -B -f api-gateway/pom.xml clean package -DskipTests

# İkinci aşama: Çalışma zamanı
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/api-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]