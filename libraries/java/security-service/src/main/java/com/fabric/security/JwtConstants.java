package com.fabric.security;

public final class JwtConstants {
    
    private JwtConstants() {
        // Utility class
    }
    
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_TYPE = "JWT";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String USER_ID_KEY = "userId";
    public static final String TENANT_ID_KEY = "tenantId";
    public static final String USERNAME_KEY = "username";
    public static final String ROLES_KEY = "roles";
    public static final String PERMISSIONS_KEY = "permissions";
    
    // Token expiration times (in milliseconds)
    public static final long ACCESS_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000L; // 24 hours
    public static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7 days
    
    // Token types
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";
}
