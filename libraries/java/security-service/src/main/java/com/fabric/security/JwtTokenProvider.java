package com.fabric.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    
    private final JwtProperties jwtProperties;
    
    public String generateAccessToken(Authentication authentication, String userId, String tenantId) {
        return generateToken(authentication, userId, tenantId, JwtConstants.ACCESS_TOKEN_TYPE, 
                           jwtProperties.getAccessTokenExpiration());
    }
    
    public String generateRefreshToken(Authentication authentication, String userId, String tenantId) {
        return generateToken(authentication, userId, tenantId, JwtConstants.REFRESH_TOKEN_TYPE, 
                           jwtProperties.getRefreshTokenExpiration());
    }
    
    private String generateToken(Authentication authentication, String userId, String tenantId, 
                               String tokenType, long expiration) {
        try {
            List<String> authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            Instant now = Instant.now();
            Instant expiryDate = now.plus(expiration, ChronoUnit.MILLIS);
            
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(authentication.getName())
                    .issuer(jwtProperties.getIssuer())
                    .audience(jwtProperties.getAudience())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiryDate))
                    .claim(JwtConstants.USER_ID_KEY, userId)
                    .claim(JwtConstants.TENANT_ID_KEY, tenantId)
                    .claim(JwtConstants.USERNAME_KEY, authentication.getName())
                    .claim(JwtConstants.AUTHORITIES_KEY, authorities)
                    .claim("tokenType", tokenType)
                    .build();
            
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
                    claimsSet
            );
            
            signedJWT.sign(new MACSigner(jwtProperties.getSecret()));
            
            return signedJWT.serialize();
            
        } catch (JOSEException e) {
            log.error("Error generating JWT token", e);
            throw new RuntimeException("Could not generate JWT token", e);
        }
    }
    
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            
            // Verify signature
            if (!signedJWT.verify(new MACVerifier(jwtProperties.getSecret()))) {
                log.warn("Invalid JWT signature");
                return false;
            }
            
            // Check expiration
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiration != null && expiration.before(new Date())) {
                log.warn("JWT token is expired");
                return false;
            }
            
            return true;
            
        } catch (ParseException | JOSEException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    
    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Error parsing username from token", e);
            return null;
        }
    }
    
    public String getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim(JwtConstants.USER_ID_KEY);
        } catch (ParseException e) {
            log.error("Error parsing user ID from token", e);
            return null;
        }
    }
    
    public String getTenantIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim(JwtConstants.TENANT_ID_KEY);
        } catch (ParseException e) {
            log.error("Error parsing tenant ID from token", e);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getAuthoritiesFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return (List<String>) signedJWT.getJWTClaimsSet().getClaim(JwtConstants.AUTHORITIES_KEY);
        } catch (ParseException e) {
            log.error("Error parsing authorities from token", e);
            return List.of();
        }
    }
}
