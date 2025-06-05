package com.system.facede.repository;

import com.system.facede.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
}
