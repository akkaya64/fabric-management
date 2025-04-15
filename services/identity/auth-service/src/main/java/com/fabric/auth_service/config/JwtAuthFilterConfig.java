package com.fabric.auth_service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fabric.auth_service.security.JwtAuthenticationFilter;

@Configuration
public class JwtAuthFilterConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public JwtAuthFilterConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(jwtAuthenticationFilter);
        // Disable automatic registration since the filter is already registered by Spring Security
        registrationBean.setEnabled(false);

        return registrationBean;
    }
}