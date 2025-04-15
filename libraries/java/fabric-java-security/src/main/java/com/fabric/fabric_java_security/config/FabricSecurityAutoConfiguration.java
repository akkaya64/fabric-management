package com.fabric.fabric_java_security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fabric.fabric_java_security.jwt.JwtUtil;
import com.fabric.fabric_java_security.jwt.KeyLoader;

@Configuration
@ComponentScan(basePackages = "com.fabric.fabric_java_security")
public class FabricSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeyLoader keyLoader() {
        return new KeyLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtil jwtUtil(KeyLoader keyLoader) {
        return new JwtUtil(keyLoader);
    }
}