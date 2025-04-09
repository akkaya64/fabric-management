package com.fabric.user_service.domain.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Role extends BaseEntity{
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean isDefault;

    // Role hiyerarşisi için
    @Column(name = "parent_role_id")
    private UUID parentRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_role_id", insertable = false, updatable = false)
    private Role parentRole;

    @OneToMany(mappedBy = "parentRole", fetch = FetchType.LAZY)
    private Set<Role> childRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
