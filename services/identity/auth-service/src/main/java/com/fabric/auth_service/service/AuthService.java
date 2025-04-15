package com.fabric.auth_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fabric.auth_service.client.UserServiceClient;
import com.fabric.auth_service.exception.TokenRefreshException;
import com.fabric.auth_service.exception.UserAlreadyExistsException;
import com.fabric.auth_service.payload.request.CreateUserRequest;
import com.fabric.auth_service.payload.request.LoginRequest;
import com.fabric.auth_service.payload.request.RefreshTokenRequest;
import com.fabric.auth_service.payload.response.JwtResponse;
import com.fabric.auth_service.payload.response.UserResponse;

import feign.FeignException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JWTService jwtService,
            UserServiceClient userServiceClient,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Get user details from user-service
        UserResponse userResponse = userServiceClient.getUserByUsername(userDetails.getUsername()).getBody();

        if (userResponse == null) {
            throw new RuntimeException("User details could not be retrieved");
        }

        return new JwtResponse(
                accessToken,
                refreshToken,
                userResponse.getId(),
                userResponse.getUsername(),
                userResponse.getEmail(),
                roles);
    }

    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.validateToken(refreshToken)) {
            throw new TokenRefreshException(refreshToken, "Refresh token is invalid");
        }

        String username = jwtService.extractUsername(refreshToken);

        // Get user details from user-service
        UserResponse userResponse = userServiceClient.getUserByUsername(username).getBody();

        if (userResponse == null) {
            throw new RuntimeException("User details could not be retrieved");
        }

        // Create a UserDetails object with the roles from the userResponse
        List<GrantedAuthority> authorities = userResponse.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toList());

        UserDetails userDetails = new User(username, "", authorities);

        // Generate new access token
        String accessToken = jwtService.generateAccessToken(
                username,
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        // Generate new refresh token
        String newRefreshToken = jwtService.generateRefreshToken(username);

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                accessToken,
                newRefreshToken,
                userResponse.getId(),
                userResponse.getUsername(),
                userResponse.getEmail(),
                roles);
    }

    public UserResponse register(CreateUserRequest request) {
        // Check if username already exists
        try {
            UserResponse existingUser = userServiceClient.getUserByUsername(request.getUsername()).getBody();
            if (existingUser != null) {
                throw new UserAlreadyExistsException("Username is already taken");
            }
        } catch (FeignException.NotFound ex) {
            // Username doesn't exist, continue
        }

        // Check if email already exists
        try {
            UserResponse existingUser = userServiceClient.getUserByEmail(request.getEmail()).getBody();
            if (existingUser != null) {
                throw new UserAlreadyExistsException("Email is already in use");
            }
        } catch (FeignException.NotFound ex) {
            // Email doesn't exist, continue
        }

        // Encode password
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set default roles if none provided
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_USER");
            request.setRoles(roles);
        }

        // Create user in user-service
        return userServiceClient.createUser(request).getBody();
    }
}
