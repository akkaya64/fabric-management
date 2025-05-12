#!/bin/bash

set -e

echo "=== user-service bağımlılık sorununu çözme ==="

# Doğru sürümü kontrol et ve güncelle
echo "1. User service POM dosyasını güncelleme..."
sed -i.bak 's/<artifactId>fabric-java-security<\/artifactId>.*<version>0.0.1-SNAPSHOT<\/version>/<artifactId>fabric-java-security<\/artifactId>\n\t\t<version>1.0.0-SNAPSHOT<\/version>/g' services/identity/user-service/pom.xml

# fabric-java-security'yi -DskipTests ile yükle
echo "2. fabric-java-security modülünü testler olmadan derleme..."
cd libraries/java/fabric-java-security
mvn clean install -DskipTests -Dmaven.test.skip=true

# Maven yerel repo'da doğru sürümün olup olmadığını kontrol et
echo "3. Maven yerel repo'da fabric-java-security:1.0.0-SNAPSHOT dosyasını kontrol ediliyor..."
ls -la ~/.m2/repository/com/fabric/fabric-java-security/1.0.0-SNAPSHOT/

echo "4. User service testler olmadan derleniyor..."
cd ../../../services/identity/user-service
mvn clean compile -DskipTests

echo "=== İşlem tamamlandı ==="
echo "Şimdi şu komutu çalıştırın: mvn clean install -DskipTests"