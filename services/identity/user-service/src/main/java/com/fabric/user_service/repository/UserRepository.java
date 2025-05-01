package com.fabric.user_service.repository;


import com.fabric.user_service.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.contacts c WHERE c.type = 'EMAIL' AND c.value = :email")
    Optional<User> findByContactEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN u.contacts c WHERE c.type = 'PHONE' AND c.value = :phone")
    Optional<User> findByContactPhone(@Param("phone") String phone);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.contacts c WHERE c.type = 'EMAIL' AND c.value = :email")
    boolean existsByContactEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.contacts c WHERE c.type = 'PHONE' AND c.value = :phone")
    boolean existsByContactPhone(@Param("phone") String phone);
}
