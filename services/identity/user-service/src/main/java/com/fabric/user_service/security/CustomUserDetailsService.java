package com.fabric.user_service.security;

import com.fabric.fabric_java_security.model.UserPrincipal;
import com.fabric.user_service.domain.User;
import com.fabric.user_service.domain.UserCompany;
import com.fabric.user_service.domain.UserContact;
import com.fabric.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // First try to find by email (stored in User.email field)
        Optional<User> userOptional = userRepository.findByEmail(usernameOrEmail);

        // If not found, try to find by email contact
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByContactEmail(usernameOrEmail);
        }

        // If still not found, try to find by phone contact
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByContactPhone(usernameOrEmail);
        }

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email or phone: " + usernameOrEmail));

        // Get primary email if exists
        String email = user.getEmail();
        if (email == null) {
            email = user.getContacts().stream()
                    .filter(c -> c.getType() == UserContact.ContactType.EMAIL && c.isPrimary())
                    .map(UserContact::getValue)
                    .findFirst()
                    .orElse(null);
        }

        // Get first company if exists (company context will be selected later)
        Long companyId = null;
        String companyType = null;
        List<String> roles = Collections.emptyList();

        if (!user.getCompanies().isEmpty()) {
            UserCompany company = user.getCompanies().get(0);
            companyId = company.getCompanyId();
            companyType = company.getCompanyType().name();
            roles = List.of("ROLE_" + company.getUserRole());
        }

        return UserPrincipal.create(
                user.getId(),
                email,
                user.getPassword(),
                companyId,
                companyType,
                roles
        );
    }
}