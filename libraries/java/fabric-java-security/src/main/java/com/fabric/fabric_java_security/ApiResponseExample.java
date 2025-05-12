package com.fabric.fabric_java_security;

import com.fabric.fabric_java_security.model.ApiResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Example class to demonstrate ApiResponse usage.
 * This can be run with: 
 * mvn -pl libraries/java/fabric-java-security spring-boot:run -Dspring-boot.run.main-class=com.fabric.fabric_java_security.ApiResponseExample
 */
@SpringBootApplication
public class ApiResponseExample implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApiResponseExample.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("======== ApiResponse Examples ========");
        
        // Success response examples
        ApiResponse<String> successResponse = ApiResponse.success("Success message");
        System.out.println("Success Response: " + successResponse);
        
        ApiResponse<Integer> dataResponse = ApiResponse.success("Operation completed", 42);
        System.out.println("Data Response: " + dataResponse);
        
        // Create response examples
        ApiResponse<String> createdResponse = ApiResponse.created("New resource created");
        System.out.println("Created Response: " + createdResponse);
        
        // Error response examples
        ApiResponse<Void> badRequestResponse = ApiResponse.badRequest("Invalid input provided");
        System.out.println("Bad Request: " + badRequestResponse);
        
        ApiResponse<Void> notFoundResponse = ApiResponse.notFound("Resource not found");
        System.out.println("Not Found: " + notFoundResponse);
        
        ApiResponse<Void> serverErrorResponse = ApiResponse.internalServerError("Something went wrong");
        System.out.println("Server Error: " + serverErrorResponse);
        
        System.out.println("====================================");
        
        // Exit the application
        System.exit(0);
    }
}