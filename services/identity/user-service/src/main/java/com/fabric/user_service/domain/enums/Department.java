package com.fabric.user_service.domain.enums;

public enum Department {
    ORDER_MANAGEMENT("Sipariş Yönetimi"),
    PROCUREMENT("Satın Alma"),
    FABRIC_PRODUCTION("Kumaş Üretimi"),
    DYEING("Baskı / Boyahane"),
    QUALITY_CONTROL("Kalite Kontrol"),
    CUTTING("Kesimhane"),
    SEWING("Dikimhane"),
    PACKAGING("Paketleme"),
    STORAGE("Depolama"),
    WHOLESALE("Toptan Satış"),
    LOGISTICS("Lojistik"),
    IT("Bilgi Teknolojileri"),
    HR("İnsan Kaynakları"),
    FINANCE("Finans"),
    MARKETING("Pazarlama"),
    SALES("Satış"),
    OPERATIONS("Operasyon"),
    LEGAL("Hukuk"),
    EXECUTIVE("Yönetim");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
