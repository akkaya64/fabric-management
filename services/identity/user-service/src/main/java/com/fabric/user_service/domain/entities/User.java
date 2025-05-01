package com.fabric.user_service.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private boolean enabled = false;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserContact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserCompany> companies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    public void addContact(UserContact contact) {
        contacts.add(contact);
        contact.setUser(this);
    }

    public void removeContact(UserContact contact) {
        contacts.remove(contact);
        contact.setUser(null);
    }

    public void addCompany(UserCompany company) {
        companies.add(company);
        company.setUser(this);
    }

    public void removeCompany(UserCompany company) {
        companies.remove(company);
        company.setUser(null);
    }

    public void addVerificationToken(VerificationToken token) {
        verificationTokens.add(token);
        token.setUser(this);
    }
}