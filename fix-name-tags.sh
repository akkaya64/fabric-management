#!/bin/bash

# Tüm POM dosyalarında n etiketlerini name olarak düzelt
find . -name "pom.xml" -type f -exec sed -i.bak 's/<n>/<name>/g' {} \;
find . -name "pom.xml" -type f -exec sed -i.bak 's/<\/n>/<\/name>/g' {} \;

# user-service bağımlılık sürümünü güncelle
sed -i.bak 's/<artifactId>fabric-java-security<\/artifactId>.*<version>0.0.1-SNAPSHOT<\/version>/<artifactId>fabric-java-security<\/artifactId>\n\t\t<version>1.0.0-SNAPSHOT<\/version>/g' services/identity/user-service/pom.xml

# fabric-java-security modülünü derle
cd libraries/java/fabric-java-security
mvn clean install -DskipTests -Dmaven.test.skip=true

# Ana dizine dön
cd ../../..

echo "POM etiketleri düzeltildi ve derleme yapıldı."
echo "Şimdi projeyi şu komutla derleyin: mvn clean install -DskipTests"