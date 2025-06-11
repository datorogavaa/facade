package com.system.facede.service;

import com.system.facede.dto.CustomUserBatchUpdateRequest;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.CustomUserRepository;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService {
    private final CustomUserRepository customUserRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    public CustomUserService(CustomUserRepository customUserRepository,
                             NotificationPreferenceRepository preferenceRepository) {
        this.customUserRepository = customUserRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public List<CustomUser> getAll() {
        return customUserRepository.findAll();
    }


    public List<CustomUser> getFilteredUsers(String name, String email, String phone, Sort sort) {
        return customUserRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneNumberContaining(
                name != null ? name : "",
                email != null ? email : "",
                phone != null ? phone : "",
                sort);
    }



    public Optional<CustomUser> getById(Long id) {
        return customUserRepository.findById(id);
    }

    public CustomUser save(CustomUser user) {
        CustomUser savedUser = customUserRepository.save(user);

        NotificationPreference defaultPref = new NotificationPreference();
        defaultPref.setCustomUser(savedUser);
        defaultPref.setEmailEnabled(true);
        defaultPref.setSmsEnabled(false);
        defaultPref.setPostalEnabled(false);

        preferenceRepository.save(defaultPref);

        return savedUser;
    }

    public boolean existsByEmail(String email) {
        return customUserRepository.existsByEmail(email);
    }

    public boolean existsByName(String name) {
        return customUserRepository.existsByName(name);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return customUserRepository.existsByPhoneNumber(phoneNumber);
    }



    @Transactional
    public void updateUsersBatch(List<CustomUserBatchUpdateRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Requests list cannot be empty.");
        }
        for (CustomUserBatchUpdateRequest request : requests) {
            if (request.getUserId() == null) {
                throw new IllegalArgumentException("User ID cannot be null.");
            }
            customUserRepository.updateUser(
                    request.getUserId(),
                    request.getName(),
                    request.getEmail(),
                    request.getPhoneNumber()
            );
        }
    }

    public void delete(Long id) {
        customUserRepository.deleteById(id);
    }
}

