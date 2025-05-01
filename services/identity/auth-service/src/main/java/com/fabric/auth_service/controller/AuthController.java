package com.fabric.auth_service.controller;

import com.fabric.auth_service.payload.AuthResponse;
import com.fabric.auth_service.payload.CompanySelectionRequest;
import com.fabric.auth_service.payload.ResetPasswordRequest;
import com.fabric.auth_service.payload.SetPasswordRequest;
import com.fabric.auth_service.payload.response.AuthResponse;
import com.fabric.fabric_java_security.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email/phone and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/select-company")
    @Operation(summary = "Select company context after login")
    public ResponseEntity<ApiResponse<AuthResponse>> selectCompany(
            @Valid @RequestBody CompanySelectionRequest request) {
        AuthResponse authResponse = authService.selectCompany(request);
        return ResponseEntity.ok(ApiResponse.success("Company context selected", authResponse));
    }

    @PostMapping("/set-password")
    @Operation(summary = "Set password for first-time users")
    public ResponseEntity<ApiResponse<Void>> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        authService.setPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password set successfully", null));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Request password reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent", null));
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify email address")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestBody String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully", null));
    }

    @PostMapping("/verify-phone")
    @Operation(summary = "Verify phone number with code")
    public ResponseEntity<ApiResponse<Void>> verifyPhone(@RequestBody String code) {
        authService.verifyPhone(code);
        return ResponseEntity.ok(ApiResponse.success("Phone number verified successfully", null));
    }
}