package com.fabric.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "fabric.security.jwt")
public class JwtProperties {
    
    private String secret = "defaultSecretKeyForDevelopmentOnlyDoNotUseInProduction";
    private long accessTokenExpiration = JwtConstants.ACCESS_TOKEN_EXPIRATION;
    private long refreshTokenExpiration = JwtConstants.REFRESH_TOKEN_EXPIRATION;
    private String issuer = "fabric-management";
    private String audience = "fabric-users";
    private boolean enableRefreshToken = true;
    
    // RSA key configuration for production
    private String publicKeyPath;
    private String privateKeyPath;
    private boolean useRsaKeys = false;
    
    // Algorithm configuration
    private String algorithm = "HS256"; // HMAC SHA-256
}
