package com.fabric.auth_service.controller;

import com.fabric.auth_service.payload.*;
import com.fabric.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse jwtAuthResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/send-verification/{userId}")
    public ResponseEntity<ApiResponse> sendVerification(@PathVariable String userId) {
        authService.sendUserVerification(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Verification code sent successfully"));
    }

    @PostMapping("/verify-setup-password")
    public ResponseEntity<JwtAuthResponse> verifyAndSetupPassword(@Valid @RequestBody PasswordSetupRequest request) {
        JwtAuthResponse jwtAuthResponse = authService.verifyAndSetupPassword(request);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtAuthResponse jwtAuthResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
