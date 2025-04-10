📘 Fabric Management Platform - Mimarinin Genel Görünümü (overview.md)

🎯 Amaç

Fabric Management Platformu, kurumsal düzeyde ölçeklenebilir, modüler ve geleceğe hazır bir sistem sunmayı amaçlayan mikroservis tabanlı bir çözümdür. Bu sistem, üretimden insan kaynaklarına, finanstan lojistiğe kadar farklı iş alanlarını bir araya getirerek entegre bir dijital omurga oluşturmayı hedefler.

🧱 Temel Mimarî Prensipler

🔹 Domain-Driven Design (DDD)

Her iş alanı (örneğin: hr, finance, production) kendi mikroservis alanına sahiptir. Bu servisler bağımsızdır, kendi veri tabanlarını tutar ve bounded context sınırları içinde çalışırlar.

🔹 Clean Architecture

Her servis kendi içinde api, application, domain, infrastructure gibi katmanlara ayrılarak kodun okunabilirliği ve sürdürülebilirliği artırılmıştır.

🔹 Microservice + Event-Driven

Servisler arası iletişim hem REST hem de Kafka gibi mesajlaşma sistemleri üzerinden sağlanır. Böylece loosely-coupled yapı korunur.

🔹 API Gateway

Tüm dış istekler bir API Gateway üzerinden yönlendirilir. Burada authentication, rate limiting, logging gibi işlemler merkezi olarak yönetilir.

🧩 Kullanılan Teknolojiler

Alan

Teknoloji / Araç

Backend Framework

Spring Boot 3.x, Java 17

Güvenlik

JWT, Spring Security, OAuth2

API Dokümantasyonu

springdoc-openapi (Swagger UI)

Observability

Prometheus, Grafana, Zipkin, OTel

Konteyner & Orkestrasyon

Docker, Kubernetes, Helm

GitOps

ArgoCD

Policy-as-Code

Open Policy Agent (OPA)

CI/CD

GitHub Actions

Veritabanı

PostgreSQL

Messaging

Apache Kafka

Frontend

Flutter, React

Mobil

Flutter (Android/iOS)

📂 Yapı Özeti

Platform, aşağıdaki temel klasör yapısıyla organize edilmiştir:

services/: Domain bazlı mikroservisler

libraries/: Ortak kullanılacak kütüphaneler

infrastructure/: API Gateway, monitoring, k8s dosyaları

docs/: Teknik ve operasyonel dokümantasyon

charts/: Helm chart'lar

config/: Ortam bazlı konfigürasyonlar

frontend/ ve mobile-app/: UI katmanı

✅ Hedef Avantajlar

Modülerlik: Her servis izole şekilde geliştirilebilir ve dağıtılabilir.

Şeffaf Gözlemlenebilirlik: OTel ve Prometheus ile izleme ve loglama kolaylığı.

Güvenlik: Yetkilendirme, merkezi token doğrulama ve RBAC mantığı.

Geleceğe Uyum: AI, mobil, web ve event tabanlı mimarilere entegre hazır.