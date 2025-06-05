package com.system.facede.service;

import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.springframework.stereotype.Service;

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
        return repository.findByCustomerUserId(customerUserId);
    }

    public NotificationPreference save(NotificationPreference preference) {
        return repository.save(preference);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
