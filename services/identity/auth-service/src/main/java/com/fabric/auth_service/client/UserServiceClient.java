package com.fabric.auth_service.client;

import com.fabric.auth_service.payload.request.ResetPasswordRequest;
import com.fabric.auth_service.payload.request.SetPasswordRequest;
import com.fabric.auth_service.payload.response.UserResponse;
import com.fabric.fabric_java_security.model.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${fabric.services.user-service.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @PostMapping("/api/users/set-password")
    ApiResponse<Void> setPassword(@RequestBody SetPasswordRequest request);

    @PostMapping("/api/users/reset-password")
    ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request);

    @PostMapping("/api/users/verify-email")
    ApiResponse<Void> verifyEmail(@RequestBody String token);

    @PostMapping("/api/users/verify-phone")
    ApiResponse<Void> verifyPhone(@RequestBody String code);
}