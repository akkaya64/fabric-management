package com.fabric.user_service.service.impl;

import com.fabric.fabric_java_security.exception.ResourceNotFoundException;
import com.fabric.user_service.domain.User;
import com.fabric.user_service.domain.UserCompany;
import com.fabric.user_service.domain.UserContact;
import com.fabric.user_service.domain.VerificationToken;
import com.fabric.user_service.payload.*;
import com.fabric.user_service.repository.UserCompanyRepository;
import com.fabric.user_service.repository.UserContactRepository;
import com.fabric.user_service.repository.UserRepository;
import com.fabric.user_service.repository.VerificationTokenRepository;
import com.fabric.user_service.service.UserService;
import com.fabric.user_service.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationService verificationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapUserToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return mapUserToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByContact(String contactType, String contactValue) {
        UserContact.ContactType type = UserContact.ContactType.valueOf(contactType.toUpperCase());

        User user = switch (type) {
            case EMAIL -> userRepository.findByContactEmail(contactValue)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + contactValue));
            case PHONE -> userRepository.findByContactPhone(contactValue)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with phone: " + contactValue));
        };

        return mapUserToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Validate no duplicate email
        if (request.email() != null && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Validate no duplicate contacts
        for (CreateUserContactRequest contact : request.contacts()) {
            UserContact.ContactType type = UserContact.ContactType.valueOf(contact.type().toUpperCase());

            if (userContactRepository.existsByTypeAndValue(type, contact.value())) {
                throw new IllegalArgumentException(contact.type() + " already in use: " + contact.value());
            }
        }

        // Create user
        User user = User.builder()
                .email(request.email())
                .enabled(false) // User will be enabled after verification
                .build();

        // Add contacts
        for (CreateUserContactRequest contactRequest : request.contacts()) {
            UserContact contact = UserContact.builder()
                    .type(UserContact.ContactType.valueOf(contactRequest.type().toUpperCase()))
                    .value(contactRequest.value())
                    .primary(contactRequest.primary())
                    .verified(false) // Contacts start as unverified
                    .build();

            user.addContact(contact);
        }

        // Add companies
        for (CreateUserCompanyRequest companyRequest : request.companies()) {
            UserCompany company = UserCompany.builder()
                    .companyId(companyRequest.companyId())
                    .companyName(companyRequest.companyName())
                    .companyType(UserCompany.CompanyType.valueOf(companyRequest.companyType().toUpperCase()))
                    .userRole(companyRequest.userRole())
                    .build();

            user.addCompany(company);
        }

        User savedUser = userRepository.save(user);

        // Generate verification tokens and send emails/SMS
        for (UserContact contact : savedUser.getContacts()) {
            String token = UUID.randomUUID().toString();

            VerificationToken verificationToken = VerificationToken.builder()
                    .user(savedUser)
                    .token(token)
                    .targetIdentifier(contact.getValue())
                    .tokenType(switch (contact.getType()) {
                        case EMAIL -> VerificationToken.TokenType.EMAIL_VERIFICATION;
                        case PHONE -> VerificationToken.TokenType.PHONE_VERIFICATION;
                    })
                    .expiryDate(LocalDateTime.now().plusDays(1))
                    .build();

            verificationTokenRepository.save(verificationToken);

            // Send verification email/SMS
            if (contact.getType() == UserContact.ContactType.EMAIL) {
                verificationService.sendEmailVerification(contact.getValue(), token);
            } else {
                verificationService.sendPhoneVerification(contact.getValue(), token);
            }
        }

        // Create password setup token
        String passwordToken = UUID.randomUUID().toString();
        VerificationToken passwordSetupToken = VerificationToken.builder()
                .user(savedUser)
                .token(passwordToken)
                .tokenType(VerificationToken.TokenType.INITIAL_PASSWORD_SETUP)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        verificationTokenRepository.save(passwordSetupToken);

        // Send password setup email to primary email
        savedUser.getContacts().stream()
                .filter(c -> c.getType() == UserContact.ContactType.EMAIL && c.isPrimary())
                .findFirst()
                .ifPresent(primaryEmail ->
                        verificationService.sendPasswordSetupEmail(primaryEmail.getValue(), passwordToken)
                );

        return mapUserToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update enabled status if provided
        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }

        // Add new contacts if provided
        if (request.addContacts() != null && !request.addContacts().isEmpty()) {
            for (CreateUserContactRequest contactRequest : request.addContacts()) {
                UserContact.ContactType type = UserContact.ContactType.valueOf(contactRequest.type().toUpperCase());

                // Check if contact already exists
                if (userContactRepository.existsByTypeAndValue(type, contactRequest.value())) {
                    throw new IllegalArgumentException(contactRequest.type() + " already in use: " + contactRequest.value());
                }

                UserContact contact = UserContact.builder()
                        .type(type)
                        .value(contactRequest.value())
                        .primary(contactRequest.primary())
                        .verified(false) // New contacts start as unverified
                        .build();

                user.addContact(contact);

                // Generate verification token and send email/SMS
                String token = UUID.randomUUID().toString();

                VerificationToken verificationToken = VerificationToken.builder()
                        .user(user)
                        .token(token)
                        .targetIdentifier(contactRequest.value())
                        .tokenType(switch (type) {
                            case EMAIL -> VerificationToken.TokenType.EMAIL_VERIFICATION;
                            case PHONE -> VerificationToken.TokenType.PHONE_VERIFICATION;
                        })
                        .expiryDate(LocalDateTime.now().plusDays(1))
                        .build();

                verificationTokenRepository.save(verificationToken);

                // Send verification email/SMS
                if (type == UserContact.ContactType.EMAIL) {
                    verificationService.sendEmailVerification(contactRequest.value(), token);
                } else {
                    verificationService.sendPhoneVerification(contactRequest.value(), token);
                }
            }
        }

        // Remove contacts if provided
        if (request.removeContactIds() != null && !request.removeContactIds().isEmpty()) {
            for (Long contactId : request.removeContactIds()) {
                user.getContacts().removeIf(contact -> contact.getId().equals(contactId));
            }
        }

        // Add new companies if provided
        if (request.addCompanies() != null && !request.addCompanies().isEmpty()) {
            for (CreateUserCompanyRequest companyRequest : request.addCompanies()) {
                // Check if company association already exists
                if (userCompanyRepository.existsByUserIdAndCompanyId(id, companyRequest.companyId())) {
                    throw new IllegalArgumentException("User already associated with company: " + companyRequest.companyName());
                }

                UserCompany company = UserCompany.builder()
                        .companyId(companyRequest.companyId())
                        .companyName(companyRequest.companyName())
                        .companyType(UserCompany.CompanyType.valueOf(companyRequest.companyType().toUpperCase()))
                        .userRole(companyRequest.userRole())
                        .build();

                user.addCompany(company);
            }
        }

        // Remove companies if provided
        if (request.removeCompanyIds() != null && !request.removeCompanyIds().isEmpty()) {
            for (Long companyId : request.removeCompanyIds()) {
                user.getCompanies().removeIf(company -> company.getId().equals(companyId));
            }
        }

        User updatedUser = userRepository.save(user);
        return mapUserToResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse addContact(AddContactRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        UserContact.ContactType type = UserContact.ContactType.valueOf(request.type().toUpperCase());

        // Check if contact already exists
        if (userContactRepository.existsByTypeAndValue(type, request.value())) {
            throw new IllegalArgumentException(request.type() + " already in use: " + request.value());
        }

        UserContact contact = UserContact.builder()
                .type(type)
                .value(request.value())
                .primary(request.primary())
                .verified(false) // New contacts start as unverified
                .build();

        user.addContact(contact);
        User updatedUser = userRepository.save(user);

        // Generate verification token and send email/SMS
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .user(updatedUser)
                .token(token)
                .targetIdentifier(request.value())
                .tokenType(switch (type) {
                    case EMAIL -> VerificationToken.TokenType.EMAIL_VERIFICATION;
                    case PHONE -> VerificationToken.TokenType.PHONE_VERIFICATION;
                })
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        verificationTokenRepository.save(verificationToken);

        // Send verification email/SMS
        if (type == UserContact.ContactType.EMAIL) {
            verificationService.sendEmailVerification(request.value(), token);
        } else {
            verificationService.sendPhoneVerification(request.value(), token);
        }

        return mapUserToResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse verifyContact(VerifyContactRequest request) {
        VerificationToken token = verificationTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));

        if (token.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }

        if (token.isExpired()) {
            throw new IllegalStateException("Token has expired");
        }

        // Verify the contact
        String contactValue = token.getTargetIdentifier();
        UserContact.ContactType contactType = switch (token.getTokenType()) {
            case EMAIL_VERIFICATION -> UserContact.ContactType.EMAIL;
            case PHONE_VERIFICATION -> UserContact.ContactType.PHONE;
            default -> throw new IllegalStateException("Invalid token type for contact verification");
        };

        User user = token.getUser();

        UserContact contact = user.getContacts().stream()
                .filter(c -> c.getType() == contactType && c.getValue().equals(contactValue))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found for this user"));

        contact.setVerified(true);
        token.setUsed(true);

        // If this is the first verified contact, enable the user
        boolean hasVerifiedContact = user.getContacts().stream()
                .anyMatch(UserContact::isVerified);

        if (!hasVerifiedContact) {
            user.setEnabled(true);
        }

        User updatedUser = userRepository.save(user);
        return mapUserToResponse(updatedUser);
    }

    @Override
    @Transactional
    public void setPassword(SetPasswordRequest request) {
        // Validate password confirmation
        if (!request.password().equals(request.passwordConfirmation())) {
            throw new BadCredentialsException("Password confirmation doesn't match");
        }

        VerificationToken token = verificationTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (token.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }

        if (token.isExpired()) {
            throw new IllegalStateException("Token has expired");
        }

        if (token.getTokenType() != VerificationToken.TokenType.INITIAL_PASSWORD_SETUP &&
                token.getTokenType() != VerificationToken.TokenType.PASSWORD_RESET) {
            throw new IllegalStateException("Invalid token type for password setting");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.password()));
        token.setUsed(true);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByContactEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.email()));

        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();

        VerificationToken passwordResetToken = VerificationToken.builder()
                .user(user)
                .token(resetToken)
                .tokenType(VerificationToken.TokenType.PASSWORD_RESET)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        verificationTokenRepository.save(passwordResetToken);

        // Send password reset email
        verificationService.sendPasswordResetEmail(request.email(), resetToken);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }

        if (verificationToken.isExpired()) {
            throw new IllegalStateException("Token has expired");
        }

        if (verificationToken.getTokenType() != VerificationToken.TokenType.EMAIL_VERIFICATION) {
            throw new IllegalStateException("Invalid token type");
        }

        User user = verificationToken.getUser();

        // Find the email contact and mark as verified
        String email = verificationToken.getTargetIdentifier();

        UserContact emailContact = user.getContacts().stream()
                .filter(c -> c.getType() == UserContact.ContactType.EMAIL && c.getValue().equals(email))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Email contact not found for this user"));

        emailContact.setVerified(true);
        verificationToken.setUsed(true);

        // If this is the first verified contact, enable the user
        boolean hasVerifiedContact = user.getContacts().stream()
                .anyMatch(UserContact::isVerified);

        if (!hasVerifiedContact) {
            user.setEnabled(true);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void verifyPhone(String code) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification code"));

        if (verificationToken.isUsed()) {
            throw new IllegalStateException("Code has already been used");
        }

        if (verificationToken.isExpired()) {
            throw new IllegalStateException("Code has expired");
        }

        if (verificationToken.getTokenType() != VerificationToken.TokenType.PHONE_VERIFICATION) {
            throw new IllegalStateException("Invalid code type");
        }

        User user = verificationToken.getUser();

        // Find the phone contact and mark as verified
        String phone = verificationToken.getTargetIdentifier();

        UserContact phoneContact = user.getContacts().stream()
                .filter(c -> c.getType() == UserContact.ContactType.PHONE && c.getValue().equals(phone))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Phone contact not found for this user"));

        phoneContact.setVerified(true);
        verificationToken.setUsed(true);

        // If this is the first verified contact, enable the user
        boolean hasVerifiedContact = user.getContacts().stream()
                .anyMatch(UserContact::isVerified);

        if (!hasVerifiedContact) {
            user.setEnabled(true);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapUserToResponse(User user) {
        List<UserContactResponse> contacts = user.getContacts().stream()
                .map(contact -> new UserContactResponse(
                        contact.getId(),
                        contact.getType().name(),
                        contact.getValue(),
                        contact.isVerified(),
                        contact.isPrimary()
                ))
                .collect(Collectors.toList());

        List<UserCompanyResponse> companies = user.getCompanies().stream()
                .map(company -> new UserCompanyResponse(
                        company.getCompanyId(),
                        company.getCompanyName(),
                        company.getCompanyType().name(),
                        company.getUserRole()
                ))
                .collect(Collectors.toList());

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                contacts,
                companies
        );
    }
}