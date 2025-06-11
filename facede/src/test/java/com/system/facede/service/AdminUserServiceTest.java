package com.system.facede.service;

import com.system.facede.exception.PasswordEmptyException;
import com.system.facede.exception.UsernameAlreadyExistsException;
import com.system.facede.exception.UsernameEmptyException;
import com.system.facede.model.AdminUser;
import com.system.facede.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminUserServiceTest {

    @Mock private AdminUserRepository adminUserRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AdminUserService adminUserService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAdmins_shouldReturnList() {
        List<AdminUser> admins = List.of(new AdminUser());
        when(adminUserRepository.findAll()).thenReturn(admins);

        List<AdminUser> result = adminUserService.getAllAdmins();

        assertEquals(1, result.size());
        verify(adminUserRepository).findAll();
    }

    @Test
    void getById_shouldReturnUser() {
        AdminUser admin = new AdminUser();
        admin.setId(1L);
        when(adminUserRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<AdminUser> result = adminUserService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void save_shouldThrowIfUsernameExists() {
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPassword("secret");

        when(adminUserRepository.findByUsername("admin"))
                .thenReturn(Optional.of(new AdminUser()));

        assertThrows(UsernameAlreadyExistsException.class, () -> adminUserService.save(admin));
    }

    @Test
    void save_shouldThrowIfUsernameIsEmpty() {
        AdminUser admin = new AdminUser();
        admin.setPassword("secret");

        admin.setUsername("");
        assertThrows(UsernameEmptyException.class, () -> adminUserService.save(admin));
    }

    @Test
    void save_shouldThrowIfPasswordIsEmpty() {
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");

        admin.setPassword("");
        assertThrows(PasswordEmptyException.class, () -> adminUserService.save(admin));
    }

    @Test
    void save_shouldSetDefaultsAndEncodePassword() {
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPassword("secret");

        when(adminUserRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(adminUserRepository.save(any(AdminUser.class))).thenAnswer(inv -> inv.getArgument(0));

        AdminUser saved = adminUserService.save(admin);

        assertEquals("encoded", saved.getPassword());
        assertEquals("ADMIN", saved.getRole());
    }

    @Test
    void update_shouldThrowIfUserNotFound() {
        AdminUser admin = new AdminUser();
        admin.setId(1L);
        when(adminUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> adminUserService.update(admin));
    }

    @Test
    void update_shouldUpdateUsernameAndPassword() {
        AdminUser admin = new AdminUser();
        admin.setId(1L);
        admin.setUsername("newuser");
        admin.setPassword("newpass");

        AdminUser existing = new AdminUser();
        existing.setId(1L);
        existing.setUsername("olduser");

        when(adminUserRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(adminUserRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newpass")).thenReturn("encodedpass");
        when(adminUserRepository.save(any(AdminUser.class))).thenAnswer(inv -> inv.getArgument(0));

        AdminUser result = adminUserService.update(admin);

        assertEquals("newuser", result.getUsername());
        assertEquals("encodedpass", result.getPassword());
    }

    @Test
    void delete_shouldCallRepository() {
        adminUserService.delete(1L);
        verify(adminUserRepository).deleteById(1L);
    }
}
