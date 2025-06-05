package com.system.facede.service;

import com.system.facede.model.NotificationStatus;
import com.system.facede.repository.NotificationStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationStatusService {
    private final NotificationStatusRepository repository;

    public NotificationStatusService(NotificationStatusRepository repository) {
        this.repository = repository;
    }

    public List<NotificationStatus> getAll() {
        return repository.findAll();
    }

    public Optional<NotificationStatus> getById(Long id) {
        return repository.findById(id);
    }

    public List<NotificationStatus> getByCustomerUserId(Long customerUserId) {
        return repository.findByCustomerUserId(customerUserId);
    }

    public NotificationStatus save(NotificationStatus status) {
        return repository.save(status);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
