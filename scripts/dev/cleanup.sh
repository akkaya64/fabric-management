#!/bin/bash

# 🧹 Safer Cleanup Script - scripts/dev/cleanup.sh
# Geliştirme ortamını temizler ama IDE yapı dosyalarına dokunmaz.

set -e

echo "🚀 Hafif temizlik başlatılıyor..."

# Sadece build çıktıları temizleniyor
find . -type d -name "target" -exec rm -rf {} +

# Maven metadata (build bilgisi)
find . -type d -name "maven-status" -exec rm -rf {} +
find . -type d -name "maven-archiver" -exec rm -rf {} +

# Geliştiriciya zarar vermeyen şeyler
find . -type f -name "HELP.md" -delete

echo "✅ Hafif temizlik tamamlandı. Kodların hâlâ ayakta."
echo "💡 Eğer IDE'yi patlatmak istersen, orijinal scripti tekrar kullanabilirsin."