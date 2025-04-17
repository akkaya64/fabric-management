package com.fabric.fabric_java_security.config;

import com.fabric.fabric_java_security.jwt.JwtAuthenticationEntryPoint;
import com.fabric.fabric_java_security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.fabric.fabric_java_security")
public class FabricSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }
}