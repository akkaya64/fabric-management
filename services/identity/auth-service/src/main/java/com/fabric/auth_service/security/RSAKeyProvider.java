package com.fabric.auth_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RSAKeyProvider {

    private static final Logger logger = LoggerFactory.getLogger(RSAKeyProvider.class);

    @Value("${jwt.key.store.path:#{null}}")
    private String keyStorePath;

    @Value("${jwt.key.store.password:#{null}}")
    private String keyStorePassword;

    @Value("${jwt.key.alias:#{null}}")
    private String keyAlias;

    @Value("${jwt.key.private:#{null}}")
    private String privateKeyPath;

    @Value("${jwt.key.public:#{null}}")
    private String publicKeyPath;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            if (keyStorePath != null && keyStorePassword != null && keyAlias != null) {
                loadKeysFromKeyStore();
            } else if (privateKeyPath != null && publicKeyPath != null) {
                loadKeysFromFiles();
            } else {
                generateKeys();
            }

            Objects.requireNonNull(publicKey, "Public key must not be null");
            Objects.requireNonNull(privateKey, "Private key must not be null");

            logger.info("RSA key pair successfully loaded");
        } catch (Exception e) {
            logger.error("Failed to initialize RSA keys", e);
            throw new RuntimeException("Failed to initialize RSA keys", e);
        }
    }

    private void loadKeysFromKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(new File(keyStorePath))) {
            keyStore.load(fis, keyStorePassword.toCharArray());

            Key key = keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
            if (key instanceof RSAPrivateKey) {
                this.privateKey = (RSAPrivateKey) key;

                // Get the public key from the certificate
                Certificate cert = keyStore.getCertificate(keyAlias);
                PublicKey publicKey = cert.getPublicKey();
                if (publicKey instanceof RSAPublicKey) {
                    this.publicKey = (RSAPublicKey) publicKey;
                } else {
                    throw new RuntimeException("Public key is not an instance of RSAPublicKey");
                }
            } else {
                throw new RuntimeException("Private key is not an instance of RSAPrivateKey");
            }
        }
    }

    private void loadKeysFromFiles() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Load the public key
        try {
            byte[] publicKeyBytes = loadFileBytes(publicKeyPath);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            logger.error("Failed to load public key", e);
            throw e;
        }

        // Load the private key
        try {
            byte[] privateKeyBytes = loadFileBytes(privateKeyPath);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            logger.error("Failed to load private key", e);
            throw e;
        }
    }

    private byte[] loadFileBytes(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(path))) {
            byte[] content = new byte[fis.available()];
            fis.read(content);
            // Check if the content is base64 encoded
            if (isBase64Encoded(content)) {
                return Base64.getDecoder().decode(content);
            }
            return content;
        }
    }

    private boolean isBase64Encoded(byte[] bytes) {
        String content = new String(bytes).trim();
        return content.startsWith("-----BEGIN") ||
                (content.matches("^[A-Za-z0-9+/]+={0,2}$") && content.length() % 4 == 0);
    }

    private void generateKeys() throws Exception {
        logger.warn("Generating new RSA key pair. This should only be used for development purposes.");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}
