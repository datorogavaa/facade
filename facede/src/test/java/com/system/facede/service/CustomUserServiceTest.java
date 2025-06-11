package com.system.facede.service;

import com.system.facede.dto.CustomUserBatchUpdateRequest;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.CustomUserRepository;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserServiceTest {

    @Mock private CustomUserRepository customUserRepository;
    @Mock private NotificationPreferenceRepository preferenceRepository;

    @InjectMocks private CustomUserService customUserService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        List<CustomUser> users = List.of(new CustomUser());
        when(customUserRepository.findAll()).thenReturn(users);

        List<CustomUser> result = customUserService.getAll();

        assertEquals(1, result.size());
        verify(customUserRepository).findAll();
    }

    @Test
    void getById_shouldReturnUserIfExists() {
        CustomUser user = new CustomUser();
        user.setId(1L);
        when(customUserRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<CustomUser> result = customUserService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getFilteredUsers_shouldReturnFilteredList() {
        List<CustomUser> users = List.of(new CustomUser());
        Sort sort = Sort.by("name");
        when(customUserRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneNumberContaining(
                "", "", "", sort)).thenReturn(users);

        List<CustomUser> result = customUserService.getFilteredUsers(null, null, null, sort);

        assertEquals(1, result.size());
    }

    @Test
    void save_shouldPersistUserAndCreateDefaultNotificationPreference() {
        CustomUser user = new CustomUser();
        user.setId(1L);
        when(customUserRepository.save(any())).thenReturn(user);
        when(preferenceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CustomUser saved = customUserService.save(user);

        assertEquals(1L, saved.getId());
        verify(customUserRepository).save(user);
        verify(preferenceRepository).save(any(NotificationPreference.class));
    }

    @Test
    void existsByEmail_shouldReturnTrueIfExists() {
        when(customUserRepository.existsByEmail("email@test.com")).thenReturn(true);

        assertTrue(customUserService.existsByEmail("email@test.com"));
    }

    @Test
    void existsByName_shouldReturnTrueIfExists() {
        when(customUserRepository.existsByName("John")).thenReturn(true);

        assertTrue(customUserService.existsByName("John"));
    }

    @Test
    void existsByPhoneNumber_shouldReturnTrueIfExists() {
        when(customUserRepository.existsByPhoneNumber("12345")).thenReturn(true);

        assertTrue(customUserService.existsByPhoneNumber("12345"));
    }

    @Test
    void findByEmail_shouldReturnOptionalUser() {
        CustomUser user = new CustomUser();
        when(customUserRepository.findByEmail("e@test.com")).thenReturn(Optional.of(user));

        Optional<CustomUser> result = customUserService.findByEmail("e@test.com");

        assertTrue(result.isPresent());
    }

    @Test
    void updateUsersBatch_shouldThrowIfNullOrEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> customUserService.updateUsersBatch(null));
        assertThrows(IllegalArgumentException.class, () -> customUserService.updateUsersBatch(List.of()));
    }

    @Test
    void updateUsersBatch_shouldCallUpdateForEachRequest() {
        CustomUserBatchUpdateRequest req1 = new CustomUserBatchUpdateRequest();
        req1.setUserId(1L);
        req1.setName("Name1");
        req1.setEmail("email1@test.com");
        req1.setPhoneNumber("123");

        CustomUserBatchUpdateRequest req2 = new CustomUserBatchUpdateRequest();
        req2.setUserId(2L);
        req2.setName("Name2");
        req2.setEmail("email2@test.com");
        req2.setPhoneNumber("456");

        customUserService.updateUsersBatch(List.of(req1, req2));

        verify(customUserRepository).updateUser(1L, "Name1", "email1@test.com", "123");
        verify(customUserRepository).updateUser(2L, "Name2", "email2@test.com", "456");
    }

    @Test
    void updateUsersBatch_shouldThrowIfUserIdIsNull() {
        CustomUserBatchUpdateRequest req = new CustomUserBatchUpdateRequest();
        req.setUserId(null);

        assertThrows(IllegalArgumentException.class, () -> customUserService.updateUsersBatch(List.of(req)));
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        customUserService.delete(42L);
        verify(customUserRepository).deleteById(42L);
    }
}
