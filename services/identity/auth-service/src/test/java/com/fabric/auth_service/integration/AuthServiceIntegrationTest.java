package com.fabric.auth_service.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fabric.auth_service.client.UserServiceClient;
import com.fabric.auth_service.payload.request.CreateUserRequest;
import com.fabric.auth_service.payload.request.LoginRequest;
import com.fabric.auth_service.payload.response.JwtResponse;
import com.fabric.auth_service.payload.response.UserResponse;
import com.fabric.auth_service.service.AuthService;
import com.fabric.auth_service.service.JWTService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    private UserResponse userResponse;
    private LoginRequest loginRequest;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    public void setup() {
        // Setup test user response
        userResponse = new UserResponse();
        userResponse.setId("123");
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");
        userResponse.setFirstName("Test");
        userResponse.setLastName("User");
        userResponse.setActive(true);

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        userResponse.setRoles(roles);

        userResponse.setCreatedAt(LocalDateTime.now());
        userResponse.setUpdatedAt(LocalDateTime.now());

        // Setup login request
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        // Setup create user request
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("newuser");
        createUserRequest.setPassword("password");
        createUserRequest.setEmail("newuser@example.com");
        createUserRequest.setFirstName("New");
        createUserRequest.setLastName("User");
        createUserRequest.setRoles(Set.of("ROLE_USER"));

        // Mock authentication
        User userDetails = new User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities()).thenReturn(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mock JWT service
        when(jwtService.generateAccessToken(any(Authentication.class))).thenReturn("access_token");
        when(jwtService.generateRefreshToken(any(String.class))).thenReturn("refresh_token");
    }

    @Test
    public void testLoginFlow() {
        // Mock user service client
        when(userServiceClient.getUserByUsername("testuser")).thenReturn(ResponseEntity.ok(userResponse));

        // Test login
        JwtResponse response = authService.login(loginRequest);

        // Verify response
        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        assertEquals("refresh_token", response.getRefreshToken());
        assertEquals("123", response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(List.of("ROLE_USER"), response.getRoles());
    }

    @Test
    public void testRegisterFlow() {
        // Mock user service client
        when(userServiceClient.getUserByUsername("newuser")).thenThrow(feign.FeignException.NotFound.class);
        when(userServiceClient.getUserByEmail("newuser@example.com")).thenThrow(feign.FeignException.NotFound.class);

        UserResponse newUserResponse = new UserResponse();
        newUserResponse.setId("456");
        newUserResponse.setUsername("newuser");
        newUserResponse.setEmail("newuser@example.com");
        newUserResponse.setFirstName("New");
        newUserResponse.setLastName("User");
        newUserResponse.setActive(true);
        newUserResponse.setRoles(Set.of("ROLE_USER"));
        newUserResponse.setCreatedAt(LocalDateTime.now());
        newUserResponse.setUpdatedAt(LocalDateTime.now());

        when(userServiceClient.createUser(any(CreateUserRequest.class))).thenReturn(ResponseEntity.ok(newUserResponse));

        // Test register
        UserResponse response = authService.register(createUserRequest);

        // Verify response
        assertNotNull(response);
        assertEquals("456", response.getId());
        assertEquals("newuser", response.getUsername());
        assertEquals("newuser@example.com", response.getEmail());
        assertEquals(Set.of("ROLE_USER"), response.getRoles());
    }
}