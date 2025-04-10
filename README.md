🏗️ Fabric Management Platform - Ana Rehber (README.md)

📦 Proje Tanımı

Fabric Management Platform, kurumsal üretim, insan kaynakları, finans, lojistik ve satış süreçlerini dijitalleştirmek için mikroservis mimarisiyle geliştirilmiş bir sistemdir. Platform, modern yazılım mimarileri, açık standartlar ve geleceğe yönelik entegrasyon yetenekleriyle donatılmıştır.

🔍 Ana Özellikler

Domain-Driven Design (DDD) mimarisi

Mikroservis tabanlı servis organizasyonu

OpenAPI/Swagger destekli REST API'ler

OAuth2 ve JWT ile merkezi güvenlik altyapısı

Kafka ile event-driven mimari

PostgreSQL, Redis, Prometheus, Zipkin, OpenTelemetry

Flutter destekli mobil ve web arayüzleri

GitOps (ArgoCD) + Helm + Kubernetes deployment

🚀 Hızlı Başlangıç (Development)

# Gerekli Docker konteynerlerini başlat
$ docker-compose -f docker-compose.dev.yml up -d

# Servis örneği çalıştır (örnek: auth-service)
$ cd services/identity/auth-service
$ ./mvnw spring-boot:run

📁 Klasör Yapısı

📦 fabric-management
├── services/         # Mikroservisler (domain bazlı)
├── libraries/        # Ortak kütüphaneler
├── infrastructure/   # Gateway, monitoring, env, k8s
├── frontend/         # Web arayüzü (Flutter / React)
├── mobile-app/       # Flutter mobil uygulama
├── config/           # Ortam konfigürasyonları
├── charts/           # Helm chart'lar
├── scripts/          # Dev, CI, deploy script'leri
├── docs/             # Teknik ve operasyonel dokümantasyon
└── README.md

🧾 Belgelendirme

Tüm mimari ve teknik dökümantasyon docs/ klasörü altında yer alır:

🔹 Mimari

overview.md – Genel mimari yapı

structure.md – Proje klasör yapısı

security.md – Güvenlik stratejisi

tracing.md – Gözlemlenebilirlik & tracing

🔹 API

guidelines.md – REST/GraphQL API tasarımı

auth-service.openapi.yaml

user-service.openapi.yaml

🔹 Geliştirme

testing.md – Test stratejisi

versioning.md – Sürüm yönetimi

contributing.md – Katkı rehberi

🔹 Operasyon

gitops-argocd.md

helm-deployment.md

monitoring.md

ci-cd-pipeline.md

📦 Kurulum Gereksinimleri

Docker + Docker Compose

Java 17

Maven

Node.js (opsiyonel - frontend için)

Flutter SDK (mobil geliştirme için)

🤝 Katkıda Bulunma

Yeni servis geliştirmeden önce contributing.md rehberine göz atın. PR'larınız için guidelines.md kurallarına uyduğunuzdan emin olun.

🧠 Yazar Notu

Bu proje ölçeklenebilirlik, geliştirilebilirlik ve anlaşılabilirlik prensipleri üzerine kurulmuştur. Her yeni servis ve katkı, bu yapıyı güçlendirmek içindir.

"İyi yazılmış kod, iyi yazılmış belgelerle başlar."