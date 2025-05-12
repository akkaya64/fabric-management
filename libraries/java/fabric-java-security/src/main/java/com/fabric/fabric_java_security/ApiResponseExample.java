package com.fabric.fabric_java_security;

import com.fabric.fabric_java_security.model.ApiResponse;

public class ApiResponseExample {
    public static void main(String[] args) {
        // Create a success response
        ApiResponse<String> successResponse = ApiResponse.success("User created successfully", "user123");
        System.out.println("Success Response: " + successResponse);
        
        // Create an error response
        ApiResponse<Object> errorResponse = ApiResponse.error("User not found");
        System.out.println("Error Response: " + errorResponse);
    }
}
