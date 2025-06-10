package com.system.facede.service;

import com.system.facede.dto.CustomUserBatchUpdateRequest;
import com.system.facede.model.CustomUser;
import com.system.facede.repository.CustomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public List<CustomUser> getFilteredUsers(String name, String email, String phone) {
        return customUserRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndPhoneNumberContaining(
                name != null ? name : "",
                email != null ? email : "",
                phone != null ? phone : "");
    }


    public Optional<CustomUser> getById(Long id) {
        return customUserRepository.findById(id);
    }

    public CustomUser save(CustomUser user) {
        return customUserRepository.save(user);
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

