package com.fabric.user_service.controller;

import com.fabric.fabric_java_security.model.ApiResponse;
import com.fabric.user_service.payload.*;
import com.fabric.user_service.payload.request.ResetPasswordRequest;
import com.fabric.user_service.payload.request.SetPasswordRequest;
import com.fabric.user_service.payload.response.UserResponse;
import com.fabric.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User management API endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserSelf(#id)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PostMapping
    @Operation(summary = "Create a new user", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse createdUser = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("User created successfully", createdUser));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserSelf(#id)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    @PostMapping("/contacts")
    @Operation(summary = "Add a contact to a user", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserSelf(#request.userId())")
    public ResponseEntity<ApiResponse<UserResponse>> addContact(@Valid @RequestBody AddContactRequest request) {
        UserResponse updatedUser = userService.addContact(request);
        return ResponseEntity.ok(ApiResponse.success("Contact added successfully", updatedUser));
    }

    @PostMapping("/verify-contact")
    @Operation(summary = "Verify a contact")
    public ResponseEntity<ApiResponse<UserResponse>> verifyContact(@Valid @RequestBody VerifyContactRequest request) {
        UserResponse updatedUser = userService.verifyContact(request);
        return ResponseEntity.ok(ApiResponse.success("Contact verified successfully", updatedUser));
    }

    @PostMapping("/set-password")
    @Operation(summary = "Set initial password for user")
    public ResponseEntity<ApiResponse<Void>> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        userService.setPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password set successfully", null));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Request password reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent", null));
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify email address")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestBody String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully", null));
    }

    @PostMapping("/verify-phone")
    @Operation(summary = "Verify phone number with code")
    public ResponseEntity<ApiResponse<Void>> verifyPhone(@RequestBody String code) {
        userService.verifyPhone(code);
        return ResponseEntity.ok(ApiResponse.success("Phone number verified successfully", null));
    }
}