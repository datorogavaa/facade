package com.system.facede.service;

import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationPreferenceService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationPreferenceService.class);

    private final NotificationPreferenceRepository repository;

    public NotificationPreferenceService(NotificationPreferenceRepository repository) {
        this.repository = repository;
    }

    public List<NotificationPreference> getAll() {
        logger.info("Fetching all notification preferences");
        return repository.findAll();
    }

    public Optional<NotificationPreference> getById(Long id) {
        logger.info("Fetching notification preference by ID: {}", id);
        return repository.findById(id);
    }

    public Optional<NotificationPreference> getByCustomerUserId(Long customerUserId) {
        logger.info("Fetching notification preference by custom user ID: {}", customerUserId);
        return repository.findByCustomUserId(customerUserId);
    }

    public NotificationPreference save(NotificationPreference preference) {
        logger.info("Saving notification preference for user ID: {}",
                preference.getCustomUser() != null ? preference.getCustomUser().getId() : null);
        NotificationPreference saved = repository.save(preference);
        logger.info("Notification preference saved with ID: {}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        logger.info("Deleting notification preference with ID: {}", id);
        repository.deleteById(id);
        logger.info("Notification preference deleted with ID: {}", id);
    }

    @Transactional
    public void updateNotificationPreferencesBatch(List<Long> userIds, boolean emailEnabled, boolean smsEnabled, boolean postalEnabled) {
        logger.info("Batch updating notification preferences for user IDs: {}", userIds);
        if (userIds == null || userIds.isEmpty()) {
            logger.error("User IDs list cannot be empty.");
            throw new IllegalArgumentException("User IDs list cannot be empty.");
        }
        repository.updatePreferencesForMultipleUsers(userIds, emailEnabled, smsEnabled, postalEnabled);
        logger.info("Batch update completed for user IDs: {}", userIds);
    }
}