package com.fabric.user_service.repository;

import com.fabric.user_service.domain.entities.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {

    List<UserCompany> findByUserId(Long userId);

    @Query("SELECT uc FROM UserCompany uc WHERE uc.user.id = :userId AND uc.companyId = :companyId")
    Optional<UserCompany> findByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Long companyId);

    @Query("SELECT uc FROM UserCompany uc WHERE uc.companyId = :companyId AND uc.userRole = :role")
    List<UserCompany> findByCompanyIdAndRole(@Param("companyId") Long companyId, @Param("role") String role);

    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END FROM UserCompany uc WHERE uc.user.id = :userId AND uc.companyId = :companyId")
    boolean existsByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Long companyId);

    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END FROM UserCompany uc WHERE uc.user.id = :userId AND uc.companyId = :companyId AND uc.userRole = :role")
    boolean existsByUserIdAndCompanyIdAndRole(@Param("userId") Long userId, @Param("companyId") Long companyId, @Param("role") String role);
}