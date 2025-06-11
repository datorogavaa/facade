package com.system.facede.service;

import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationPreferenceServiceTest {

    @Mock
    private NotificationPreferenceRepository repository;

    @InjectMocks
    private NotificationPreferenceService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAllPreferences() {
        List<NotificationPreference> prefs = List.of(new NotificationPreference());
        when(repository.findAll()).thenReturn(prefs);

        List<NotificationPreference> result = service.getAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void getById_shouldReturnPreferenceIfExists() {
        NotificationPreference pref = new NotificationPreference();
        pref.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(pref));

        Optional<NotificationPreference> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getByCustomerUserId_shouldReturnPreference() {
        NotificationPreference pref = new NotificationPreference();
        when(repository.findByCustomUserId(5L)).thenReturn(Optional.of(pref));

        Optional<NotificationPreference> result = service.getByCustomerUserId(5L);

        assertTrue(result.isPresent());
    }

    @Test
    void save_shouldPersistPreference() {
        NotificationPreference pref = new NotificationPreference();
        when(repository.save(pref)).thenReturn(pref);

        NotificationPreference result = service.save(pref);

        assertSame(pref, result);
        verify(repository).save(pref);
    }

    @Test
    void delete_shouldInvokeRepository() {
        service.delete(10L);
        verify(repository).deleteById(10L);
    }

    @Test
    void updateNotificationPreferencesBatch_shouldThrowIfListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                service.updateNotificationPreferencesBatch(List.of(), true, false, false)
        );
    }

    @Test
    void updateNotificationPreferencesBatch_shouldCallRepositoryForValidList() {
        List<Long> userIds = List.of(1L, 2L, 3L);

        service.updateNotificationPreferencesBatch(userIds, true, false, true);

        verify(repository).updatePreferencesForMultipleUsers(userIds, true, false, true);
    }
}
