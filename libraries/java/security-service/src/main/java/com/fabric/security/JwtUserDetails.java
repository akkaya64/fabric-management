package com.fabric.security;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtUserDetails {
    private String username;
    private String userId;
    private String tenantId;
    private List<String> authorities;
    
    public static JwtUserDetailsBuilder builder() {
        return new JwtUserDetailsBuilder();
    }
}