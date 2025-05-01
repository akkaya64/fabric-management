package com.fabric.user_service.repository;

import com.fabric.user_service.domain.entities.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    List<UserContact> findByUserId(Long userId);

    @Query("SELECT c FROM UserContact c WHERE c.user.id = :userId AND c.type = :type")
    List<UserContact> findByUserIdAndType(@Param("userId") Long userId, @Param("type") UserContact.ContactType type);

    @Query("SELECT c FROM UserContact c WHERE c.user.id = :userId AND c.type = :type AND c.primary = true")
    Optional<UserContact> findPrimaryByUserIdAndType(@Param("userId") Long userId, @Param("type") UserContact.ContactType type);

    @Query("SELECT c FROM UserContact c WHERE c.type = :type AND c.value = :value")
    Optional<UserContact> findByTypeAndValue(@Param("type") UserContact.ContactType type, @Param("value") String value);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM UserContact c WHERE c.type = :type AND c.value = :value")
    boolean existsByTypeAndValue(@Param("type") UserContact.ContactType type, @Param("value") String value);
}