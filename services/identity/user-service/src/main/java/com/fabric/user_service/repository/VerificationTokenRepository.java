package com.fabric.user_service.repository;

import com.fabric.user_service.domain.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    @Query("SELECT t FROM VerificationToken t WHERE t.user.id = :userId AND t.tokenType = :tokenType AND t.used = false AND t.expiryDate > :now")
    List<VerificationToken> findValidTokensByUserAndType(
            @Param("userId") Long userId,
            @Param("tokenType") VerificationToken.TokenType tokenType,
            @Param("now") LocalDateTime now);

    @Query("SELECT t FROM VerificationToken t WHERE t.targetIdentifier = :target AND t.tokenType = :tokenType AND t.used = false AND t.expiryDate > :now")
    List<VerificationToken> findValidTokensByTargetAndType(
            @Param("target") String target,
            @Param("tokenType") VerificationToken.TokenType tokenType,
            @Param("now") LocalDateTime now);

    @Query("SELECT t FROM VerificationToken t WHERE t.user.id = :userId AND t.tokenType = :tokenType AND t.used = false AND t.expiryDate > :now ORDER BY t.createdAt DESC")
    Optional<VerificationToken> findLatestValidTokenByUserAndType(
            @Param("userId") Long userId,
            @Param("tokenType") VerificationToken.TokenType tokenType,
            @Param("now") LocalDateTime now);
}