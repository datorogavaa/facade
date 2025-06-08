package com.system.facede.service;

import com.system.facede.exception.PasswordEmptyException;
import com.system.facede.exception.UsernameAlreadyExistsException;
import com.system.facede.exception.UsernameEmptyException;
import com.system.facede.model.AdminUser;
import com.system.facede.repository.AdminUserRepository;
import com.system.facede.security.AdminUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AdminUser> getAllAdmins() {
        return adminUserRepository.findAll();
    }

    public Optional<AdminUser> getById(Long id) {
        return adminUserRepository.findById(id);
    }

    public Optional<AdminUser> getByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    public AdminUser save(AdminUser adminUser) {
        if (adminUser.getId() != null) {
            throw new IllegalArgumentException("User ID must be null for creation.");
        }

        Optional<AdminUser> existingUserOpt = getByUsername(adminUser.getUsername());
        if (existingUserOpt.isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        if (adminUser.getPassword() == null || adminUser.getPassword().isEmpty()) {
            throw new PasswordEmptyException("Password must not be empty.");
        }
        if (adminUser.getUsername() == null || adminUser.getUsername().isEmpty()) {
            throw new UsernameEmptyException("Username must not be empty.");
        }
        String encodedPassword = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encodedPassword);

        return adminUserRepository.save(adminUser);
    }


    public AdminUser getCurrentAdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof AdminUserDetails) {
            return ((AdminUserDetails) principal).getAdminUser();
        }

        return null;
    }


    public void delete(Long id) {
        adminUserRepository.deleteById(id);
    }
}
