package com.fabric.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

public final class SecurityUtils {
    
    private SecurityUtils() {
        // Utility class
    }
    
    /**
     * Get the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof String) {
                return Optional.of((String) authentication.getPrincipal());
            }
        }
        return Optional.empty();
    }
    
    /**
     * Get the current user details if available.
     */
    public static Optional<JwtUserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof JwtUserDetails) {
            return Optional.of((JwtUserDetails) authentication.getDetails());
        }
        return Optional.empty();
    }
    
    /**
     * Get the tenant ID of the current user.
     */
    public static Optional<String> getCurrentUserTenantId() {
        return getCurrentUserDetails().map(JwtUserDetails::getTenantId);
    }
    
    /**
     * Get the user ID of the current user.
     */
    public static Optional<String> getCurrentUserId() {
        return getCurrentUserDetails().map(JwtUserDetails::getUserId);
    }
    
    /**
     * Check if the current user has a specific authority.
     */
    public static boolean hasCurrentUserAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            return authorities.stream()
                    .anyMatch(grantedAuthority -> authority.equals(grantedAuthority.getAuthority()));
        }
        return false;
    }
    
    /**
     * Check if the current user has any of the specified authorities.
     */
    public static boolean hasCurrentUserAnyAuthority(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
            for (String authority : authorities) {
                if (userAuthorities.stream()
                        .anyMatch(grantedAuthority -> authority.equals(grantedAuthority.getAuthority()))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check if user is authenticated.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
    
    /**
     * Validate tenant access for the current user.
     */
    public static void validateTenantAccess(String tenantId) {
        String currentTenantId = getCurrentUserTenantId()
                .orElseThrow(() -> new RuntimeException("No tenant context found"));
        
        if (!currentTenantId.equals(tenantId)) {
            throw new RuntimeException("Access denied to tenant: " + tenantId);
        }
    }
}