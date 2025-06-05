package com.system.facede.service;

import com.system.facede.model.CustomUser;
import com.system.facede.repository.CustomUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService {
    private final CustomUserRepository customUserRepository;

    public CustomUserService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    public List<CustomUser> getAll() {
        return customUserRepository.findAll();
    }

    public Optional<CustomUser> getById(Long id) {
        return customUserRepository.findById(id);
    }

    public CustomUser save(CustomUser user) {
        return customUserRepository.save(user);
    }

    public void delete(Long id) {
        customUserRepository.deleteById(id);
    }
}

