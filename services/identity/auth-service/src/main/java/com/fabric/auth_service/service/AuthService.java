package com.fabric.auth_service.service;

import com.fabric.auth_service.payload.request.CompanySelectionRequest;
import com.fabric.auth_service.payload.request.LoginRequest;
import com.fabric.auth_service.payload.request.ResetPasswordRequest;
import com.fabric.auth_service.payload.request.SetPasswordRequest;
import com.fabric.auth_service.payload.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AuthResponse login(LoginRequest loginRequest) {
        // TODO: kullanıcıyı doğrula, token oluştur, dön
        return AuthResponse.builder()
                .accessToken("jwt-access-token")
                .refreshToken("jwt-refresh-token")
                .userId(123L)
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // TODO: refresh token kontrolü, yeni access token
        return AuthResponse.builder()
                .accessToken("new-jwt-access-token")
                .refreshToken(request.getRefreshToken())
                .userId(request.getUserId())
                .build();
    }

    public void logout(LogoutRequest request) {
        // TODO: access/refresh token’ı blacklist’e ata
    }

    public AuthResponse selectCompany(CompanySelectionRequest request) {
        // TODO: user-company ilişki kontrolü, context olarak ayarla
        return login(request.toLoginRequest()); // basit örnek
    }

    public void setPassword(SetPasswordRequest request) {
        // TODO: ilk şifre ayarlama
    }

    public void resetPassword(ResetPasswordRequest request) {
        // TODO: reset e-postası/SMS’i gönder
    }

    public void changePassword(ChangePasswordRequest request) {
        // TODO: eski şifre kontrolü, yeni şifre kaydet
    }

    public void verifyEmail(VerifyEmailRequest request) {
        // TODO: email token doğrula
    }

    public void verifyPhone(VerifyPhoneRequest request) {
        // TODO: SMS kod doğrula
    }
}
