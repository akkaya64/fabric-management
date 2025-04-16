package com.fabric.auth_service.client;

import com.fabric.auth_service.payload.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable String id);

    @GetMapping("/email/{email}")
    UserResponse getUserByEmail(@PathVariable String email);

    @GetMapping("/phone/{phoneNumber}")
    UserResponse getUserByPhoneNumber(@PathVariable String phoneNumber);
}