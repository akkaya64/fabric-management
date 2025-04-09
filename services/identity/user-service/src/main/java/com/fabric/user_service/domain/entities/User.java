package com.fabric.user_service.domain.entities;

import com.fabric.user_service.domain.enums.CompetencyLevel;
import com.fabric.user_service.domain.enums.Department;
import com.fabric.user_service.domain.enums.Position;
import com.fabric.user_service.domain.enums.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User extends BaseEntity{
    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean hasVerifiedEmail;

    @Column(nullable = false)
    private Boolean hasVerifiedPhone;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetencyLevel competencyLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column
    private String locale;

    @Column
    private String timezone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<UserRole> userRoles = new HashSet<>();

    @Column
    private LocalDateTime lastPasswordChangeAt;

    @Column
    private Boolean forcePasswordChange;

    @Column
    private LocalDateTime lastLoginAt;

    @Column
    private String lastLoginIp;

    @Column
    private Integer failedLoginAttempts;

    @Column
    private LocalDateTime lockedUntil;
}
