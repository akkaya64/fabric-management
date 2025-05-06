FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Maven bağımlılıklarının önbelleğe alınması
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Maven ile derleme
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Uygulamanın katmanlarını kopyala
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Uygulama kullanıcısı oluştur
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Uygulamayı çalıştır
ENTRYPOINT ["java","-cp","app:app/lib/*","com.fabric.weaving.WeavingServiceApplication"]

# Healthcheck ekle
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -q --spider http://localhost:8082/actuator/health || exit 1

# Port açıklaması
EXPOSE 8082