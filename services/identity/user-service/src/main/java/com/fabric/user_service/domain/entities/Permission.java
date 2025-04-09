package com.fabric.user_service.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"resource", "action"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity{
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private String resource; // e.g., "user", "invoice", "report"

    @Column(nullable = false)
    private String action; // e.g., "read", "write", "approve"

    @Column(nullable = false)
    private Boolean requiredForInternal; // İç kullanıcılar için gerekli mi

    @Column(nullable = false)
    private Boolean requiredForExternal; // Dış kullanıcılar için gerekli mi

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermissions = new HashSet<>();
}
