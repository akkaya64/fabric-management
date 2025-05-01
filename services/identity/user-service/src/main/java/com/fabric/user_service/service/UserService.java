package com.fabric.user_service.service;

import com.fabric.user_service.payload.*;
import com.fabric.user_service.payload.response.UserResponse;
import com.fabric.user_service.repository.*;
import com.fabric.user_service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserContactRepository contactRepository;
    private final UserCompanyRepository companyRepository;
    private final VerificationTokenRepository tokenRepository;
    private final VerificationService verificationService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest req) {
        // TODO: validate, map, save user + contacts + companies + send verifications
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest req) {
        // TODO: load, apply changes, save
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public UserResponse addContact(AddContactRequest req) {
        // TODO: load user, add contact + send verification, save
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public UserResponse verifyContact(VerifyContactRequest req) {
        // TODO: validate token, mark contact verified, save
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public void setPassword(SetPasswordRequest req) {
        // TODO: validate token + passwords match, set encoded pw
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        // TODO: lookup by email, generate reset token, send email
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public void verifyEmail(String token) {
        // TODO: lookup token, mark email contact verified
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional
    public void verifyPhone(String code) {
        // TODO: lookup token, mark phone contact verified
        throw new UnsupportedOperationException("Not implemented yet");
    }