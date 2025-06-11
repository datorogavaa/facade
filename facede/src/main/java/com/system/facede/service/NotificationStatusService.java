package com.system.facede.service;

import com.system.facede.model.NotificationStatus;
import com.system.facede.repository.NotificationStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationStatusService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationStatusService.class);

    private final NotificationStatusRepository repository;

    public NotificationStatusService(NotificationStatusRepository repository) {
        this.repository = repository;
    }

    public List<NotificationStatus> getAll() {
        logger.info("Fetching all notification statuses");
        return repository.findAll();
    }

    public Optional<NotificationStatus> getById(Long id) {
        logger.info("Fetching notification status by ID: {}", id);
        return repository.findById(id);
    }

    public List<NotificationStatus> getByCustomUserId(Long customUserId) {
        logger.info("Fetching notification statuses by custom user ID: {}", customUserId);
        return repository.findByCustomUserId(customUserId);
    }

    public NotificationStatus save(NotificationStatus status) {
        logger.info("Saving notification status for user ID: {}",
                status.getCustomUser() != null ? status.getCustomUser().getId() : null);
        NotificationStatus saved = repository.save(status);
        logger.info("Notification status saved with ID: {}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        logger.info("Deleting notification status with ID: {}", id);
        repository.deleteById(id);
        logger.info("Notification status deleted with ID: {}", id);
    }
}