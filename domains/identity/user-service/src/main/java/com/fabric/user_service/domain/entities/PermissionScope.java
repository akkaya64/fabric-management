package com.fabric.user_service.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permission_scopes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionScope extends BaseEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 20, nullable = false, unique = true)
    private String code; // "SELF", "DEPARTMENT", "COMPANY", "GLOBAL"

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "scope", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
