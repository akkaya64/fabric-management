🧪 Test Stratejisi ve Uygulama Kılavuzu (testing.md)

🎯 Amaç

Bu dokümanın amacı, Fabric Management Platformu kapsamında geliştirilen mikroservislerin kalitesini güvence altına almak için test süreçlerini standartlaştırmaktır. Her test türü, belirli bir amaca hizmet eder ve CI/CD süreciyle entegre edilerek uçtan uca doğrulama sağlar.

🧱 Test Seviyeleri ve Tanımları

🔹 Unit Test (Birim Test)

Amaç: Bireysel metodların doğru çalıştığını test etmek.

İzolasyon: Tüm bağımlılıklar mock'lanır (Mockito / Mockk).

Konum: src/test/java/unit/

Araçlar: JUnit 5, Mockito, AssertJ

🔹 Integration Test (Entegrasyon Testi)

Amaç: Gerçek bileşenlerin bir arada doğru çalışmasını test etmek.

Örnek: Veritabanı işlemleri, REST client entegrasyonu.

Konum: src/test/java/integration/

Araçlar: SpringBootTest, Testcontainers, RestAssured

🔹 E2E Test (Uçtan Uca Test)

Amaç: Gerçek senaryolarda sistemin uçtan uca davranışını test etmek.

Örnek: Login → Token → Yetkili erişim → Veri güncelleme

Konum: src/test/java/e2e/

Araçlar: Cucumber, Selenium, Cypress

📁 Klasör Yapısı

Tüm servislerde aşağıdaki test klasör yapısı uygulanmalıdır:

src/test/java/
├── unit/
├── integration/
└── e2e/

Her test tipi için ayrı package kullanımı zorunludur. Kod ve test karışıklığı engellenmelidir.

🔄 Test Verisi ve Ortamlar

Test ortamı izole olmalı: PostgreSQL, Kafka vb. Testcontainers ile ayağa kaldırılır.

Mock server kullanımı önerilir: WireMock, MockServer vs.

CI ortamında docker-compose.test.yml kullanılabilir.

⚙️ CI/CD ile Entegrasyon

.github/workflows/test.yml altında aşağıdaki adımlar tanımlanır:

Birim test

Entegrasyon test

Kod coverage (JaCoCo)

Static code analysis (SonarQube - opsiyonel)

Test başarısızsa build kesilir, deploy yapılmaz.

📊 Raporlama

Coverage: JaCoCo ile rapor üretilmeli

HTML raporu: /target/site/jacoco/index.html

CI'da: Coverage % threshold'u minimum %80 olmalı

✅ Sonuç

Bu test stratejisi sayesinde:

Her modül, birim seviyede kontrol altına alınır.

Servisler, entegrasyon hatalarına karşı test edilir.

Uçtan uca senaryolar CI pipeline içinde güvence altına alınır.

Kod kalitesini sürdürülebilir kılmak için bu yapıya sadık kalınmalıdır.