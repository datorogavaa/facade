package com.system.facede.service;

import com.system.facede.model.NotificationStatus;
import com.system.facede.repository.NotificationStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationStatusServiceTest {

    @Mock
    private NotificationStatusRepository repository;

    @InjectMocks
    private NotificationStatusService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnAllStatuses() {
        List<NotificationStatus> statuses = List.of(new NotificationStatus());
        when(repository.findAll()).thenReturn(statuses);

        List<NotificationStatus> result = service.getAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void getById_shouldReturnStatusIfFound() {
        NotificationStatus status = new NotificationStatus();
        status.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(status));

        Optional<NotificationStatus> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getByCustomUserId_shouldReturnStatuses() {
        List<NotificationStatus> statuses = List.of(new NotificationStatus());
        when(repository.findByCustomUserId(42L)).thenReturn(statuses);

        List<NotificationStatus> result = service.getByCustomUserId(42L);

        assertEquals(1, result.size());
        verify(repository).findByCustomUserId(42L);
    }

    @Test
    void save_shouldPersistStatus() {
        NotificationStatus status = new NotificationStatus();
        when(repository.save(status)).thenReturn(status);

        NotificationStatus result = service.save(status);

        assertSame(status, result);
        verify(repository).save(status);
    }

    @Test
    void delete_shouldCallRepository() {
        service.delete(5L);
        verify(repository).deleteById(5L);
    }
}
