package com.fabric.user_service.security;

import com.fabric.fabric_java_security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final JwtProvider jwtProvider;

    /**
     * Check if the current user is accessing their own data
     *
     * @param userId The user ID being accessed
     * @return true if the current user is the same as the requested user ID
     */
    public boolean isUserSelf(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String token = extractTokenFromAuthentication(authentication);

        if (token == null) {
            return false;
        }

        Long currentUserId = jwtProvider.getUserIdFromToken(token);
        return userId.equals(currentUserId);
    }

    /**
     * Check if the current user belongs to the specified company
     *
     * @param companyId The company ID to check
     * @return true if the current user belongs to the company
     */
    public boolean belongsToCompany(Long companyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String token = extractTokenFromAuthentication(authentication);

        if (token == null) {
            return false;
        }

        Long userCompanyId = jwtProvider.getCompanyIdFromToken(token);
        return companyId.equals(userCompanyId);
    }

    /**
     * Check if the current user belongs to a company of the specified type
     *
     * @param companyType The company type to check
     * @return true if the current user belongs to a company of the specified type
     */
    public boolean belongsToCompanyType(String companyType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String token = extractTokenFromAuthentication(authentication);

        if (token == null) {
            return false;
        }

        String userCompanyType = jwtProvider.getCompanyTypeFromToken(token);
        return companyType.equalsIgnoreCase(userCompanyType);
    }

    /**
     * Extract JWT token from Authentication object
     */
    private String extractTokenFromAuthentication(Authentication authentication) {
        String principal = authentication.getPrincipal().toString();

        // If principal contains a token (format: "Bearer <token>")
        if (principal.startsWith("Bearer ")) {
            return principal.substring(7);
        }

        return null;
    }
}