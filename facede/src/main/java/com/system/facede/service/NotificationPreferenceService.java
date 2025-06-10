package com.system.facede.service;

import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationPreferenceService {
    private final NotificationPreferenceRepository repository;

    public NotificationPreferenceService(NotificationPreferenceRepository repository) {
        this.repository = repository;
    }

    public List<NotificationPreference> getAll() {
        return repository.findAll();
    }

    public Optional<NotificationPreference> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<NotificationPreference> getByCustomerUserId(Long customerUserId) {
        return repository.findByCustomUserId(customerUserId);
    }

    public NotificationPreference save(NotificationPreference preference) {
        return repository.save(preference);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Batch update method for notification preferences
    @Transactional
    public void updateNotificationPreferencesBatch(List<Long> userIds, boolean emailEnabled, boolean smsEnabled, boolean postalEnabled) {
        // Validate the userIds list
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("User IDs list cannot be empty.");
        }

        // Perform the batch update using the custom query in the repository
        repository.updatePreferencesForMultipleUsers(userIds, emailEnabled, smsEnabled, postalEnabled);
    }


    public List<NotificationPreference> searchAndSort(String username, String preferenceType, Sort sort) {
        return repository.findWithFilters(username, preferenceType, sort);
    }

}
