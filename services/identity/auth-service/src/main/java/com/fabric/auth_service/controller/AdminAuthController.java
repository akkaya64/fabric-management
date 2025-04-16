package com.fabric.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<String> getAuthStatus() {
        return ResponseEntity.ok("Auth service is running with admin access");
    }

    // Admin yetkilendirme işlemleri için ek endpointler burada olabilir
}
