package com.fabric.user_service.service;

public interface VerificationService {

    /**
     * Send email verification
     *
     * @param email Email to verify
     * @param token Verification token
     */
    void sendEmailVerification(String email, String token);

    /**
     * Send phone verification
     *
     * @param phone Phone number to verify
     * @param code Verification code
     */
    void sendPhoneVerification(String phone, String code);

    /**
     * Send password setup email
     *
     * @param email Email to send to
     * @param token Password setup token
     */
    void sendPasswordSetupEmail(String email, String token);

    /**
     * Send password reset email
     *
     * @param email Email to send to
     * @param token Password reset token
     */
    void sendPasswordResetEmail(String email, String token);
}