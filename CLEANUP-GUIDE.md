# Fabric Management Project Cleanup Guide

Bu belge, Fabric Management projesinde gereksiz ve tekrarlanan dosyaları temizlemek için hazırlanmıştır.

## Temizlik İşlemleri

Projede aşağıdaki gereksiz dosya ve kodları temizlemek için üç farklı script oluşturuldu:

### 1. Gereksiz Dosyaları Temizleme (`cleanup-project.sh`)

Bu script şu işlemleri yapar:
- Geçici ve yedek dosyaları siler (`.bak`, `.tmp`, `~`, `.swp`)
- Build artefactlarını temizler (`target` dizinleri)
- YML dosyaları ile aynı isimde olan properties dosyalarını siler
- Kullanılmayan Docker Compose dosyalarını kaldırır
- IDE dosyalarını temizler (`.idea`, `.DS_Store`)
- Boş dizinleri kaldırır
- Standart bir `.gitignore` dosyası oluşturur
- Tekrarlanan Dockerfile'ları temizler
- Dosya izinlerini standartlaştırır

```bash
cd /Users/user/Coding/fabric-management
./scripts/cleanup-project.sh
```

### 2. Yapılandırma Dosyalarını Optimize Etme (`optimize-configs.sh`)

Bu script şu işlemleri yapar:
- Tüm servislerde kullanılacak standart bir konfigürasyon dizin yapısı oluşturur
- Temel `application.yml` şablonu oluşturur
- Ortam bazlı yapılandırma dosyaları oluşturur (dev, prod, test)
- Her servis için özelleştirilmiş yapılandırma dosyaları oluşturur
- Gereksiz tekrarları önlemek için import kullanır

```bash
cd /Users/user/Coding/fabric-management
./scripts/optimize-configs.sh
```

### 3. Docker ve Kubernetes Yapılandırmalarını Temizleme (`remove-duplicate-dockerfiles.sh`)

Bu script şu işlemleri yapar:
- Dockerfile'ları standartlaştırır
- Tekrarlanan Dockerfile varyantlarını kaldırır
- Servislerdeki gereksiz docker-compose dosyalarını temizler
- Kubernetes yapılandırmalarındaki tekrarları kaldırır

```bash
cd /Users/user/Coding/fabric-management
./scripts/remove-duplicate-dockerfiles.sh
```

## Scriptlerin Çalıştırılması

Her scripti çalıştırmadan önce, dosya izinlerini ayarlamanız gerekir:

```bash
chmod +x scripts/cleanup-project.sh
chmod +x scripts/optimize-configs.sh
chmod +x scripts/remove-duplicate-dockerfiles.sh
```

Ardından scriptleri sırayla çalıştırabilirsiniz:

```bash
./scripts/cleanup-project.sh
./scripts/optimize-configs.sh
./scripts/remove-duplicate-dockerfiles.sh
```

## Manuel Kontrol Edilmesi Gereken Dosyalar

Scriptler otomatik temizleme işlemleri yapar, ancak aşağıdaki dosyaları manuel olarak kontrol etmeniz önerilir:

1. İçeriği boş veya minimal olan dosyalar
2. "archived" etiketli veya eski sürümlerin kopyaları olabilecek dosyalar
3. Her mikroservis için gerekmeyebilecek yapılandırma dosyaları
4. Her service klasöründe bulunan ve aynı amaçla kullanılan duplicate controller, service vb. sınıflar

## Notlar

- Bu scriptler, mevcut çalışan kodu değiştirmez, sadece gereksiz dosyaları kaldırır ve yapılandırmaları standartlaştırır.
- Temizlik işlemi sonrasında, projenin çalıştığından emin olmak için testleri çalıştırmanız önerilir.