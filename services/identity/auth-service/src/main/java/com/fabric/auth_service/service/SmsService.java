package com.fabric.auth_service.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    // Bu servis, gerçek bir SMS gateway'e bağlanmalıdır
    // Bu örnekte, sadece konsola çıktı vereceğiz

    public void sendVerificationSms(String phoneNumber, String verificationCode) {
        // Gerçek uygulamada burada bir SMS API'si kullanılmalıdır
        System.out.println("Sending SMS to " + phoneNumber + " with verification code: " + verificationCode);
    }
}
