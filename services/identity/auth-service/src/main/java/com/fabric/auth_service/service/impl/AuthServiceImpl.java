package com.fabric.auth_service.service.impl;

import com.fabric.auth_service.client.UserServiceClient;
import com.fabric.auth_service.payload.AuthResponse;
import com.fabric.auth_service.payload.CompanyInfo;
import com.fabric.auth_service.payload.CompanySelectionRequest;
import com.fabric.auth_service.payload.LoginRequest;
import com.fabric.auth_service.payload.ResetPasswordRequest;
import com.fabric.auth_service.payload.SetPasswordRequest;
import com.fabric.auth_service.payload.UserResponse;
import com.fabric.auth_service.payload.request.LoginRequest;
import com.fabric.auth_service.payload.response.AuthResponse;
import com.fabric.auth_service.service.AuthService;
import com.fabric.fabric_java_security.exception.ResourceNotFoundException;
import com.fabric.fabric_java_security.jwt.JwtProvider;
import com.fabric.fabric_java_security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserServiceClient userServiceClient;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.identifier(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtProvider.generateToken(authentication);

            // Get user details
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // Fetch complete user info from User Service
            UserResponse userResponse = userServiceClient.getUserById(userPrincipal.getId());

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Convert user's companies to CompanyInfo objects
            List<CompanyInfo> availableCompanies = userResponse.companies().stream()
                    .map(company -> new CompanyInfo(
                            company.id(),
                            company.name(),
                            company.type(),
                            company.userRole()
                    ))
                    .collect(Collectors.toList());

            // If user belongs to multiple companies, return auth response without company context
            if (availableCompanies.size() > 1) {
                return new AuthResponse(
                        jwt,
                        userPrincipal.getId(),
                        userPrincipal.getUsername(),
                        null,
                        null,
                        null,
                        roles,
                        availableCompanies
                );
            }

            // If user belongs to only one company, set company context
            if (availableCompanies.size() == 1) {
                CompanyInfo company = availableCompanies.get(0);
                return new AuthResponse(
                        jwt,
                        userPrincipal.getId(),
                        userPrincipal.getUsername(),
                        company.id(),
                        company.name(),
                        company.type(),
                        roles,
                        availableCompanies
                );
            }

            // If user doesn't belong to any company
            return new AuthResponse(
                    jwt,
                    userPrincipal.getId(),
                    userPrincipal.getUsername(),
                    null,
                    null,
                    null,
                    roles,
                    List.of()
            );

        } catch (BadCredentialsException ex) {
            log.error("Invalid login attempt: {}", ex.getMessage());
            throw new BadCredentialsException("Invalid email/phone or password");
        }
    }

    @Override
    public AuthResponse selectCompany(CompanySelectionRequest request) {
        // Validate token
        if (!jwtProvider.validateToken(request.accessToken())) {
            throw new BadCredentialsException("Invalid access token");
        }

        // Get user ID from token
        Long userIdFromToken = jwtProvider.getUserIdFromToken(request.accessToken());

        // Verify that the user ID in the token matches the requested user ID
        if (!userIdFromToken.equals(request.userId())) {
            throw new BadCredentialsException("Token does not match the provided user ID");
        }

        // Get user info from User Service
        UserResponse userResponse = userServiceClient.getUserById(request.userId());

        // Find the requested company
        CompanyInfo selectedCompany = userResponse.companies().stream()
                .filter(company -> company.id().equals(request.companyId()))
                .map(company -> new CompanyInfo(
                        company.id(),
                        company.name(),
                        company.type(),
                        company.userRole()
                ))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User does not belong to the selected company"));

        // Create Authentication with company context
        List<String> roles = List.of("ROLE_" + selectedCompany.role());

        // Create user principal with company context
        UserPrincipal userPrincipal = UserPrincipal.create(
                request.userId(),
                userResponse.email(),
                null, // Password is not needed here
                selectedCompany.id(),
                selectedCompany.type(),
                roles
        );

        // Generate new token with company context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        String jwt = jwtProvider.generateToken(authentication);

        // Convert all user's companies to CompanyInfo objects
        List<CompanyInfo> availableCompanies = userResponse.companies().stream()
                .map(company -> new CompanyInfo(
                        company.id(),
                        company.name(),
                        company.type(),
                        company.userRole()
                ))
                .collect(Collectors.toList());

        return new AuthResponse(
                jwt,
                request.userId(),
                userResponse.email(),
                selectedCompany.id(),
                selectedCompany.name(),
                selectedCompany.type(),
                roles,
                availableCompanies
        );
    }

    @Override
    public void setPassword(SetPasswordRequest request) {
        // Validate password confirmation
        if (!request.password().equals(request.passwordConfirmation())) {
            throw new BadCredentialsException("Password confirmation doesn't match");
        }

        // Verify token and set password
        userServiceClient.setPassword(request);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        // Call user service to generate reset token and send email
        userServiceClient.resetPassword(request);
    }

    @Override
    public void verifyEmail(String token) {
        // Call user service to verify email
        userServiceClient.verifyEmail(token);
    }

    @Override
    public void verifyPhone(String code) {
        // Call user service to verify phone
        userServiceClient.verifyPhone(code);
    }
}