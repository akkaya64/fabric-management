package com.fabric.auth_service.e2e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import com.fabric.auth_service.client.UserServiceClient;
import com.fabric.auth_service.payload.request.CreateUserRequest;
import com.fabric.auth_service.payload.response.UserResponse;
import com.fabric.auth_service.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private Authentication authentication;

    private UserResponse userResponse;

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
        when(jwtService.extractUsername(any(String.class))).thenReturn("testuser");
        when(jwtService.validateToken(any(String.class))).thenReturn(true);
    }

    @Test
    public void testLoginEndpoint() throws Exception {
        // Mock user service client
        when(userServiceClient.getUserByUsername("testuser")).thenReturn(ResponseEntity.ok(userResponse));

        // Test login endpoint
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "testuser",
                                "password", "password"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    public void testRegisterEndpoint() throws Exception {
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

        // Test register endpoint
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "newuser",
                                "password", "password123",
                                "email", "newuser@example.com",
                                "firstName", "New",
                                "lastName", "User"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("456"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    public void testRefreshTokenEndpoint() throws Exception {
        // Mock user service client
        when(userServiceClient.getUserByUsername("testuser")).thenReturn(ResponseEntity.ok(userResponse));

        // Test refresh token endpoint
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "refreshToken", "valid_refresh_token"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_token"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    private static class Map<K, V> extends java.util.HashMap<K, V> {
        @Serial
        private static final long serialVersionUID = 1L;

        public static <K, V> Map<K, V> of(K k1, V v1) {
            Map<K, V> map = new Map<>();
            map.put(k1, v1);
            return map;
        }

        public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
            Map<K, V> map = new Map<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }

        public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
            Map<K, V> map = new Map<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            return map;
        }

        public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
            Map<K, V> map = new Map<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            return map;
        }

        public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
            Map<K, V> map = new Map<>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            return map;
        }
    }
}