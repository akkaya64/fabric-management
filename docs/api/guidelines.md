API Tasarım Rehberi (guidelines.md)

🎯 Amaç

Bu belge, Fabric Management Platformu kapsamında geliştirilen tüm RESTful ve (opsiyonel) GraphQL API’ler için ortak bir standart belirlemek amacıyla hazırlanmıştır. Bu standartlar, API’lerin anlaşılabilirliğini, sürdürülebilirliğini ve dış entegrasyonlara açıklığını artırmayı hedefler.

🌐 REST API Standartları

📛 URL Tasarımı

Tüm endpoint’ler küçük harfli ve çoğul isimlendirilmelidir.

/api/v1/users
/api/v1/companies

Hiyerarşi açıkça yansıtılmalıdır:

/api/v1/companies/{companyId}/employees

📦 HTTP Metotları

İşlem

HTTP Metodu

Listeleme

GET

Detay

GET

Oluşturma

POST

Güncelleme

PUT / PATCH

Silme

DELETE

💬 Standart Response Formatı

Tüm cevaplar aşağıdaki JSON şemasına uymalıdır:

{
"success": true,
"data": { ... },
"errors": [],
"timestamp": "2025-04-09T12:34:56Z"
}

🧭 Versiyonlama

Versiyonlama mutlaka URL üzerinden yapılmalıdır:

/api/v1/...

Major versiyon değişikliği varsa v2, v3 gibi sürümler oluşturulmalıdır.

🔐 Güvenlik

Tüm endpoint’ler varsayılan olarak güvenli olmalı, JWT token ile erişilmelidir.

Role-Based Access Control (RBAC) mekanizması uygulanmalıdır.

Swagger dokümantasyonlarında auth header örneği gösterilmelidir:

Authorization: Bearer <jwt_token>

📑 Swagger / OpenAPI Desteği

Tüm servislerde springdoc-openapi entegrasyonu yapılmalıdır.

/v3/api-docs ve /swagger-ui.html endpoint’leri açık olmalıdır.

Dokümantasyon, DTO açıklamaları ve örnek response’larla desteklenmelidir.

🔄 GraphQL (Opsiyonel)

GraphQL endpoint /graphql altında sunulmalıdır.

Query ve Mutation ayrımı belirgin olmalıdır.

Playground UI veya Altair gibi bir tarayıcı arayüzü ile test edilebilir olmalıdır.

✅ Sonuç

Bu API tasarım kuralları, platformdaki tüm servislerin ortak bir dil ile çalışmasını sağlar. Bu belge zaman içinde yeni ihtiyaçlara göre güncellenecektir. Yeni bir servis geliştirirken bu belge referans alınmalıdır.