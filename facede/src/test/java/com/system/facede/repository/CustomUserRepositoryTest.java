package com.system.facede.repository;

import com.system.facede.model.CustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CustomUserRepositoryTest {

    @Autowired
    private CustomUserRepository customUserRepository;

    private CustomUser testUser;

    @BeforeEach
    void setUp() {
        customUserRepository.deleteAll(); // Ensure clean state

        testUser = new CustomUser();
        testUser.setName("Alice Smith");
        testUser.setEmail("alice@example.com");
        testUser.setPhoneNumber("123456789");

        testUser = customUserRepository.save(testUser);
    }

    @Test
    void updateUser_shouldModifyNameEmailPhoneById() {
        customUserRepository.updateUser(
                testUser.getId(),
                "Alice Updated",
                "updated@example.com",
                "987654321"
        );

        Optional<CustomUser> updated = customUserRepository.findById(testUser.getId());

        assertTrue(updated.isPresent());
        assertEquals("Alice Updated", updated.get().getName());
        assertEquals("updated@example.com", updated.get().getEmail());
        assertEquals("987654321", updated.get().getPhoneNumber());
    }

    @Test
    void findByNameContainingEmailContainingPhoneContaining_shouldReturnMatch() {
        List<CustomUser> result = customUserRepository
                .findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneNumberContaining(
                        "alice", "example", "123", Sort.by("name"));

        assertEquals(1, result.size());
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        assertTrue(customUserRepository.existsByEmail("alice@example.com"));
        assertFalse(customUserRepository.existsByEmail("notfound@example.com"));
    }

    @Test
    void existsByName_shouldReturnTrue() {
        assertTrue(customUserRepository.existsByName("Alice Smith"));
        assertFalse(customUserRepository.existsByName("Bob"));
    }

    @Test
    void existsByPhone_shouldReturnTrue() {
        assertTrue(customUserRepository.existsByPhoneNumber("123456789"));
        assertFalse(customUserRepository.existsByPhoneNumber("000000000"));
    }

    @Test
    void findByEmail_shouldReturnOptional() {
        Optional<CustomUser> result = customUserRepository.findByEmail("alice@example.com");
        assertTrue(result.isPresent());
    }

    @Test
    void findByName_shouldReturnOptional() {
        Optional<CustomUser> result = customUserRepository.findByName("Alice Smith");
        assertTrue(result.isPresent());
    }

    @Test
    void findByPhone_shouldReturnOptional() {
        Optional<CustomUser> result = customUserRepository.findByPhoneNumber("123456789");
        assertTrue(result.isPresent());
    }
}
