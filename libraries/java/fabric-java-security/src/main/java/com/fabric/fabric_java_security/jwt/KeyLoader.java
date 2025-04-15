package com.fabric.fabric_java_security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {
    private static final Logger logger = LoggerFactory.getLogger(KeyLoader.class);

    @Value("${jwt.key.public:classpath:public_key.pem}")
    private String publicKeyPath;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            loadPublicKey();
        } catch (Exception e) {
            logger.error("Failed to load RSA public key", e);
            throw new RuntimeException("Failed to load RSA public key", e);
        }
    }

    private void loadPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = loadKeyBytes(publicKeyPath);

        // Remove PEM headers and newlines if present
        String keyContent = new String(keyBytes);
        if (keyContent.contains("-----BEGIN PUBLIC KEY-----")) {
            keyContent = keyContent.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            keyBytes = Base64.getDecoder().decode(keyContent);
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        publicKey = keyFactory.generatePublic(keySpec);
    }

    private byte[] loadKeyBytes(String location) throws IOException {
        if (location.startsWith("classpath:")) {
            String path = location.substring("classpath:".length());
            Resource resource = new ClassPathResource(path);
            try (InputStream is = resource.getInputStream()) {
                return is.readAllBytes();
            }
        } else {
            Path path = Paths.get(location);
            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                try (FileInputStream fis = new FileInputStream(new File(location))) {
                    return fis.readAllBytes();
                }
            }
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}