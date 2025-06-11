package com.system.facede.repository;

import com.system.facede.model.AdminUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") // Loads application-test.yml or .properties
class AdminUserRepositoryTest {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    void findByUsername_shouldReturnUserIfExists() {
        AdminUser user = new AdminUser();
        user.setUsername("testusersuper123");
        user.setPassword("password");
        user.setRole("SUPER_ADMIN");

        adminUserRepository.save(user);

        Optional<AdminUser> result = adminUserRepository.findByUsername("superadmin");

        assertTrue(result.isPresent());
        assertEquals("SUPER_ADMIN", result.get().getRole());
    }

    @Test
    void findByUsername_shouldReturnEmptyIfNotExists() {
        Optional<AdminUser> result = adminUserRepository.findByUsername("unknown");
        assertTrue(result.isEmpty());
    }
}
