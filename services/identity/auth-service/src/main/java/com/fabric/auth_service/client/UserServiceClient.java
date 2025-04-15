package com.fabric.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fabric.auth_service.payload.request.CreateUserRequest;
import com.fabric.auth_service.payload.response.UserResponse;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/username/{username}")
    ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/email/{email}")
    ResponseEntity<UserResponse> getUserByEmail(@PathVariable("email") String email);

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id);

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request);
}