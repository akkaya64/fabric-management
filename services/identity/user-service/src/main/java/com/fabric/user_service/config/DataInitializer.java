package com.fabric.user_service.config;

import com.fabric.user_service.domain.User;
import com.fabric.user_service.domain.UserCompany;
import com.fabric.user_service.domain.UserContact;
import com.fabric.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${fabric.admin.email}")
    private String adminEmail;

    @Value("${fabric.admin.password}")
    private String adminPassword;

    @Value("${fabric.admin.companyName}")
    private String adminCompanyName;

    @Value("${fabric.admin.companyType}")
    private String adminCompanyType;

    @Value("${fabric.admin.role}")
    private String adminRole;

    @Override
    @Transactional
    public void run(String... args) {
        // Check if admin user already exists
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user already exists, skipping initialization");
            return;
        }

        log.info("Creating admin user with email: {}", adminEmail);

        // Create admin user
        User adminUser = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .enabled(true)
                .build();

        // Add admin email contact
        UserContact emailContact = UserContact.builder()
                .type(UserContact.ContactType.EMAIL)
                .value(adminEmail)
                .primary(true)
                .verified(true)
                .build();
        adminUser.addContact(emailContact);

        // Add admin company
        UserCompany company = UserCompany.builder()
                .companyId(1L) // Default company ID for internal company
                .companyName(adminCompanyName)
                .companyType(UserCompany.CompanyType.valueOf(adminCompanyType))
                .userRole(adminRole)
                .build();
        adminUser.addCompany(company);

        // Save admin user
        userRepository.save(adminUser);

        log.info("Admin user created successfully");
    }
}