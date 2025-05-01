package com.fabric.user_service.service.impl;

import com.fabric.user_service.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final JavaMailSender mailSender;

    @Value("${fabric.app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmailVerification(String email, String token) {
        String verificationUrl = baseUrl + "/api/users/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Please click the link below to verify your email:\n\n" + verificationUrl);

        try {
            mailSender.send(message);
            log.info("Email verification sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email verification to: {}", email, e);
        }
    }

    @Override
    public void sendPhoneVerification(String phone, String code) {
        // In a real implementation, this would integrate with an SMS service
        // For now, just log the code
        log.info("SMS verification code for {}: {}", phone, code);
    }

    @Override
    public void sendPasswordSetupEmail(String email, String token) {
        String passwordSetupUrl = baseUrl + "/set-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Set Up Your Password");
        message.setText("Welcome to Fabric Management Platform!\n\n" +
                "Please click the link below to set up your password:\n\n" +
                passwordSetupUrl + "\n\n" +
                "This link will expire in 7 days.");

        try {
            mailSender.send(message);
            log.info("Password setup email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password setup email to: {}", email, e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = baseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("You requested a password reset.\n\n" +
                "Please click the link below to reset your password:\n\n" +
                resetUrl + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you did not request this password reset, please ignore this email.");

        try {
            mailSender.send(message);
            log.info("Password reset email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", email, e);
        }
    }
}