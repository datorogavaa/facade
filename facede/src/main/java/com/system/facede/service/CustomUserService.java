package com.system.facede.service;

import com.system.facede.dto.CustomUserBatchUpdateRequest;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import com.system.facede.repository.CustomUserRepository;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    private final CustomUserRepository customUserRepository;
    private final NotificationPreferenceRepository preferenceRepository;

    public CustomUserService(CustomUserRepository customUserRepository,
                             NotificationPreferenceRepository preferenceRepository) {
        this.customUserRepository = customUserRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public List<CustomUser> getAll() {
        logger.info("Fetching all custom users");
        return customUserRepository.findAll();
    }

    public List<CustomUser> getFilteredUsers(String name, String email, String phone, Sort sort) {
        logger.info("Fetching filtered users with name: {}, email: {}, phone: {}", name, email, phone);
        return customUserRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneNumberContaining(
                name != null ? name : "",
                email != null ? email : "",
                phone != null ? phone : "",
                sort);
    }

    public Optional<CustomUser> getById(Long id) {
        logger.info("Fetching custom user by ID: {}", id);
        return customUserRepository.findById(id);
    }

    public CustomUser save(CustomUser user) {
        logger.info("Saving custom user: {}", user.getName());
        CustomUser savedUser = customUserRepository.save(user);

        NotificationPreference defaultPref = new NotificationPreference();
        defaultPref.setCustomUser(savedUser);
        defaultPref.setEmailEnabled(true);
        defaultPref.setSmsEnabled(false);
        defaultPref.setPostalEnabled(false);

        preferenceRepository.save(defaultPref);

        logger.info("Custom user saved with ID: {}", savedUser.getId());
        return savedUser;
    }

    public boolean existsByEmail(String email) {
        logger.info("Checking if custom user exists by email: {}", email);
        return customUserRepository.existsByEmail(email);
    }

    public boolean existsByName(String name) {
        logger.info("Checking if custom user exists by name: {}", name);
        return customUserRepository.existsByName(name);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        logger.info("Checking if custom user exists by phone number: {}", phoneNumber);
        return customUserRepository.existsByPhoneNumber(phoneNumber);
    }

    public Optional<CustomUser> findByEmail(String email) {
        logger.info("Finding custom user by email: {}", email);
        return customUserRepository.findByEmail(email);
    }

    public Optional<CustomUser> findByName(String name) {
        logger.info("Finding custom user by name: {}", name);
        return customUserRepository.findByName(name);
    }

    public Optional<CustomUser> findByPhoneNumber(String phoneNumber) {
        logger.info("Finding custom user by phone number: {}", phoneNumber);
        return customUserRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    public void updateUsersBatch(List<CustomUserBatchUpdateRequest> requests) {
        logger.info("Batch updating custom users, count: {}", requests != null ? requests.size() : 0);
        if (requests == null || requests.isEmpty()) {
            logger.error("Requests list cannot be empty.");
            throw new IllegalArgumentException("Requests list cannot be empty.");
        }
        for (CustomUserBatchUpdateRequest request : requests) {
            if (request.getUserId() == null) {
                logger.error("User ID cannot be null in batch update.");
                throw new IllegalArgumentException("User ID cannot be null.");
            }
            logger.info("Updating user with ID: {}", request.getUserId());
            customUserRepository.updateUser(
                    request.getUserId(),
                    request.getName(),
                    request.getEmail(),
                    request.getPhoneNumber()
            );
        }
    }

    public void delete(Long id) {
        logger.info("Deleting custom user with ID: {}", id);
        customUserRepository.deleteById(id);
        logger.info("Custom user deleted with ID: {}", id);
    }
}