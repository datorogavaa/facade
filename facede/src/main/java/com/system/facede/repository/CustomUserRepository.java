package com.system.facede.repository;

import com.system.facede.model.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE CustomUser u SET u.name = :name, u.email = :email, u.phoneNumber = :phoneNumber WHERE u.id = :userId")
    void updateUser(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber);

    List<CustomUser> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneNumberContaining(
            String name, String email, String phoneNumber, Sort sort);

    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByPhoneNumber(String phoneNumber);

}
