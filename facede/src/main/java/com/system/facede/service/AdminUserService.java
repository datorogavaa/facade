package com.system.facede.service;

import com.system.facede.exception.PasswordEmptyException;
import com.system.facede.exception.UsernameAlreadyExistsException;
import com.system.facede.exception.UsernameEmptyException;
import com.system.facede.model.AdminUser;
import com.system.facede.repository.AdminUserRepository;
import com.system.facede.security.AdminUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserService.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AdminUser> getAllAdmins() {
        logger.info("Fetching all admin users");
        return adminUserRepository.findAll();
    }

    public Optional<AdminUser> getById(Long id) {
        logger.info("Fetching admin user by ID: {}", id);
        return adminUserRepository.findById(id);
    }

    public Optional<AdminUser> getByUsername(String username) {
        logger.info("Fetching admin user by username: {}", username);
        return adminUserRepository.findByUsername(username);
    }

    public AdminUser save(AdminUser adminUser) {
        logger.info("Saving new admin user: {}", adminUser.getUsername());
        if (adminUser.getId() != null) {
            logger.error("User ID must be null for creation.");
            throw new IllegalArgumentException("User ID must be null for creation.");
        }

        Optional<AdminUser> existingUserOpt = getByUsername(adminUser.getUsername());
        if (existingUserOpt.isPresent()) {
            logger.error("Username already exists: {}", adminUser.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        if (adminUser.getPassword() == null || adminUser.getPassword().isEmpty()) {
            logger.error("Password must not be empty for user: {}", adminUser.getUsername());
            throw new PasswordEmptyException("Password must not be empty.");
        } else if (adminUser.getUsername() == null || adminUser.getUsername().isEmpty()) {
            logger.error("Username must not be empty.");
            throw new UsernameEmptyException("Username must not be empty.");
        }
        String encodedPassword = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encodedPassword);
        if (adminUser.getRole() == null || adminUser.getRole().isBlank()) {
            adminUser.setRole("ADMIN");
        }
        AdminUser savedUser = adminUserRepository.save(adminUser);
        logger.info("Admin user saved with ID: {}", savedUser.getId());
        return savedUser;
    }

    public AdminUser update(AdminUser adminUser) {
        logger.info("Updating admin user with ID: {}", adminUser.getId());
        if (adminUser.getId() == null) {
            logger.error("User ID must not be null for update.");
            throw new IllegalArgumentException("User ID must not be null for update.");
        }

        Optional<AdminUser> existingUserOpt = adminUserRepository.findById(adminUser.getId());
        if (existingUserOpt.isEmpty()) {
            logger.error("Admin user not found with ID: {}", adminUser.getId());
            throw new IllegalArgumentException("Admin user not found.");
        }

        AdminUser existingUser = existingUserOpt.get();

        if (!existingUser.getUsername().equals(adminUser.getUsername())) {
            Optional<AdminUser> userWithSameUsername = getByUsername(adminUser.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(adminUser.getId())) {
                logger.error("Username already exists: {}", adminUser.getUsername());
                throw new UsernameAlreadyExistsException("Username already exists.");
            }
            existingUser.setUsername(adminUser.getUsername());
        }
        if (adminUser.getPassword() != null && !adminUser.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(adminUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        AdminUser updatedUser = adminUserRepository.save(existingUser);
        logger.info("Admin user updated with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    public AdminUser getCurrentAdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("No authenticated admin user found.");
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof AdminUserDetails) {
            logger.info("Current admin user retrieved from security context.");
            return ((AdminUserDetails) principal).getAdminUser();
        }

        logger.warn("Principal is not an instance of AdminUserDetails.");
        return null;
    }

    public void delete(Long id) {
        logger.info("Deleting admin user with ID: {}", id);
        adminUserRepository.deleteById(id);
        logger.info("Admin user deleted with ID: {}", id);
    }
}