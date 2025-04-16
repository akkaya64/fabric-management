package com.fabric.auth_service.service;

import com.fabric.auth_service.client.UserServiceClient;
import com.fabric.auth_service.exception.BadRequestException;
import com.fabric.auth_service.exception.ResourceNotFoundException;
import com.fabric.auth_service.payload.*;
import com.fabric.fabric_java_security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;

    // Cache for verification codes (In production, use Redis or similar)
    private final Map<String, VerificationData> verificationCodes = new HashMap<>();

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                       UserServiceClient userServiceClient, EmailService emailService,
                       SmsService smsService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userServiceClient = userServiceClient;
        this.emailService = emailService;
        this.smsService = smsService;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtAuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication.getName());

        return new JwtAuthResponse(jwt, refreshToken);
    }

    public void sendUserVerification(String userId) {
        try {
            UserResponse user = userServiceClient.getUserById(userId);
            String verificationCode = generateVerificationCode();
            String verificationToken = jwtProvider.generateVerificationToken(userId);

            // Store verification data (In production, use Redis or similar)
            verificationCodes.put(userId, new VerificationData(verificationCode, verificationToken));

            // Send verification code via email
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                emailService.sendVerificationEmail(user.getEmail(), verificationCode);
            }

            // Send verification code via SMS
            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                smsService.sendVerificationSms(user.getPhoneNumber(), verificationCode);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
    }

    public JwtAuthResponse verifyAndSetupPassword(PasswordSetupRequest request) {
        VerificationData verificationData = verificationCodes.get(request.getUserId());

        if (verificationData == null || !verificationData.getVerificationCode().equals(request.getVerificationCode())) {
            throw new BadRequestException("Invalid verification code");
        }

        // Update user password in User Service (using client)
        // This would involve a call to User Service
        // userServiceClient.updatePassword(request.getUserId(), passwordEncoder.encode(request.getPassword()));

        // Generate tokens
        String accessToken = jwtProvider.generateToken(request.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken(request.getUserId());

        // Remove verification data
        verificationCodes.remove(request.getUserId());

        return new JwtAuthResponse(accessToken, refreshToken);
    }

    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        boolean isValid = jwtProvider.validateToken(refreshTokenRequest.getRefreshToken());

        if (!isValid) {
            throw new BadRequestException("Invalid refresh token");
        }

        String userId = jwtProvider.getUserIdFromJWT(refreshTokenRequest.getRefreshToken());
        String newAccessToken = jwtProvider.generateToken(userId);

        return new JwtAuthResponse(newAccessToken, refreshTokenRequest.getRefreshToken());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }

    private static class VerificationData {
        private final String verificationCode;
        private final String verificationToken;

        public VerificationData(String verificationCode, String verificationToken) {
            this.verificationCode = verificationCode;
            this.verificationToken = verificationToken;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public String getVerificationToken() {
            return verificationToken;
        }
    }
}